package com.qoobot.openaccounting.system.service.impl;

import com.qoobot.openaccounting.common.constant.ResultCode;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.system.dto.LoginRequest;
import com.qoobot.openaccounting.system.dto.PasswordChangeRequest;
import com.qoobot.openaccounting.system.dto.PasswordResetRequest;
import com.qoobot.openaccounting.system.dto.RefreshTokenRequest;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.entity.SysRole;
import com.qoobot.openaccounting.system.entity.SysUser;
import com.qoobot.openaccounting.system.service.AuthService;
import com.qoobot.openaccounting.system.service.SysUserService;
import com.qoobot.openaccounting.system.utils.JwtTokenUtil;
import com.qoobot.openaccounting.system.vo.LoginVO;
import com.qoobot.openaccounting.system.vo.SysUserVO;
import com.qoobot.openaccounting.system.vo.TokenVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证服务实现
 *
 * @author openaccounting
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginRequest request) {
        SysUser user = userService.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (user.getStatus() != 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        SysUserVO userVO = userService.selectUserById(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("realName", user.getRealName());
        claims.put("deptId", user.getDeptId());
        claims.put("companyId", user.getCompanyId());

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getUsername(), claims);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getUsername());

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setExpiresIn(jwtTokenUtil.getExpiration());
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setRealName(user.getRealName());
        loginVO.setAvatar(user.getAvatar());
        loginVO.setRoles(extractRoles(userVO));
        loginVO.setPermissions(extractPermissions(user.getId()));

        log.info("用户登录成功: {}", user.getUsername());
        return loginVO;
    }

    @Override
    public void logout(String token) {
        if (token != null) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            log.info("用户登出: {}", username);
        }
    }

    @Override
    public LoginVO getUserInfo(Long userId) {
        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        SysUserVO userVO = userService.selectUserById(userId);

        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setRealName(user.getRealName());
        loginVO.setAvatar(user.getAvatar());
        loginVO.setRoles(extractRoles(userVO));
        loginVO.setPermissions(extractPermissions(user.getId()));

        return loginVO;
    }

    @Override
    public TokenVO refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenUtil.validateToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        Long userId = jwtTokenUtil.getUserIdFromToken(refreshToken);
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        SysUser user = userService.getById(userId);
        if (user == null || user.getStatus() != 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        SysUserVO userVO = userService.selectUserById(userId);

        Map<String, Object> claims = new HashMap<>();
        claims.put("realName", user.getRealName());
        claims.put("deptId", user.getDeptId());
        claims.put("companyId", user.getCompanyId());

        String accessToken = jwtTokenUtil.generateAccessToken(userId, username, claims);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userId, username);

        log.info("用户刷新Token成功: {}", username);

        return TokenVO.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtTokenUtil.getExpiration())
                .build();
    }

    @Override
    public void changePassword(Long userId, PasswordChangeRequest request) {
        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        String newPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPassword);
        userService.updateById(user);

        log.info("用户修改密码成功: {}", user.getUsername());
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        SysUser user = userService.getById(request.getUserId());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String newPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPassword);
        userService.updateById(user);

        log.info("管理员重置用户密码: {}", user.getUsername());
    }

    private List<String> extractRoles(SysUserVO userVO) {
        if (userVO.getRoles() == null) {
            return List.of();
        }
        return userVO.getRoles().stream()
                .map(SysUserVO.RoleInfo::getCode)
                .collect(Collectors.toList());
    }

    private List<String> extractPermissions(Long userId) {
        List<SysPermission> permissions = userService.getPermissionsByUserId(userId);
        return permissions.stream()
                .map(SysPermission::getPermissionCode)
                .filter(code -> code != null && !code.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}
