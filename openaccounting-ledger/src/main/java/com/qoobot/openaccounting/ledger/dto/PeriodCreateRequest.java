package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建会计期间请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "创建会计期间请求")
public class PeriodCreateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotNull(message = "会计年度不能为空")
    @Schema(description = "会计年度", required = true, example = "2024")
    private Integer accountingYear;

    @NotNull(message = "会计期间不能为空")
    @Schema(description = "会计期间(1-12)", required = true, example = "1")
    private Integer accountingPeriod;

    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期", required = true)
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    @Schema(description = "结束日期", required = true)
    private LocalDate endDate;
}
