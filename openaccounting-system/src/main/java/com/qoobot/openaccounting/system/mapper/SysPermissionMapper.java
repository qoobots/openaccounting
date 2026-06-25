package com.qoobot.openaccounting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.vo.SysPermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限信息Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 根据用户ID查询用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据父权限ID查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<SysPermission> selectByParentId(@Param("parentId") Long parentId);
}
