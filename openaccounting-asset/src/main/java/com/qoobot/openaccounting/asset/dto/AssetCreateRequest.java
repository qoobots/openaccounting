package com.qoobot.openaccounting.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 资产创建请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "资产创建请求")
public class AssetCreateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotBlank(message = "资产编码不能为空")
    @Schema(description = "资产编码", required = true)
    private String assetCode;

    @NotBlank(message = "资产名称不能为空")
    @Schema(description = "资产名称", required = true)
    private String assetName;

    @Schema(description = "资产类别")
    private String assetCategory;

    @Schema(description = "资产规格")
    private String specification;

    @Schema(description = "资产型号")
    private String model;

    @Schema(description = "使用部门ID")
    private Long departmentId;

    @Schema(description = "责任人ID")
    private Long responsiblePersonId;

    @Schema(description = "存放地点")
    private String location;

    @NotNull(message = "资产原值不能为空")
    @Schema(description = "资产原值", required = true)
    private BigDecimal originalValue;

    @NotNull(message = "购置日期不能为空")
    @Schema(description = "购置日期", required = true)
    private LocalDate purchaseDate;

    @Schema(description = "启用日期")
    private LocalDate startDate;

    @Schema(description = "折旧方法")
    private String depreciationMethod;

    @Schema(description = "折旧年限")
    private Integer depreciationYears;

    @Schema(description = "残值率(%)")
    private BigDecimal salvageRate;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "发票号")
    private String invoiceNumber;

    @Schema(description = "备注")
    private String remark;
}
