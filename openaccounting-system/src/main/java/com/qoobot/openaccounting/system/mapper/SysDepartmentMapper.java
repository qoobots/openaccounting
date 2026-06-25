package com.qoobot.openaccounting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.system.entity.SysDepartment;
import com.qoobot.openaccounting.system.vo.SysDepartmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门信息Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface SysDepartmentMapper extends BaseMapper<SysDepartment> {

    /**
     * 分页查询部门列表
     *
     * @param page           分页参数
     * @param departmentName 部门名称
     * @param status         状态
     * @return 部门分页列表
     */
    Page<SysDepartmentVO> selectDepartmentPage(Page<SysDepartmentVO> page, @Param("departmentName") String departmentName, @Param("status") Integer status);

    /**
     * 根据父部门ID查询子部门列表
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<SysDepartment> selectByParentId(@Param("parentId") Long parentId);
}
