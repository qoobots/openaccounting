package com.qoobot.openaccounting.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.workflow.entity.WfProcessInstance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程实例Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface WfProcessInstanceMapper extends BaseMapper<WfProcessInstance> {
}
