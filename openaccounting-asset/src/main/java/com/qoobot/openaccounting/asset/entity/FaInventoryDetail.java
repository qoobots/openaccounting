package com.qoobot.openaccounting.asset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qoobot.openaccounting.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 资产盘点明细
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fa_inventory_detail")
@Schema(description = "资产盘点明细")
public class FaInventoryDetail extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "明细ID")
    private Long id;

    @Schema(description = "盘点ID")
    private Long inventoryId;

    @Schema(description = "资产ID")
    private Long assetId;

    @Schema(description = "资产编码")
    private String assetCode;

    @Schema(description = "资产名称")
    private String assetName;

    @Schema(description = "账面数量")
    private Integer bookQuantity;

    @Schema(description = "盘点数量")
    private Integer actualQuantity;

    @Schema(description = "盘盈数量")
    private Integer profitQuantity;

    @Schema(description = "盘亏数量")
    private Integer lossQuantity;

    @Schema(description = "账面价值")
    private BigDecimal bookValue;

    @Schema(description = "实际价值")
    private BigDecimal actualValue;

    @Schema(description = "盘盈金额")
    private BigDecimal profitAmount;

    @Schema(description = "盘亏金额")
    private BigDecimal lossAmount;

    @Schema(description = "盘点结果(正常/盘盈/盘亏)")
    private String inventoryResult;

    @Schema(description = "备注")
    private String remark;
}
