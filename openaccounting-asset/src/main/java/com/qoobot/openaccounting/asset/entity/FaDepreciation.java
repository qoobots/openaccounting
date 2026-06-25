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
 * 资产折旧记录
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fa_depreciation")
@Schema(description = "资产折旧记录")
public class FaDepreciation extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "折旧ID")
    private Long id;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "资产ID")
    private Long assetId;

    @Schema(description = "资产编码")
    private String assetCode;

    @Schema(description = "资产名称")
    private String assetName;

    @Schema(description = "折旧年度")
    private Integer depreciationYear;

    @Schema(description = "折旧期间")
    private Integer depreciationPeriod;

    @Schema(description = "折旧日期")
    private LocalDate depreciationDate;

    @Schema(description = "期初原值")
    private BigDecimal beginningValue;

    @Schema(description = "本期折旧")
    private BigDecimal currentDepreciation;

    @Schema(description = "累计折旧")
    private BigDecimal accumulatedDepreciation;

    @Schema(description = "期末净值")
    private BigDecimal endingNetValue;

    @Schema(description = "凭证ID")
    private Long voucherId;

    @Schema(description = "状态(已提/未提)")
    private String status;
}
