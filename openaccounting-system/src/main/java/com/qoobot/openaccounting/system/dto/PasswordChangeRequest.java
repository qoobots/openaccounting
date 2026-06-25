package com.qoobot.openaccounting.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求
 *
 * @author openaccounting
 */
@Data
public class PasswordChangeRequest {

    /**
     * 原密码
     */
    @NotBlank(message = "原密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String newPassword;
}
