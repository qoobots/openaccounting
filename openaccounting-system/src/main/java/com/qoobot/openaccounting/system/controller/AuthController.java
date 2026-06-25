package com.qoobot.openaccounting.system.controller;

import com.qoobot.openaccounting.common.Result;
import com.qoobot.openaccounting.system.dto.LoginRequest;
import com.qoobot.openaccounting.system.dto.PasswordChangeRequest;
import com.qoobot.openaccounting.system.dto.PasswordResetRequest;
import com.qoobot.openaccounting.system.dto.RefreshTokenRequest;
import com.qoobot.openaccounting.system.service.AuthService;
import com.qoobot.openaccounting.system.vo.LoginVO;
import com.qoobot.openaccounting.system.vo.TokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 认证授权控制器
 *
 * @author openaccounting
 */
@Tag(name = "认证授权", description = "登录登出、Token刷新等接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        authService.logout(token);
        return Result.success();
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public Result<TokenVO> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return Result.success(authService.refreshToken(request));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/user/info")
    public Result<LoginVO> getUserInfo(@RequestHeader("X-User-Id") Long userId) {
        return Result.success(authService.getUserInfo(userId));
    }

    @Operation(summary = "修改密码")
    @PostMapping("/change-password")
    public Result<Void> changePassword(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody PasswordChangeRequest request) {
        authService.changePassword(userId, request);
        return Result.success();
    }

    @Operation(summary = "重置密码（管理员操作）")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return Result.success();
    }
}
