package com.qoobot.openaccounting.asset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 盘点明细视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "盘点明细视图对象")
public class InventoryDetailVO {

    @Schema(description = "明细ID")
    private Long id;

    @Schema(description = "资产ID")
    private Long assetId;

    @Schema(description = "资产编码")
    private String assetCode;

    @Schema(description = "资产名称")
    private String assetName;

    @Schema(description = "资产类别")
    private String assetCategory;

    @Schema(description = "账面数量")
    private Integer bookQuantity;

    @Schema(description = "盘点数量")
    private Integer actualQuantity;

    @Schema(description = "盘盈数量")
    private Integer profitQuantity;

    @Schema(description = "盘亏数量")
    private Integer lossQuantity;

    @Schema(description = "账面价值")
    private java.math.BigDecimal bookValue;

    @Schema(description = "实际价值")
    private java.math.BigDecimal actualValue;

    @Schema(description = "盘盈金额")
    private java.math.BigDecimal profitAmount;

    @Schema(description = "盘亏金额")
    private java.math.BigDecimal lossAmount;

    @Schema(description = "盘点结果")
    private String inventoryResult;

    @Schema(description = "备注")
    private String remark;
}
