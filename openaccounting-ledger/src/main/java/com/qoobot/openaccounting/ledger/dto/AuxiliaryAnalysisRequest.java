package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 辅助核算分析请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "辅助核算分析请求")
public class AuxiliaryAnalysisRequest {

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "会计年度")
    private Integer accountingYear;

    @Schema(description = "会计期间")
    private Integer accountingPeriod;

    @Schema(description = "辅助核算类型: dept-部门, project-项目, customer-客户, supplier-供应商, employee-员工")
    private String auxiliaryType;

    @Schema(description = "辅助核算ID")
    private Long auxiliaryId;

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "分析类型: age-账龄分析, balance-余额分析, detail-明细分析")
    private String analysisType = "balance";
}
