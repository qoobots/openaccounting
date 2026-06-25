package com.qoobot.openaccounting.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 总账响应VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "总账信息")
public class GeneralLedgerVO {

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "科目编码")
    private String accountCode;

    @Schema(description = "科目名称")
    private String accountName;

    @Schema(description = "科目类型")
    private String accountType;

    @Schema(description = "科目类型名称")
    private String accountTypeName;

    @Schema(description = "余额方向")
    private String balanceDirection;

    @Schema(description = "期初借方")
    private BigDecimal beginningDebit;

    @Schema(description = "期初贷方")
    private BigDecimal beginningCredit;

    @Schema(description = "期初余额")
    private BigDecimal beginningBalance;

    @Schema(description = "本期借方")
    private BigDecimal currentDebit;

    @Schema(description = "本期贷方")
    private BigDecimal currentCredit;

    @Schema(description = "本期发生额")
    private BigDecimal currentBalance;

    @Schema(description = "期末借方")
    private BigDecimal endingDebit;

    @Schema(description = "期末贷方")
    private BigDecimal endingCredit;

    @Schema(description = "期末余额")
    private BigDecimal endingBalance;
}
