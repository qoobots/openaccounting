package com.qoobot.openaccounting.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.constant.ResultCode;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.mapper.SysPermissionMapper;
import com.qoobot.openaccounting.system.service.SysPermissionService;
import com.qoobot.openaccounting.system.vo.SysPermissionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 *
 * @author openaccounting
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermissionVO> selectPermissionTree() {
        List<SysPermission> allPermissions = baseMapper.selectList(null);
        return buildTree(allPermissions, 0L);
    }

    @Override
    public List<SysPermissionVO> selectByUserId(Long userId) {
        List<SysPermission> permissions = baseMapper.selectByUserId(userId);
        return permissions.stream().map(perm -> {
            SysPermissionVO vo = new SysPermissionVO();
            BeanUtils.copyProperties(perm, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysPermissionVO> selectByRoleId(Long roleId) {
        List<SysPermission> permissions = baseMapper.selectByRoleId(roleId);
        return permissions.stream().map(perm -> {
            SysPermissionVO vo = new SysPermissionVO();
            BeanUtils.copyProperties(perm, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysPermissionVO> selectByParentId(Long parentId) {
        List<SysPermission> permissions = baseMapper.selectByParentId(parentId);
        return permissions.stream().map(perm -> {
            SysPermissionVO vo = new SysPermissionVO();
            BeanUtils.copyProperties(perm, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermission(SysPermission permission) {
        if (StringUtils.hasText(permission.getPermissionCode())) {
            SysPermission existPerm = baseMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysPermission>()
                    .eq(SysPermission::getPermissionCode, permission.getPermissionCode())
            );
            if (existPerm != null) {
                throw new BusinessException(ResultCode.CONFLICT, "权限代码已存在");
            }
        }

        if (permission.getParentId() != null && permission.getParentId() > 0) {
            SysPermission parentPerm = baseMapper.selectById(permission.getParentId());
            if (parentPerm == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "父权限不存在");
            }
        }

        baseMapper.insert(permission);
        return permission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePermission(SysPermission permission) {
        SysPermission existPerm = baseMapper.selectById(permission.getId());
        if (existPerm == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "权限不存在");
        }

        if (StringUtils.hasText(permission.getPermissionCode())
                && !permission.getPermissionCode().equals(existPerm.getPermissionCode())) {
            SysPermission codePerm = baseMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysPermission>()
                    .eq(SysPermission::getPermissionCode, permission.getPermissionCode())
                    .ne(SysPermission::getId, permission.getId())
            );
            if (codePerm != null) {
                throw new BusinessException(ResultCode.CONFLICT, "权限代码已存在");
            }
        }

        if (permission.getParentId() != null && permission.getParentId() > 0) {
            if (permission.getParentId().equals(permission.getId())) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "父权限不能是自己");
            }

            SysPermission parentPerm = baseMapper.selectById(permission.getParentId());
            if (parentPerm == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "父权限不存在");
            }
        }

        return baseMapper.updateById(permission) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePermission(Long id) {
        SysPermission permission = baseMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "权限不存在");
        }

        Long childCount = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getParentId, id)
        );
        if (childCount > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "该权限下有子权限，无法删除");
        }

        return baseMapper.deleteById(id) > 0;
    }

    private List<SysPermissionVO> buildTree(List<SysPermission> permissions, Long parentId) {
        List<SysPermissionVO> tree = new ArrayList<>();
        for (SysPermission perm : permissions) {
            if (perm.getParentId() == null || perm.getParentId().equals(parentId)) {
                SysPermissionVO vo = new SysPermissionVO();
                BeanUtils.copyProperties(perm, vo);
                vo.setChildren(buildTree(permissions, perm.getId()));
                tree.add(vo);
            }
        }
        return tree;
    }
}
