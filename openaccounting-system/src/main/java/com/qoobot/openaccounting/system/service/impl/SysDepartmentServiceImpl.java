package com.qoobot.openaccounting.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.constant.ResultCode;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.system.entity.SysDepartment;
import com.qoobot.openaccounting.system.entity.SysUser;
import com.qoobot.openaccounting.system.mapper.SysDepartmentMapper;
import com.qoobot.openaccounting.system.mapper.SysUserMapper;
import com.qoobot.openaccounting.system.service.SysDepartmentService;
import com.qoobot.openaccounting.system.vo.SysDepartmentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 *
 * @author openaccounting
 */
@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {

    private final SysUserMapper userMapper;

    public SysDepartmentServiceImpl(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Page<SysDepartmentVO> selectDepartmentPage(Page<SysDepartmentVO> page, String departmentName, Integer status) {
        return baseMapper.selectDepartmentPage(page, departmentName, status);
    }

    @Override
    public List<SysDepartmentVO> selectDepartmentTree() {
        List<SysDepartment> allDepartments = baseMapper.selectList(new LambdaQueryWrapper<>());
        return buildTree(allDepartments, 0L);
    }

    @Override
    public List<SysDepartmentVO> selectByParentId(Long parentId) {
        List<SysDepartment> departments = baseMapper.selectByParentId(parentId);
        return departments.stream().map(dept -> {
            SysDepartmentVO vo = new SysDepartmentVO();
            BeanUtils.copyProperties(dept, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDepartment(SysDepartment department) {
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getDepartmentCode, department.getDepartmentCode());
        SysDepartment existDept = baseMapper.selectOne(wrapper);
        if (existDept != null) {
            throw new BusinessException(ResultCode.CONFLICT, "部门代码已存在");
        }

        if (department.getParentId() != null && department.getParentId() > 0) {
            SysDepartment parentDept = baseMapper.selectById(department.getParentId());
            if (parentDept == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "父部门不存在");
            }
        }

        baseMapper.insert(department);
        return department.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDepartment(SysDepartment department) {
        SysDepartment existDept = baseMapper.selectById(department.getId());
        if (existDept == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "部门不存在");
        }

        if (!existDept.getDepartmentCode().equals(department.getDepartmentCode())) {
            LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysDepartment::getDepartmentCode, department.getDepartmentCode());
            wrapper.ne(SysDepartment::getId, department.getId());
            SysDepartment codeDept = baseMapper.selectOne(wrapper);
            if (codeDept != null) {
                throw new BusinessException(ResultCode.CONFLICT, "部门代码已存在");
            }
        }

        if (department.getParentId() != null && department.getParentId() > 0) {
            if (department.getParentId().equals(department.getId())) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "父部门不能是自己");
            }

            SysDepartment parentDept = baseMapper.selectById(department.getParentId());
            if (parentDept == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "父部门不存在");
            }
        }

        return baseMapper.updateById(department) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDepartment(Long id) {
        SysDepartment department = baseMapper.selectById(id);
        if (department == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "部门不存在");
        }

        LambdaQueryWrapper<SysDepartment> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(SysDepartment::getParentId, id);
        Long childCount = baseMapper.selectCount(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "该部门下有子部门，无法删除");
        }

        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getDeptId, id);
        Long userCount = userMapper.selectCount(userWrapper);
        if (userCount > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "该部门下有用户，无法删除");
        }

        return baseMapper.deleteById(id) > 0;
    }

    private List<SysDepartmentVO> buildTree(List<SysDepartment> departments, Long parentId) {
        List<SysDepartmentVO> tree = new ArrayList<>();
        for (SysDepartment dept : departments) {
            if (dept.getParentId() == null || dept.getParentId().equals(parentId)) {
                SysDepartmentVO vo = new SysDepartmentVO();
                BeanUtils.copyProperties(dept, vo);
                vo.setChildren(buildTree(departments, dept.getId()));
                tree.add(vo);
            }
        }
        return tree;
    }
}
