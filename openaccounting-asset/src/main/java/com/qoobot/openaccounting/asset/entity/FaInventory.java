package com.qoobot.openaccounting.asset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qoobot.openaccounting.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 资产盘点记录
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fa_inventory")
@Schema(description = "资产盘点记录")
public class FaInventory extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "盘点ID")
    private Long id;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "盘点单号")
    private String inventoryNo;

    @Schema(description = "盘点年度")
    private Integer inventoryYear;

    @Schema(description = "盘点期间")
    private Integer inventoryPeriod;

    @Schema(description = "盘点日期")
    private LocalDate inventoryDate;

    @Schema(description = "盘点人ID")
    private Long inventoryUserId;

    @Schema(description = "盘点人")
    private String inventoryUserName;

    @Schema(description = "备注")
    private String remark;
}
