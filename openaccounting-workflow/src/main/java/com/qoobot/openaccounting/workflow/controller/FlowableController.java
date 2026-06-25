package com.qoobot.openaccounting.workflow.controller;

import com.qoobot.openaccounting.workflow.service.FlowableService;
import com.qoobot.openaccounting.workflow.vo.ProcessInstanceVO;
import com.qoobot.openaccounting.workflow.vo.TaskVO;
import com.qoobot.openaccounting.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Flowable工作流Controller
 *
 * @author openaccounting
 */
@Tag(name = "Flowable工作流", description = "Flowable工作流相关接口")
@RestController
@RequestMapping("/api/flowable")
@RequiredArgsConstructor
public class FlowableController {

    private final FlowableService flowableService;

    @Operation(summary = "部署流程定义")
    @PostMapping("/deploy")
    public Result<String> deployProcess(
            @RequestParam String processName,
            @RequestParam MultipartFile file) {
        try {
            String bpmnContent = new String(file.getBytes());
            String deploymentId = flowableService.deployProcess(processName, bpmnContent);
            return Result.success(deploymentId);
        } catch (Exception e) {
            return Result.fail("部署失败: " + e.getMessage());
        }
    }

    @Operation(summary = "启动流程实例")
    @PostMapping("/start-instance")
    public Result<String> startProcessInstance(
            @RequestParam String processKey,
            @RequestParam String businessKey,
            @RequestBody(required = false) Map<String, Object> variables) {
        String instanceId = flowableService.startProcessInstance(processKey, businessKey, variables);
        return Result.success(instanceId);
    }

    @Operation(summary = "完成任务")
    @PostMapping("/complete-task")
    public Result<Void> completeTask(
            @RequestParam String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        flowableService.completeTask(taskId, variables);
        return Result.success();
    }

    @Operation(summary = "查询我的待办任务")
    @GetMapping("/my-tasks")
    public Result<List<TaskVO>> getMyTasks(@AuthenticationPrincipal Long userId) {
        List<TaskVO> tasks = flowableService.getMyTasks(String.valueOf(userId));
        return Result.success(tasks);
    }

    @Operation(summary = "查询流程实例详情")
    @GetMapping("/instances/{instanceId}")
    public Result<ProcessInstanceVO> getProcessInstanceDetail(
            @Parameter(description = "流程实例ID") @PathVariable String instanceId) {
        ProcessInstanceVO instance = flowableService.getProcessInstanceDetail(instanceId);
        return Result.success(instance);
    }

    @Operation(summary = "获取流程图")
    @GetMapping("/diagram/{processDefinitionId}")
    public ResponseEntity<Resource> getProcessDiagram(
            @Parameter(description = "流程定义ID") @PathVariable String processDefinitionId) {
        byte[] diagram = flowableService.getProcessDiagram(processDefinitionId);

        ByteArrayResource resource = new ByteArrayResource(diagram);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=diagram.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(diagram.length)
                .body(resource);
    }
}
