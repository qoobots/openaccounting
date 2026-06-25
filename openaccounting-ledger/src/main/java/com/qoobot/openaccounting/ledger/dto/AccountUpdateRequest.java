package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新科目请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "更新科目请求")
public class AccountUpdateRequest {

    @NotBlank(message = "科目名称不能为空")
    @Size(max = 100, message = "科目名称长度不能超过100")
    @Schema(description = "科目名称", required = true)
    private String accountName;

    @Schema(description = "余额方向: debit-借方, credit-贷方")
    private String balanceDirection;

    @Schema(description = "是否部门核算: 0-否, 1-是")
    private Integer auxiliaryDept;

    @Schema(description = "是否项目核算: 0-否, 1-是")
    private Integer auxiliaryProject;

    @Schema(description = "是否客户核算: 0-否, 1-是")
    private Integer auxiliaryCustomer;

    @Schema(description = "是否供应商核算: 0-否, 1-是")
    private Integer auxiliarySupplier;

    @Schema(description = "是否员工核算: 0-否, 1-是")
    private Integer auxiliaryEmployee;

    @Schema(description = "状态: 0-启用, 1-停用")
    private Integer status;
}
