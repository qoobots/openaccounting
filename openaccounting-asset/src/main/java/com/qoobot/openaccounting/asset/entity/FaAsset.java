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
 * 固定资产实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fa_asset")
@Schema(description = "固定资产")
public class FaAsset extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "资产ID")
    private Long id;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "资产编码")
    private String assetCode;

    @Schema(description = "资产名称")
    private String assetName;

    @Schema(description = "资产类别")
    private String assetCategory;

    @Schema(description = "资产规格")
    private String specification;

    @Schema(description = "资产型号")
    private String model;

    @Schema(description = "使用部门ID")
    private Long departmentId;

    @Schema(description = "使用部门")
    private String departmentName;

    @Schema(description = "责任人ID")
    private Long responsiblePersonId;

    @Schema(description = "责任人")
    private String responsiblePersonName;

    @Schema(description = "存放地点")
    private String location;

    @Schema(description = "资产原值")
    private BigDecimal originalValue;

    @Schema(description = "购置日期")
    private LocalDate purchaseDate;

    @Schema(description = "启用日期")
    private LocalDate startDate;

    @Schema(description = "折旧方法(直线法/双倍余额递减法/年数总和法)")
    private String depreciationMethod;

    @Schema(description = "折旧年限")
    private Integer depreciationYears;

    @Schema(description = "残值率(%)")
    private BigDecimal salvageRate;

    @Schema(description = "残值")
    private BigDecimal salvageValue;

    @Schema(description = "累计折旧")
    private BigDecimal accumulatedDepreciation;

    @Schema(description = "净值")
    private BigDecimal netValue;

    @Schema(description = "资产状态(在用/闲置/维修中/报废)")
    private String assetStatus;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "发票号")
    private String invoiceNumber;

    @Schema(description = "备注")
    private String remark;
}
