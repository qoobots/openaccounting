package com.qoobot.openaccounting.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qoobot.openaccounting.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_task")
@Schema(description = "任务")
public class WfTask extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "流程实例ID")
    private Long instanceId;

    @Schema(description = "任务编号")
    private String taskNo;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "任务节点")
    private String taskNode;

    @Schema(description = "处理人ID")
    private Long assigneeId;

    @Schema(description = "处理人")
    private String assigneeName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "处理时间")
    private LocalDateTime processTime;

    @Schema(description = "任务状态(待处理/已处理/已转交)")
    private String status;

    @Schema(description = "处理意见")
    private String comment;

    @Schema(description = "处理结果(通过/拒绝)")
    private String result;
}
