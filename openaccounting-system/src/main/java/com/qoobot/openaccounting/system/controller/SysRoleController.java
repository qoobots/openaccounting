package com.qoobot.openaccounting.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.common.Result;
import com.qoobot.openaccounting.system.dto.SysRoleDTO;
import com.qoobot.openaccounting.system.service.SysRoleService;
import com.qoobot.openaccounting.system.vo.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 角色管理控制器
 *
 * @author openaccounting
 */
@Tag(name = "角色管理", description = "角色管理接口")
@RestController
@RequestMapping("/api/system/roles")
public class SysRoleController {

    private final SysRoleService roleService;

    public SysRoleController(SysRoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "分页查询角色列表")
    @GetMapping("/page")
    public Result<Page<SysRoleVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Page<SysRoleVO> page = new Page<>(current, size);
        return Result.success(roleService.selectRolePage(page, roleName, status));
    }

    @Operation(summary = "查询角色详情")
    @GetMapping("/{id}")
    public Result<SysRoleVO> getById(@Parameter(description = "角色ID") @PathVariable Long id) {
        return Result.success(roleService.selectRoleById(id));
    }

    @Operation(summary = "创建角色")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SysRoleDTO roleDTO) {
        return Result.success(roleService.createRole(roleDTO));
    }

    @Operation(summary = "更新角色")
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody SysRoleDTO roleDTO) {
        return Result.success(roleService.updateRole(roleDTO));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@Parameter(description = "角色ID") @PathVariable Long id) {
        return Result.success(roleService.deleteRole(id));
    }

    @Operation(summary = "分配权限")
    @PostMapping("/{roleId}/permissions")
    public Result<Boolean> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody Long[] permissionIds) {
        return Result.success(roleService.assignPermissions(roleId, permissionIds));
    }
}
