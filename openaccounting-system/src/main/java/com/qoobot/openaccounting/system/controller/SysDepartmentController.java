package com.qoobot.openaccounting.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.common.Result;
import com.qoobot.openaccounting.system.entity.SysDepartment;
import com.qoobot.openaccounting.system.service.SysDepartmentService;
import com.qoobot.openaccounting.system.vo.SysDepartmentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 *
 * @author openaccounting
 */
@Tag(name = "部门管理", description = "部门管理接口")
@RestController
@RequestMapping("/api/system/departments")
public class SysDepartmentController {

    private final SysDepartmentService departmentService;

    public SysDepartmentController(SysDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "分页查询部门列表")
    @GetMapping("/page")
    public Result<Page<SysDepartmentVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "部门名称") @RequestParam(required = false) String departmentName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Page<SysDepartmentVO> page = new Page<>(current, size);
        return Result.success(departmentService.selectDepartmentPage(page, departmentName, status));
    }

    @Operation(summary = "查询部门树")
    @GetMapping("/tree")
    public Result<List<SysDepartmentVO>> tree() {
        return Result.success(departmentService.selectDepartmentTree());
    }

    @Operation(summary = "根据父部门ID查询子部门")
    @GetMapping("/children/{parentId}")
    public Result<List<SysDepartmentVO>> children(@Parameter(description = "父部门ID") @PathVariable Long parentId) {
        return Result.success(departmentService.selectByParentId(parentId));
    }

    @Operation(summary = "创建部门")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SysDepartment department) {
        return Result.success(departmentService.createDepartment(department));
    }

    @Operation(summary = "更新部门")
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody SysDepartment department) {
        return Result.success(departmentService.updateDepartment(department));
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@Parameter(description = "部门ID") @PathVariable Long id) {
        return Result.success(departmentService.deleteDepartment(id));
    }
}
