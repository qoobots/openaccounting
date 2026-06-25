package com.qoobot.openaccounting.asset.controller;

import com.qoobot.openaccounting.asset.dto.InventoryCreateRequest;
import com.qoobot.openaccounting.asset.dto.InventoryDetailRequest;
import com.qoobot.openaccounting.asset.service.FaInventoryService;
import com.qoobot.openaccounting.asset.vo.InventoryVO;
import com.qoobot.openaccounting.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 盘点Controller
 *
 * @author openaccounting
 */
@Tag(name = "资产盘点", description = "资产盘点相关接口")
@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final FaInventoryService inventoryService;

    @Operation(summary = "创建盘点单")
    @PostMapping
    public Result<Long> createInventory(
            @Valid @RequestBody InventoryCreateRequest request,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        Long inventoryId = inventoryService.createInventory(request, userId, username);
        return Result.success(inventoryId);
    }

    @Operation(summary = "开始盘点")
    @PostMapping("/{id}/start")
    public Result<Void> startInventory(
            @Parameter(description = "盘点ID") @PathVariable Long id,
            @RequestBody List<InventoryDetailRequest> detailRequests) {
        inventoryService.startInventory(id, detailRequests);
        return Result.success();
    }

    @Operation(summary = "完成盘点")
    @PostMapping("/{id}/complete")
    public Result<Void> completeInventory(@Parameter(description = "盘点ID") @PathVariable Long id) {
        inventoryService.completeInventory(id);
        return Result.success();
    }

    @Operation(summary = "查询盘点详情")
    @GetMapping("/{id}")
    public Result<InventoryVO> getInventoryDetail(@Parameter(description = "盘点ID") @PathVariable Long id) {
        InventoryVO result = inventoryService.getInventoryDetail(id);
        return Result.success(result);
    }
}
