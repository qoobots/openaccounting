package com.qoobot.openaccounting.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建预算请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "创建预算请求")
public class BudgetCreateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @Schema(description = "预算编码")
    private String budgetCode;

    @NotNull(message = "预算名称不能为空")
    @Schema(description = "预算名称", required = true)
    private String budgetName;

    @NotNull(message = "预算年度不能为空")
    @Schema(description = "预算年度", required = true)
    private Integer budgetYear;

    @NotNull(message = "预算类型不能为空")
    @Schema(description = "预算类型: department-部门, project-项目, company-公司", required = true)
    private String budgetType;

    @Schema(description = "预算对象ID(部门/项目ID)")
    private Long targetId;

    @Schema(description = "预算对象名称")
    private String targetName;

    @NotNull(message = "预算金额不能为空")
    @Schema(description = "预算金额", required = true)
    private BigDecimal budgetAmount;

    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期", required = true)
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    @Schema(description = "结束日期", required = true)
    private LocalDate endDate;

    @NotEmpty(message = "预算明细不能为空")
    @Valid
    @Schema(description = "预算明细列表", required = true)
    private List<BudgetDetailRequest> details;
}
