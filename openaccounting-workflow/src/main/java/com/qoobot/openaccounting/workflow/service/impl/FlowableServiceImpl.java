package com.qoobot.openaccounting.workflow.service.impl;

import com.qoobot.openaccounting.workflow.service.FlowableService;
import com.qoobot.openaccounting.workflow.vo.ProcessInstanceVO;
import com.qoobot.openaccounting.workflow.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Flowable服务实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class FlowableServiceImpl implements FlowableService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final ProcessDiagramGenerator processDiagramGenerator;
    private final ProcessEngine processEngine;

    @Override
    public String deployProcess(String processName, String bpmnContent) {
        Deployment deployment = repositoryService.createDeployment()
                .name(processName)
                .addString(processName + ".bpmn20.xml", bpmnContent)
                .deploy();

        return deployment.getId();
    }

    @Override
    public String startProcessInstance(String processKey, String businessKey, Map<String, Object> variables) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        return instance.getId();
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @Override
    public List<TaskVO> getMyTasks(String userId) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        return tasks.stream()
                .map(this::convertToTaskVO)
                .collect(Collectors.toList());
    }

    @Override
    public ProcessInstanceVO getProcessInstanceDetail(String instanceId) {
        // 查询流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();

        if (instance == null) {
            // 查询历史流程实例
            HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceId)
                    .singleResult();

            if (historicInstance == null) {
                throw new RuntimeException("流程实例不存在");
            }

            return convertToProcessInstanceVO(historicInstance);
        }

        return convertToProcessInstanceVO(instance);
    }

    @Override
    public byte[] getProcessDiagram(String processDefinitionId) {
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        // 获取BPMN模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        // 查询已完成的节点
        List<String> highLightedActivities = new ArrayList<>();
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId(processDefinitionId)
                .finished()
                .list();

        for (HistoricActivityInstance hai : historicActivityInstances) {
            highLightedActivities.add(hai.getActivityId());
        }

        // 生成流程图
        InputStream inputStream = processDiagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                highLightedActivities,
                new ArrayList<>(),
                processEngine.getProcessEngineConfiguration().getActivityFontName(),
                processEngine.getProcessEngineConfiguration().getLabelFontName(),
                processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                processEngine.getProcessEngineConfiguration().getClassLoader(),
                1.0
        );

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成流程图失败", e);
        }
    }

    /**
     * 转换为任务VO
     */
    private TaskVO convertToTaskVO(Task task) {
        return TaskVO.builder()
                .id(Long.valueOf(task.getId()))
                .taskNo(task.getId())
                .taskName(task.getName())
                .taskNode(task.getTaskDefinitionKey())
                .assigneeName(task.getAssignee())
                .createTime(task.getCreateTime())
                .status("待处理")
                .build();
    }

    /**
     * 转换为流程实例VO
     */
    private ProcessInstanceVO convertToProcessInstanceVO(ProcessInstance instance) {
        // 查询流程定义
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId())
                .singleResult();

        // 查询任务列表
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(instance.getId())
                .list();

        List<TaskVO> taskVOs = tasks.stream()
                .map(this::convertToTaskVO)
                .collect(Collectors.toList());

        return ProcessInstanceVO.builder()
                .id(Long.valueOf(instance.getId()))
                .instanceNo(instance.getId())
                .processName(definition != null ? definition.getName() : "")
                .businessType(instance.getBusinessKey())
                .businessId(null)
                .initiatorName(instance.getStartUserId())
                .startTime(instance.getStartTime())
                .endTime(null)
                .currentNode(taskVOs.isEmpty() ? "已完成" : taskVOs.get(0).getTaskName())
                .status("进行中")
                .tasks(taskVOs)
                .build();
    }

    /**
     * 转换为流程实例VO（历史）
     */
    private ProcessInstanceVO convertToProcessInstanceVO(HistoricProcessInstance instance) {
        // 查询流程定义
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId())
                .singleResult();

        // 查询历史任务列表
        List<org.flowable.task.api.history.HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(instance.getId())
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        List<TaskVO> taskVOs = tasks.stream()
                .map(this::convertToTaskVO)
                .collect(Collectors.toList());

        return ProcessInstanceVO.builder()
                .id(Long.valueOf(instance.getId()))
                .instanceNo(instance.getId())
                .processName(definition != null ? definition.getName() : "")
                .businessType(instance.getBusinessKey())
                .businessId(null)
                .initiatorName(instance.getStartUserId())
                .startTime(instance.getStartTime())
                .endTime(instance.getEndTime())
                .currentNode("已完成")
                .status("已完成")
                .tasks(taskVOs)
                .build();
    }

    /**
     * 转换为任务VO（历史）
     */
    private TaskVO convertToTaskVO(org.flowable.task.api.history.HistoricTaskInstance task) {
        return TaskVO.builder()
                .id(Long.valueOf(task.getId()))
                .taskNo(task.getId())
                .taskName(task.getName())
                .taskNode(task.getTaskDefinitionKey())
                .assigneeName(task.getAssignee())
                .createTime(task.getStartTime())
                .processTime(task.getEndTime())
                .status(task.getEndTime() != null ? "已处理" : "待处理")
                .comment(task.getDeleteReason())
                .result(task.getEndTime() != null ? "完成" : null)
                .build();
    }
}
