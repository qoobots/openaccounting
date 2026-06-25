package com.qoobot.openaccounting.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.system.entity.SysDepartment;
import com.qoobot.openaccounting.system.vo.SysDepartmentVO;

import java.util.List;

/**
 * 部门服务接口
 *
 * @author openaccounting
 */
public interface SysDepartmentService extends IService<SysDepartment> {

    /**
     * 分页查询部门列表
     *
     * @param page          分页参数
     * @param departmentName 部门名称（模糊查询）
     * @param status        状态
     * @return 部门分页列表
     */
    Page<SysDepartmentVO> selectDepartmentPage(Page<SysDepartmentVO> page, String departmentName, Integer status);

    /**
     * 查询部门树形结构
     *
     * @return 部门树
     */
    List<SysDepartmentVO> selectDepartmentTree();

    /**
     * 根据父部门ID查询子部门列表
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<SysDepartmentVO> selectByParentId(Long parentId);

    /**
     * 创建部门
     *
     * @param department 部门
     * @return 部门ID
     */
    Long createDepartment(SysDepartment department);

    /**
     * 更新部门
     *
     * @param department 部门
     * @return 是否成功
     */
    Boolean updateDepartment(SysDepartment department);

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 是否成功
     */
    Boolean deleteDepartment(Long id);
}
