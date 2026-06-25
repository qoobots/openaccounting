package com.qoobot.openaccounting.system.service;

import com.qoobot.openaccounting.system.dto.LoginRequest;
import com.qoobot.openaccounting.system.dto.PasswordChangeRequest;
import com.qoobot.openaccounting.system.dto.PasswordResetRequest;
import com.qoobot.openaccounting.system.dto.RefreshTokenRequest;
import com.qoobot.openaccounting.system.vo.LoginVO;
import com.qoobot.openaccounting.system.vo.TokenVO;

/**
 * 认证服务接口
 *
 * @author openaccounting
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginVO login(LoginRequest request);

    /**
     * 用户登出
     *
     * @param token 访问令牌
     */
    void logout(String token);

    /**
     * 刷新Token
     *
     * @param request 刷新Token请求
     * @return 新的Token
     */
    TokenVO refreshToken(RefreshTokenRequest request);

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    LoginVO getUserInfo(Long userId);

    /**
     * 修改密码
     *
     * @param userId  用户ID
     * @param request 修改密码请求
     */
    void changePassword(Long userId, PasswordChangeRequest request);

    /**
     * 重置密码（管理员操作）
     *
     * @param request 重置密码请求
     */
    void resetPassword(PasswordResetRequest request);
}
