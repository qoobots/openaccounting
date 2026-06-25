package com.qoobot.openaccounting.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.common.Result;
import com.qoobot.openaccounting.system.entity.SysCompany;
import com.qoobot.openaccounting.system.service.SysCompanyService;
import com.qoobot.openaccounting.system.vo.SysCompanyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公司管理控制器
 *
 * @author openaccounting
 */
@Tag(name = "公司管理", description = "公司管理接口")
@RestController
@RequestMapping("/api/system/companies")
public class SysCompanyController {

    private final SysCompanyService companyService;

    public SysCompanyController(SysCompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "分页查询公司列表")
    @GetMapping("/page")
    public Result<Page<SysCompanyVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "公司名称") @RequestParam(required = false) String companyName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Page<SysCompanyVO> page = new Page<>(current, size);
        return Result.success(companyService.selectCompanyPage(page, companyName, status));
    }

    @Operation(summary = "查询公司树")
    @GetMapping("/tree")
    public Result<List<SysCompanyVO>> tree() {
        return Result.success(companyService.selectCompanyTree());
    }

    @Operation(summary = "根据父公司ID查询子公司")
    @GetMapping("/children/{parentId}")
    public Result<List<SysCompanyVO>> children(@Parameter(description = "父公司ID") @PathVariable Long parentId) {
        return Result.success(companyService.selectByParentId(parentId));
    }

    @Operation(summary = "创建公司")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SysCompany company) {
        return Result.success(companyService.createCompany(company));
    }

    @Operation(summary = "更新公司")
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody SysCompany company) {
        return Result.success(companyService.updateCompany(company));
    }

    @Operation(summary = "删除公司")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@Parameter(description = "公司ID") @PathVariable Long id) {
        return Result.success(companyService.deleteCompany(id));
    }
}
