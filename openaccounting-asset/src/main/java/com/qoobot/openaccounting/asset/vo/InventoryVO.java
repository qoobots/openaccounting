package com.qoobot.openaccounting.asset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 盘点视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "盘点视图对象")
public class InventoryVO {

    @Schema(description = "盘点ID")
    private Long id;

    @Schema(description = "盘点单号")
    private String inventoryNo;

    @Schema(description = "盘点年度")
    private Integer inventoryYear;

    @Schema(description = "盘点期间")
    private Integer inventoryPeriod;

    @Schema(description = "盘点日期")
    private LocalDate inventoryDate;

    @Schema(description = "盘点人")
    private String inventoryUserName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "盘点状态")
    private String status;

    @Schema(description = "资产总数")
    private Integer totalAssets;

    @Schema(description = "正常数量")
    private Integer normalCount;

    @Schema(description = "盘盈数量")
    private Integer profitCount;

    @Schema(description = "盘亏数量")
    private Integer lossCount;

    @Schema(description = "盘盈金额")
    private java.math.BigDecimal profitAmount;

    @Schema(description = "盘亏金额")
    private java.math.BigDecimal lossAmount;

    @Schema(description = "盘点明细")
    private List<InventoryDetailVO> details;
}
