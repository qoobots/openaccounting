package com.qoobot.openaccounting.workflow.service;

import com.qoobot.openaccounting.workflow.dto.ProcessStartRequest;
import com.qoobot.openaccounting.workflow.dto.TaskCompleteRequest;
import com.qoobot.openaccounting.workflow.vo.ProcessInstanceVO;
import com.qoobot.openaccounting.workflow.vo.TaskVO;

import java.util.List;
import java.util.Map;

/**
 * Flowable服务接口
 *
 * @author openaccounting
 */
public interface FlowableService {

    /**
     * 部署流程定义
     *
     * @param processName 流程名称
     * @param bpmnContent BPMN内容
     * @return 部署ID
     */
    String deployProcess(String processName, String bpmnContent);

    /**
     * 启动流程实例
     *
     * @param processKey 流程Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    String startProcessInstance(String processKey, String businessKey, Map<String, Object> variables);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param variables 流程变量
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * 查询我的待办任务
     *
     * @param userId 用户ID
     * @return 待办任务列表
     */
    List<TaskVO> getMyTasks(String userId);

    /**
     * 查询流程实例详情
     *
     * @param instanceId 流程实例ID
     * @return 流程实例详情
     */
    ProcessInstanceVO getProcessInstanceDetail(String instanceId);

    /**
     * 获取流程图
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程图图片
     */
    byte[] getProcessDiagram(String processDefinitionId);
}
