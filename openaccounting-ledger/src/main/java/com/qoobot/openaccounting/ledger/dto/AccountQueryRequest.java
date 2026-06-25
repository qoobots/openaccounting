package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 科目查询请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "科目查询请求")
public class AccountQueryRequest {

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "科目编码(模糊查询)")
    private String accountCode;

    @Schema(description = "科目名称(模糊查询)")
    private String accountName;

    @Schema(description = "科目类型: assets-资产, liabilities-负债, equity-所有者权益, revenue-收入, expense-费用")
    private String accountType;

    @Schema(description = "上级科目ID")
    private Long parentId;

    @Schema(description = "层级: 1-一级, 2-二级, 3-三级, 4-四级")
    private Integer level;

    @Schema(description = "余额方向: debit-借方, credit-贷方")
    private String balanceDirection;

    @Schema(description = "状态: 0-启用, 1-停用")
    private Integer status;

    @Schema(description = "是否启用辅助核算")
    private Boolean hasAuxiliary;
}
