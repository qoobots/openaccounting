package com.qoobot.openaccounting.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.common.Result;
import com.qoobot.openaccounting.system.dto.SysUserDTO;
import com.qoobot.openaccounting.system.service.SysUserService;
import com.qoobot.openaccounting.system.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理Controller
 *
 * @author openaccounting
 */
@Tag(name = "用户管理", description = "用户管理接口")
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
@Validated
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    public Result<Page<SysUserVO>> page(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId) {
        Page<SysUserVO> page = new Page<>(current, size);
        Page<SysUserVO> result = sysUserService.selectUserPage(page, username, phone, status, deptId);
        return Result.success(result);
    }

    /**
     * 根据ID查询用户详情
     */
    @Operation(summary = "根据ID查询用户详情")
    @GetMapping("/{id}")
    public Result<SysUserVO> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        SysUserVO userVO = sysUserService.selectUserById(id);
        return Result.success(userVO);
    }

    /**
     * 创建用户
     */
    @Operation(summary = "创建用户")
    @PostMapping
    public Result<Long> create(@Validated @RequestBody SysUserDTO userDTO) {
        Long userId = sysUserService.createUser(userDTO);
        return Result.success(userId);
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public Result<Boolean> update(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Validated @RequestBody SysUserDTO userDTO) {
        userDTO.setId(id);
        Boolean result = sysUserService.updateUser(userDTO);
        return Result.success(result);
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        Boolean result = sysUserService.deleteUser(id);
        return Result.success(result);
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码")
    @PutMapping("/{id}/password")
    public Result<Boolean> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        Boolean result = sysUserService.resetPassword(id, newPassword);
        return Result.success(result);
    }

    /**
     * 分配角色
     */
    @Operation(summary = "分配角色")
    @PutMapping("/{id}/roles")
    public Result<Boolean> assignRoles(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "角色ID列表") @RequestParam Long[] roleIds) {
        Boolean result = sysUserService.assignRoles(id, roleIds);
        return Result.success(result);
    }
}
