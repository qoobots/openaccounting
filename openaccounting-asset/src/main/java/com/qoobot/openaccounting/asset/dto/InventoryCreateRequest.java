package com.qoobot.openaccounting.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 盘点创建请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "盘点创建请求")
public class InventoryCreateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotNull(message = "盘点年度不能为空")
    @Schema(description = "盘点年度", required = true)
    private Integer inventoryYear;

    @NotNull(message = "盘点期间不能为空")
    @Schema(description = "盘点期间", required = true)
    private Integer inventoryPeriod;

    @NotNull(message = "盘点日期不能为空")
    @Schema(description = "盘点日期", required = true)
    private LocalDate inventoryDate;

    @Schema(description = "备注")
    private String remark;
}
