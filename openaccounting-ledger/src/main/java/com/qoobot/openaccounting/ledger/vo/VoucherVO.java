package com.qoobot.openaccounting.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证响应VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "凭证信息")
public class VoucherVO {

    @Schema(description = "凭证ID")
    private Long id;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "凭证号")
    private String voucherNo;

    @Schema(description = "会计年度")
    private Integer accountingYear;

    @Schema(description = "会计期间")
    private Integer accountingPeriod;

    @Schema(description = "凭证日期")
    private LocalDate voucherDate;

    @Schema(description = "凭证类型")
    private String voucherType;

    @Schema(description = "凭证类型名称")
    private String voucherTypeName;

    @Schema(description = "摘要")
    private String abstract;

    @Schema(description = "借方合计")
    private BigDecimal totalDebit;

    @Schema(description = "贷方合计")
    private BigDecimal totalCredit;

    @Schema(description = "分录数量")
    private Integer entryCount;

    @Schema(description = "状态: draft-草稿, submitted-已提交, audited-已审核, posted-已过账")
    private String status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "制单人ID")
    private Long makerId;

    @Schema(description = "制单人")
    private String makerName;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核人ID")
    private Long auditorId;

    @Schema(description = "审核人")
    private String auditorName;

    @Schema(description = "过账时间")
    private LocalDateTime postTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "凭证分录列表")
    private List<VoucherEntryVO> entries;
}
