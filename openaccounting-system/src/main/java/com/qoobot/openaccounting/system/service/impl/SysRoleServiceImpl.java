package com.qoobot.openaccounting.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.constant.ResultCode;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.system.dto.SysRoleDTO;
import com.qoobot.openaccounting.system.entity.SysRole;
import com.qoobot.openaccounting.system.entity.SysRolePermission;
import com.qoobot.openaccounting.system.mapper.SysRoleMapper;
import com.qoobot.openaccounting.system.mapper.SysRolePermissionMapper;
import com.qoobot.openaccounting.system.service.SysRoleService;
import com.qoobot.openaccounting.system.vo.SysRoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 角色服务实现
 *
 * @author openaccounting
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRolePermissionMapper rolePermissionMapper;

    public SysRoleServiceImpl(SysRolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Page<SysRoleVO> selectRolePage(Page<SysRoleVO> page, String roleName, Integer status) {
        return baseMapper.selectRolePage(page, roleName, status);
    }

    @Override
    public SysRoleVO selectRoleById(Long id) {
        SysRoleVO roleVO = baseMapper.selectRoleById(id);
        if (roleVO == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }
        return roleVO;
    }

    @Override
    public SysRole selectByRoleCode(String roleCode) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleCode, roleCode);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(SysRoleDTO roleDTO) {
        SysRole existRole = selectByRoleCode(roleDTO.getRoleCode());
        if (existRole != null) {
            throw new BusinessException(ResultCode.CONFLICT, "角色代码已存在");
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleDTO, role);
        baseMapper.insert(role);
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRole(SysRoleDTO roleDTO) {
        SysRole role = baseMapper.selectById(roleDTO.getId());
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }

        if (!role.getRoleCode().equals(roleDTO.getRoleCode())) {
            SysRole existRole = selectByRoleCode(roleDTO.getRoleCode());
            if (existRole != null) {
                throw new BusinessException(ResultCode.CONFLICT, "角色代码已存在");
            }
        }

        SysRole updateRole = new SysRole();
        BeanUtils.copyProperties(roleDTO, updateRole);
        return baseMapper.updateById(updateRole) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRole(Long id) {
        SysRole role = baseMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }

        rolePermissionMapper.deleteByRoleId(id);
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignPermissions(Long roleId, Long[] permissionIds) {
        SysRole role = baseMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }

        rolePermissionMapper.deleteByRoleId(roleId);

        if (permissionIds != null && permissionIds.length > 0) {
            rolePermissionMapper.batchInsert(roleId, permissionIds);
        }
        return true;
    }
}
