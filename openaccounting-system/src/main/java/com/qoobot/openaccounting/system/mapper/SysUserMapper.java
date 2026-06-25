package com.qoobot.openaccounting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.system.entity.SysUser;
import com.qoobot.openaccounting.system.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param page     分页参数
     * @param username 用户名（模糊查询）
     * @param phone    手机号（模糊查询）
     * @param status   状态
     * @param deptId   部门ID
     * @return 用户分页列表
     */
    Page<SysUserVO> selectUserPage(Page<SysUserVO> page, @Param("username") String username,
                                   @Param("phone") String phone, @Param("status") Integer status,
                                   @Param("deptId") Long deptId);
}
