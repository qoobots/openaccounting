package com.qoobot.openaccounting.system.controller;

import com.qoobot.openaccounting.common.Result;
import com.qoobot.openaccounting.system.entity.SysPermission;
import com.qoobot.openaccounting.system.service.SysPermissionService;
import com.qoobot.openaccounting.system.vo.SysPermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 *
 * @author openaccounting
 */
@Tag(name = "权限管理", description = "权限管理接口")
@RestController
@RequestMapping("/api/system/permissions")
public class SysPermissionController {

    private final SysPermissionService permissionService;

    public SysPermissionController(SysPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "查询权限树")
    @GetMapping("/tree")
    public Result<List<SysPermissionVO>> tree() {
        return Result.success(permissionService.selectPermissionTree());
    }

    @Operation(summary = "根据用户ID查询用户权限")
    @GetMapping("/user/{userId}")
    public Result<List<SysPermissionVO>> byUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(permissionService.selectByUserId(userId));
    }

    @Operation(summary = "根据角色ID查询角色权限")
    @GetMapping("/role/{roleId}")
    public Result<List<SysPermissionVO>> byRole(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        return Result.success(permissionService.selectByRoleId(roleId));
    }

    @Operation(summary = "根据父权限ID查询子权限")
    @GetMapping("/children/{parentId}")
    public Result<List<SysPermissionVO>> children(@Parameter(description = "父权限ID") @PathVariable Long parentId) {
        return Result.success(permissionService.selectByParentId(parentId));
    }

    @Operation(summary = "创建权限")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SysPermission permission) {
        return Result.success(permissionService.createPermission(permission));
    }

    @Operation(summary = "更新权限")
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody SysPermission permission) {
        return Result.success(permissionService.updatePermission(permission));
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@Parameter(description = "权限ID") @PathVariable Long id) {
        return Result.success(permissionService.deletePermission(id));
    }
}
