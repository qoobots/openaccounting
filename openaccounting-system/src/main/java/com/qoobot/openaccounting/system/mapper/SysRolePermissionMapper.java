package com.qoobot.openaccounting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.system.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色权限关联Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 批量插入角色权限关联
     *
     * @param roleId       角色ID
     * @param permissionIds 权限ID列表
     * @return 插入数量
     */
    int batchInsert(@Param("roleId") Long roleId, @Param("permissionIds") Long[] permissionIds);

    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     * @return 删除数量
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
}
