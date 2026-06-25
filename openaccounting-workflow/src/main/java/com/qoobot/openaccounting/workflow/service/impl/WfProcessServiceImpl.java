package com.qoobot.openaccounting.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.workflow.dto.ProcessStartRequest;
import com.qoobot.openaccounting.workflow.dto.TaskCompleteRequest;
import com.qoobot.openaccounting.workflow.entity.WfProcessDefinition;
import com.qoobot.openaccounting.workflow.entity.WfProcessInstance;
import com.qoobot.openaccounting.workflow.entity.WfTask;
import com.qoobot.openaccounting.workflow.mapper.WfProcessDefinitionMapper;
import com.qoobot.openaccounting.workflow.mapper.WfProcessInstanceMapper;
import com.qoobot.openaccounting.workflow.mapper.WfTaskMapper;
import com.qoobot.openaccounting.workflow.service.WfProcessService;
import com.qoobot.openaccounting.workflow.vo.ProcessInstanceVO;
import com.qoobot.openaccounting.workflow.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class WfProcessServiceImpl extends ServiceImpl<WfProcessInstanceMapper, WfProcessInstance> implements WfProcessService {

    private final WfProcessDefinitionMapper processDefinitionMapper;
    private final WfTaskMapper taskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long startProcess(ProcessStartRequest request, Long userId, String username) {
        // 查询流程定义
        WfProcessDefinition definition = processDefinitionMapper.selectById(request.getDefinitionId());
        if (definition == null) {
            throw new RuntimeException("流程定义不存在");
        }

        if (!"启用".equals(definition.getStatus())) {
            throw new RuntimeException("流程未启用");
        }

        // 生成流程实例编号
        String instanceNo = generateInstanceNo(definition.getProcessCode());

        // 创建流程实例
        WfProcessInstance instance = new WfProcessInstance();
        instance.setDefinitionId(request.getDefinitionId());
        instance.setCompanyId(request.getCompanyId());
        instance.setBusinessType(request.getBusinessType());
        instance.setBusinessId(request.getBusinessId());
        instance.setInstanceNo(instanceNo);
        instance.setInitiatorId(userId);
        instance.setInitiatorName(username);
        instance.setStartTime(LocalDateTime.now());
        instance.setCurrentNode("开始");
        instance.setStatus("进行中");
        instance.setRemark(request.getRemark());

        save(instance);

        // 创建第一个任务
        createFirstTask(instance, definition);

        return instance.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(TaskCompleteRequest request, Long userId, String username) {
        // 查询任务
        WfTask task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        if (!"待处理".equals(task.getStatus())) {
            throw new RuntimeException("任务已处理");
        }

        if (!task.getAssigneeId().equals(userId)) {
            throw new RuntimeException("无权处理此任务");
        }

        // 更新任务状态
        task.setStatus("已处理");
        task.setProcessTime(LocalDateTime.now());
        task.setComment(request.getComment());
        task.setResult(request.getResult());
        taskMapper.updateById(task);

        // 查询流程实例
        WfProcessInstance instance = getById(task.getInstanceId());

        if ("拒绝".equals(request.getResult())) {
            // 拒绝：结束流程
            instance.setStatus("已拒绝");
            instance.setEndTime(LocalDateTime.now());
            updateById(instance);

            // TODO: 更新业务单据状态为已拒绝
        } else {
            // 通过：判断是否有下一节点
            // TODO: 根据流程定义判断下一节点
            boolean hasNext = false; // 简化处理，实际需要从流程定义中获取

            if (hasNext) {
                // 创建下一任务
                // TODO: 创建下一任务
            } else {
                // 没有下一节点，结束流程
                instance.setStatus("已完成");
                instance.setEndTime(LocalDateTime.now());
                updateById(instance);

                // TODO: 更新业务单据状态为已通过
            }
        }
    }

    @Override
    public List<TaskVO> getMyTasks(Long userId) {
        LambdaQueryWrapper<WfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WfTask::getAssigneeId, userId);
        wrapper.eq(WfTask::getStatus, "待处理");
        wrapper.orderByDesc(WfTask::getCreateTime);

        List<WfTask> tasks = taskMapper.selectList(wrapper);

        return tasks.stream()
                .map(this::convertToTaskVO)
                .collect(Collectors.toList());
    }

    @Override
    public ProcessInstanceVO getInstanceDetail(Long instanceId) {
        // 查询流程实例
        WfProcessInstance instance = getById(instanceId);
        if (instance == null) {
            throw new RuntimeException("流程实例不存在");
        }

        // 查询任务列表
        LambdaQueryWrapper<WfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WfTask::getInstanceId, instanceId);
        wrapper.orderByAsc(WfTask::getCreateTime);
        List<WfTask> tasks = taskMapper.selectList(wrapper);

        List<TaskVO> taskVOs = tasks.stream()
                .map(this::convertToTaskVO)
                .collect(Collectors.toList());

        // 查询流程定义
        WfProcessDefinition definition = processDefinitionMapper.selectById(instance.getDefinitionId());

        return ProcessInstanceVO.builder()
                .id(instance.getId())
                .instanceNo(instance.getInstanceNo())
                .processName(definition != null ? definition.getProcessName() : "")
                .businessType(instance.getBusinessType())
                .businessId(instance.getBusinessId())
                .initiatorName(instance.getInitiatorName())
                .startTime(instance.getStartTime())
                .endTime(instance.getEndTime())
                .currentNode(instance.getCurrentNode())
                .status(instance.getStatus())
                .tasks(taskVOs)
                .build();
    }

    /**
     * 创建第一个任务
     */
    private void createFirstTask(WfProcessInstance instance, WfProcessDefinition definition) {
        // TODO: 从流程定义中解析第一个节点
        // 简化处理：创建一个默认任务
        WfTask task = new WfTask();
        task.setInstanceId(instance.getId());
        task.setTaskNo(generateTaskNo(instance.getInstanceNo()));
        task.setTaskName("审核");
        task.setTaskNode("审核");
        task.setAssigneeId(1L); // TODO: 根据流程定义分配处理人
        task.setAssigneeName("系统管理员");
        task.setCreateTime(LocalDateTime.now());
        task.setStatus("待处理");

        taskMapper.insert(task);
    }

    /**
     * 生成流程实例编号
     */
    private String generateInstanceNo(String processCode) {
        return "PI" + processCode.toUpperCase() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 生成任务编号
     */
    private String generateTaskNo(String instanceNo) {
        return "T" + instanceNo.substring(2) + System.currentTimeMillis() % 10000;
    }

    /**
     * 转换为任务VO
     */
    private TaskVO convertToTaskVO(WfTask task) {
        return TaskVO.builder()
                .id(task.getId())
                .taskNo(task.getTaskNo())
                .taskName(task.getTaskName())
                .taskNode(task.getTaskNode())
                .assigneeName(task.getAssigneeName())
                .createTime(task.getCreateTime())
                .processTime(task.getProcessTime())
                .status(task.getStatus())
                .comment(task.getComment())
                .result(task.getResult())
                .build();
    }
}
