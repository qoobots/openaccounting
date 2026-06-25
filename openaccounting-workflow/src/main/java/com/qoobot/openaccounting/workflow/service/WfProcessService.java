package com.qoobot.openaccounting.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.workflow.dto.ProcessStartRequest;
import com.qoobot.openaccounting.workflow.dto.TaskCompleteRequest;
import com.qoobot.openaccounting.workflow.entity.WfProcessInstance;
import com.qoobot.openaccounting.workflow.entity.WfTask;
import com.qoobot.openaccounting.workflow.vo.ProcessInstanceVO;
import com.qoobot.openaccounting.workflow.vo.TaskVO;

import java.util.List;

/**
 * 流程Service接口
 *
 * @author openaccounting
 */
public interface WfProcessService extends IService<WfProcessInstance> {

    /**
     * 启动流程
     *
     * @param request 启动请求
     * @param userId 发起人ID
     * @param username 发起人
     * @return 流程实例ID
     */
    Long startProcess(ProcessStartRequest request, Long userId, String username);

    /**
     * 完成任务
     *
     * @param request 任务完成请求
     * @param userId 处理人ID
     * @param username 处理人
     */
    void completeTask(TaskCompleteRequest request, Long userId, String username);

    /**
     * 查询我的待办任务
     *
     * @param userId 用户ID
     * @return 待办任务列表
     */
    List<TaskVO> getMyTasks(Long userId);

    /**
     * 查询流程实例详情
     *
     * @param instanceId 流程实例ID
     * @return 流程实例详情
     */
    ProcessInstanceVO getInstanceDetail(Long instanceId);
}
