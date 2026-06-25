package com.qoobot.openaccounting.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.vo.SysPermissionVO;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author openaccounting
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 查询权限树形结构
     *
     * @return 权限树
     */
    List<SysPermissionVO> selectPermissionTree();

    /**
     * 根据用户ID查询用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermissionVO> selectByUserId(Long userId);

    /**
     * 根据角色ID查询角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermissionVO> selectByRoleId(Long roleId);

    /**
     * 根据父权限ID查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<SysPermissionVO> selectByParentId(Long parentId);

    /**
     * 创建权限
     *
     * @param permission 权限
     * @return 权限ID
     */
    Long createPermission(SysPermission permission);

    /**
     * 更新权限
     *
     * @param permission 权限
     * @return 是否成功
     */
    Boolean updatePermission(SysPermission permission);

    /**
     * 删除权限
     *
     * @param id 权限ID
     * @return 是否成功
     */
    Boolean deletePermission(Long id);
}
