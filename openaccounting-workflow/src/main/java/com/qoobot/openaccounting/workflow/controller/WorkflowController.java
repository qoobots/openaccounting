package com.qoobot.openaccounting.workflow.controller;

import com.qoobot.openaccounting.workflow.dto.ProcessStartRequest;
import com.qoobot.openaccounting.workflow.dto.TaskCompleteRequest;
import com.qoobot.openaccounting.workflow.service.WfProcessService;
import com.qoobot.openaccounting.workflow.vo.ProcessInstanceVO;
import com.qoobot.openaccounting.workflow.vo.TaskVO;
import com.qoobot.openaccounting.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作流Controller
 *
 * @author openaccounting
 */
@Tag(name = "工作流管理", description = "工作流管理相关接口")
@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final WfProcessService processService;

    @Operation(summary = "启动流程")
    @PostMapping("/start")
    public Result<Long> startProcess(
            @Valid @RequestBody ProcessStartRequest request,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        Long instanceId = processService.startProcess(request, userId, username);
        return Result.success(instanceId);
    }

    @Operation(summary = "完成任务")
    @PostMapping("/complete-task")
    public Result<Void> completeTask(
            @Valid @RequestBody TaskCompleteRequest request,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        processService.completeTask(request, userId, username);
        return Result.success();
    }

    @Operation(summary = "查询我的待办任务")
    @GetMapping("/my-tasks")
    public Result<List<TaskVO>> getMyTasks(@AuthenticationPrincipal Long userId) {
        List<TaskVO> tasks = processService.getMyTasks(userId);
        return Result.success(tasks);
    }

    @Operation(summary = "查询流程实例详情")
    @GetMapping("/instances/{id}")
    public Result<ProcessInstanceVO> getInstanceDetail(@Parameter(description = "流程实例ID") @PathVariable Long id) {
        ProcessInstanceVO instance = processService.getInstanceDetail(id);
        return Result.success(instance);
    }
}
