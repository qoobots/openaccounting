package com.qoobot.openaccounting.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.workflow.entity.WfTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface WfTaskMapper extends BaseMapper<WfTask> {
}
