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
 * 流程实例实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_process_instance")
@Schema(description = "流程实例")
public class WfProcessInstance extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "流程实例ID")
    private Long id;

    @Schema(description = "流程定义ID")
    private Long definitionId;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "业务单据类型")
    private String businessType;

    @Schema(description = "业务单据ID")
    private Long businessId;

    @Schema(description = "流程实例编号")
    private String instanceNo;

    @Schema(description = "发起人ID")
    private Long initiatorId;

    @Schema(description = "发起人")
    private String initiatorName;

    @Schema(description = "发起时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime endTime;

    @Schema(description = "当前节点")
    private String currentNode;

    @Schema(description = "状态(进行中/已完成/已拒绝/已撤回)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
