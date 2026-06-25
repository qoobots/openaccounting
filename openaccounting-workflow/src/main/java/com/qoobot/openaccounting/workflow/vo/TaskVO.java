package com.qoobot.openaccounting.workflow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务视图对象")
public class TaskVO {

    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "任务编号")
    private String taskNo;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "任务节点")
    private String taskNode;

    @Schema(description = "处理人")
    private String assigneeName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "处理时间")
    private LocalDateTime processTime;

    @Schema(description = "任务状态")
    private String status;

    @Schema(description = "处理意见")
    private String comment;

    @Schema(description = "处理结果")
    private String result;
}
