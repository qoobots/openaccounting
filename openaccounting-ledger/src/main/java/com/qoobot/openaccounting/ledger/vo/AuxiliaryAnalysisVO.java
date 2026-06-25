package com.qoobot.openaccounting.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 辅助核算分析VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "辅助核算分析结果")
public class AuxiliaryAnalysisVO {

    @Schema(description = "辅助核算ID")
    private Long auxiliaryId;

    @Schema(description = "辅助核算编码")
    private String auxiliaryCode;

    @Schema(description = "辅助核算名称")
    private String auxiliaryName;

    @Schema(description = "辅助核算类型")
    private String auxiliaryType;

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "科目编码")
    private String accountCode;

    @Schema(description = "科目名称")
    private String accountName;

    @Schema(description = "期初余额")
    private BigDecimal beginningBalance;

    @Schema(description = "本期借方")
    private BigDecimal currentDebit;

    @Schema(description = "本期贷方")
    private BigDecimal currentCredit;

    @Schema(description = "期末余额")
    private BigDecimal endingBalance;

    // 账龄分析字段
    @Schema(description = "30天内")
    private BigDecimal within30Days;

    @Schema(description = "31-60天")
    private BigDecimal days31To60;

    @Schema(description = "61-90天")
    private BigDecimal days61To90;

    @Schema(description = "91-180天")
    private BigDecimal days91To180;

    @Schema(description = "181-360天")
    private BigDecimal days181To360;

    @Schema(description = "360天以上")
    private BigDecimal over360Days;
}
