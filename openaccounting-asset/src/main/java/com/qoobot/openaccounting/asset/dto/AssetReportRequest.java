package com.qoobot.openaccounting.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 资产报表请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "资产报表请求")
public class AssetReportRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotNull(message = "报表年度不能为空")
    @Schema(description = "报表年度", required = true)
    private Integer reportYear;

    @Schema(description = "报表期间")
    private Integer reportPeriod;
}
