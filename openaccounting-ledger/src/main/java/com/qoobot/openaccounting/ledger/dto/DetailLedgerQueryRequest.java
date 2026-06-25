package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 明细账查询请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "明细账查询请求")
public class DetailLedgerQueryRequest {

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

    @Schema(description = "科目ID(必填)")
    private Long accountId;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "供应商ID")
    private Long supplierId;

    @Schema(description = "员工ID")
    private Long employeeId;
}
