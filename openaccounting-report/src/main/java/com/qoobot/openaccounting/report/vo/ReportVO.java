package com.qoobot.openaccounting.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 报表VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "报表信息")
public class ReportVO {

    @Schema(description = "报表类型")
    private String reportType;

    @Schema(description = "报表类型名称")
    private String reportTypeName;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "会计年度")
    private Integer accountingYear;

    @Schema(description = "会计期间")
    private Integer accountingPeriod;

    @Schema(description = "报表日期")
    private LocalDate reportDate;

    @Schema(description = "报表项目列表")
    private List<ReportItemVO> items;

    @Data
    @Schema(description = "报表项目")
    public static class ReportItemVO {

        @Schema(description = "项目编码")
        private String itemCode;

        @Schema(description = "项目名称")
        private String itemName;

        @Schema(description = "项目层级")
        private Integer level;

        @Schema(description = "上级项目编码")
        private String parentCode;

        @Schema(description = "行次")
        private Integer lineNo;

        @Schema(description = "期初余额")
        private BigDecimal beginningBalance;

        @Schema(description = "期末余额")
        private BigDecimal endingBalance;

        @Schema(description = "本期发生额")
        private BigDecimal currentAmount;

        @Schema(description = "是否合计行")
        private Boolean isSummary;
    }
}
