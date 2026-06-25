package com.qoobot.openaccounting.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.system.dto.SysRoleDTO;
import com.qoobot.openaccounting.system.entity.SysRole;
import com.qoobot.openaccounting.system.vo.SysRoleVO;

/**
 * 角色服务接口
 *
 * @author openaccounting
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page    分页参数
     * @param roleName 角色名称（模糊查询）
     * @param status  状态
     * @return 角色分页列表
     */
    Page<SysRoleVO> selectRolePage(Page<SysRoleVO> page, String roleName, Integer status);

    /**
     * 根据角色ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    SysRoleVO selectRoleById(Long id);

    /**
     * 根据角色代码查询角色
     *
     * @param roleCode 角色代码
     * @return 角色
     */
    SysRole selectByRoleCode(String roleCode);

    /**
     * 创建角色
     *
     * @param roleDTO 角色DTO
     * @return 角色ID
     */
    Long createRole(SysRoleDTO roleDTO);

    /**
     * 更新角色
     *
     * @param roleDTO 角色DTO
     * @return 是否成功
     */
    Boolean updateRole(SysRoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    Boolean deleteRole(Long id);

    /**
     * 分配权限
     *
     * @param roleId       角色ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    Boolean assignPermissions(Long roleId, Long[] permissionIds);
}
