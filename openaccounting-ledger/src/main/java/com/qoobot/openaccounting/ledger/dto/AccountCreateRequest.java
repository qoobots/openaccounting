package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建科目请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "创建科目请求")
public class AccountCreateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotBlank(message = "科目编码不能为空")
    @Pattern(regexp = "^\\d{4}$|^\\d{4}-\\d{2}$|^\\d{4}-\\d{2}-\\d{2}$|^\\d{4}-\\d{2}-\\d{2}-\\d{2}$", message = "科目编码格式不正确")
    @Schema(description = "科目编码(4-2-2-2结构)", required = true, example = "1001")
    private String accountCode;

    @NotBlank(message = "科目名称不能为空")
    @Size(max = 100, message = "科目名称长度不能超过100")
    @Schema(description = "科目名称", required = true, example = "库存现金")
    private String accountName;

    @Schema(description = "上级科目ID", example = "0")
    private Long parentId;

    @NotBlank(message = "科目类型不能为空")
    @Pattern(regexp = "assets|liabilities|equity|revenue|expense", message = "科目类型不正确")
    @Schema(description = "科目类型: assets-资产, liabilities-负债, equity-所有者权益, revenue-收入, expense-费用", required = true)
    private String accountType;

    @NotBlank(message = "余额方向不能为空")
    @Pattern(regexp = "debit|credit", message = "余额方向不正确")
    @Schema(description = "余额方向: debit-借方, credit-贷方", required = true)
    private String balanceDirection;

    @Schema(description = "是否部门核算: 0-否, 1-是")
    private Integer auxiliaryDept = 0;

    @Schema(description = "是否项目核算: 0-否, 1-是")
    private Integer auxiliaryProject = 0;

    @Schema(description = "是否客户核算: 0-否, 1-是")
    private Integer auxiliaryCustomer = 0;

    @Schema(description = "是否供应商核算: 0-否, 1-是")
    private Integer auxiliarySupplier = 0;

    @Schema(description = "是否员工核算: 0-否, 1-是")
    private Integer auxiliaryEmployee = 0;
}
