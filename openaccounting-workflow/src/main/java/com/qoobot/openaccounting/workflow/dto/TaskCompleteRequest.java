package com.qoobot.openaccounting.workflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 任务完成请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "任务完成请求")
public class TaskCompleteRequest {

    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID", required = true)
    private Long taskId;

    @NotBlank(message = "处理结果不能为空")
    @Schema(description = "处理结果(通过/拒绝)", required = true)
    private String result;

    @Schema(description = "处理意见")
    private String comment;
}
