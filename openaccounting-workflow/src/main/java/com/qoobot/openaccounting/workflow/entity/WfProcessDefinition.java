package com.qoobot.openaccounting.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qoobot.openaccounting.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程定义实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_process_definition")
@Schema(description = "流程定义")
public class WfProcessDefinition extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "流程定义ID")
    private Long id;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "流程编码")
    private String processCode;

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "流程类型(凭证审批/预算审批/资产处置)")
    private String processType;

    @Schema(description = "流程版本")
    private Integer version;

    @Schema(description = "流程定义JSON")
    private String definitionJson;

    @Schema(description = "状态(启用/停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
