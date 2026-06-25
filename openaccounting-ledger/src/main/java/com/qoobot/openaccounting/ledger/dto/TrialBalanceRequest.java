package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 试算平衡请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "试算平衡请求")
public class TrialBalanceRequest {

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "会计年度")
    private Integer accountingYear;

    @Schema(description = "会计期间")
    private Integer accountingPeriod;
}
