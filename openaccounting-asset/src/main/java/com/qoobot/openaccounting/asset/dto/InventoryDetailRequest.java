package com.qoobot.openaccounting.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 盘点明细请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "盘点明细请求")
public class InventoryDetailRequest {

    @NotNull(message = "资产ID不能为空")
    @Schema(description = "资产ID", required = true)
    private Long assetId;

    @Schema(description = "盘点数量")
    private Integer actualQuantity;

    @Schema(description = "实际价值")
    private java.math.BigDecimal actualValue;

    @Schema(description = "备注")
    private String remark;
}
