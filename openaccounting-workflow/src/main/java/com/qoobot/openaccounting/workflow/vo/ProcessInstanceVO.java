package com.qoobot.openaccounting.workflow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程实例视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流程实例视图对象")
public class ProcessInstanceVO {

    @Schema(description = "流程实例ID")
    private Long id;

    @Schema(description = "流程实例编号")
    private String instanceNo;

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "业务单据类型")
    private String businessType;

    @Schema(description = "业务单据ID")
    private Long businessId;

    @Schema(description = "发起人")
    private String initiatorName;

    @Schema(description = "发起时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime endTime;

    @Schema(description = "当前节点")
    private String currentNode;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "任务列表")
    private List<TaskVO> tasks;
}
