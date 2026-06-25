package com.qoobot.openaccounting.workflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 流程启动请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "流程启动请求")
public class ProcessStartRequest {

    @NotNull(message = "流程定义ID不能为空")
    @Schema(description = "流程定义ID", required = true)
    private Long definitionId;

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotBlank(message = "业务单据类型不能为空")
    @Schema(description = "业务单据类型", required = true)
    private String businessType;

    @NotNull(message = "业务单据ID不能为空")
    @Schema(description = "业务单据ID", required = true)
    private Long businessId;

    @Schema(description = "备注")
    private String remark;
}
