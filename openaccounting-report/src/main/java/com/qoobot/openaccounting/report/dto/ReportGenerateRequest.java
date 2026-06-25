package com.qoobot.openaccounting.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 报表生成请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "报表生成请求")
public class ReportGenerateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotNull(message = "会计年度不能为空")
    @Schema(description = "会计年度", required = true)
    private Integer accountingYear;

    @NotNull(message = "会计期间不能为空")
    @Schema(description = "会计期间", required = true)
    private Integer accountingPeriod;

    @Schema(description = "报表类型: balance_sheet-资产负债表, income_statement-利润表, cash_flow-现金流量表")
    private String reportType;

    @Schema(description = "报表日期")
    private LocalDate reportDate;
}
