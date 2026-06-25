package com.qoobot.openaccounting.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.system.dto.SysUserDTO;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.entity.SysUser;
import com.qoobot.openaccounting.system.vo.SysUserVO;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author openaccounting
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param page    分页参数
     * @param username 用户名（模糊查询）
     * @param phone   手机号（模糊查询）
     * @param status  状态
     * @param deptId  部门ID
     * @return 用户分页列表
     */
    Page<SysUserVO> selectUserPage(Page<SysUserVO> page, String username, String phone, Integer status, Long deptId);

    /**
     * 根据用户ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    SysUserVO selectUserById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser selectByUsername(String username);

    /**
     * 创建用户
     *
     * @param userDTO 用户DTO
     * @return 用户ID
     */
    Long createUser(SysUserDTO userDTO);

    /**
     * 更新用户
     *
     * @param userDTO 用户DTO
     * @return 是否成功
     */
    Boolean updateUser(SysUserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean deleteUser(Long id);

    /**
     * 重置密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean resetPassword(Long id, String newPassword);

    /**
     * 分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    Boolean assignRoles(Long userId, Long[] roleIds);

    /**
     * 根据用户ID查询用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> getPermissionsByUserId(Long userId);
}
