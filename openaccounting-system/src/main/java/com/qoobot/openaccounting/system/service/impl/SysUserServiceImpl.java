package com.qoobot.openaccounting.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.exception.DataNotFoundException;
import com.qoobot.openaccounting.system.dto.SysUserDTO;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.entity.SysRole;
import com.qoobot.openaccounting.system.entity.SysUser;
import com.qoobot.openaccounting.system.entity.SysUserRole;
import com.qoobot.openaccounting.system.mapper.SysPermissionMapper;
import com.qoobot.openaccounting.system.mapper.SysUserMapper;
import com.qoobot.openaccounting.system.mapper.SysUserRoleMapper;
import com.qoobot.openaccounting.system.service.SysUserService;
import com.qoobot.openaccounting.system.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 * @author openaccounting
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysPermissionMapper permissionMapper;

    @Override
    public Page<SysUserVO> selectUserPage(Page<SysUserVO> page, String username, String phone, Integer status, Long deptId) {
        return baseMapper.selectUserPage(page, username, phone, status, deptId);
    }

    @Override
    public SysUserVO selectUserById(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new DataNotFoundException("用户不存在", id);
        }
        SysUserVO userVO = BeanUtil.copyProperties(user, SysUserVO.class);
        // TODO: 查询用户角色
        return userVO;
    }

    @Override
    public SysUser selectByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(SysUserDTO userDTO) {
        // 校验用户名是否已存在
        SysUser existUser = selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        SysUser user = BeanUtil.copyProperties(userDTO, SysUser.class);
        // TODO: 密码加密
        if (StrUtil.isNotBlank(userDTO.getPassword())) {
            user.setPassword(userDTO.getPassword());
        }
        this.save(user);

        // 分配角色
        if (userDTO.getRoleIds() != null && userDTO.getRoleIds().length > 0) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(SysUserDTO userDTO) {
        SysUser user = this.getById(userDTO.getId());
        if (user == null) {
            throw new DataNotFoundException("用户不存在", userDTO.getId());
        }

        // 如果修改用户名，需要校验用户名是否已存在
        if (StrUtil.isNotBlank(userDTO.getUsername()) && !user.getUsername().equals(userDTO.getUsername())) {
            SysUser existUser = selectByUsername(userDTO.getUsername());
            if (existUser != null) {
                throw new RuntimeException("用户名已存在");
            }
        }

        SysUser updateUser = BeanUtil.copyProperties(userDTO, SysUser.class);
        // 不修改密码
        updateUser.setPassword(null);
        this.updateById(updateUser);

        // 更新角色
        if (userDTO.getRoleIds() != null) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUser(Long id) {
        // TODO: 校验是否是管理员
        SysUser user = this.getById(id);
        if (user == null) {
            throw new DataNotFoundException("用户不存在", id);
        }
        // 删除用户角色关联
        LambdaUpdateWrapper<SysUserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
        // 删除用户
        return this.removeById(id);
    }

    @Override
    public Boolean resetPassword(Long id, String newPassword) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new DataNotFoundException("用户不存在", id);
        }
        // TODO: 密码加密
        user.setPassword(newPassword);
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignRoles(Long userId, Long[] roleIds) {
        // 删除旧的角色关联
        LambdaUpdateWrapper<SysUserRole> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(deleteWrapper);

        // 添加新的角色关联
        if (roleIds != null && roleIds.length > 0) {
            List<SysUserRole> userRoles = List.of(roleIds).stream()
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());
            // 批量插入
            userRoles.forEach(userRoleMapper::insert);
        }
        return true;
    }

    @Override
    public List<SysPermission> getPermissionsByUserId(Long userId) {
        return permissionMapper.selectByUserId(userId);
    }
}
