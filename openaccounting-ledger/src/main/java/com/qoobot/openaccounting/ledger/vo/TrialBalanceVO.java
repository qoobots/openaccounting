package com.qoobot.openaccounting.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 试算平衡VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "试算平衡结果")
public class TrialBalanceVO {

    @Schema(description = "是否平衡")
    private Boolean balanced;

    @Schema(description = "借方合计")
    private BigDecimal totalDebit;

    @Schema(description = "贷方合计")
    private BigDecimal totalCredit;

    @Schema(description = "差额")
    private BigDecimal difference;
}
