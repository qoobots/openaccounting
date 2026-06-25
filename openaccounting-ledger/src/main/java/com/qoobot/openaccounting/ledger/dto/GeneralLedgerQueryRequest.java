package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 总账查询请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "总账查询请求")
public class GeneralLedgerQueryRequest {

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "会计年度")
    private Integer accountingYear;

    @Schema(description = "会计期间")
    private Integer accountingPeriod;

    @Schema(description = "开始期间")
    private Integer startPeriod;

    @Schema(description = "结束期间")
    private Integer endPeriod;

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "科目编码(模糊查询)")
    private String accountCode;

    @Schema(description = "科目名称(模糊查询)")
    private String accountName;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "是否只显示有余额的科目")
    private Boolean onlyWithBalance = false;
}
