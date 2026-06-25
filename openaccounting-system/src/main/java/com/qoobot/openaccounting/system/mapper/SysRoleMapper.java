package com.qoobot.openaccounting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.system.entity.SysRole;
import com.qoobot.openaccounting.system.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色信息Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page      分页参数
     * @param roleName  角色名称
     * @param status    状态
     * @return 角色分页列表
     */
    Page<SysRoleVO> selectRolePage(Page<SysRoleVO> page, @Param("roleName") String roleName, @Param("status") Integer status);

    /**
     * 根据角色ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    SysRoleVO selectRoleById(@Param("id") Long id);

    /**
     * 根据角色代码查询角色
     *
     * @param roleCode 角色代码
     * @return 角色
     */
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);
}
