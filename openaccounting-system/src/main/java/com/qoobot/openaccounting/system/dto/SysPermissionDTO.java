package com.qoobot.openaccounting.system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限DTO
 *
 * @author openaccounting
 */
@Data
public class SysPermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    @NotBlank(message = "权限代码不能为空")
    private String permissionCode;

    private String permissionType;

    private String path;

    private String component;

    private String icon;

    private String method;

    private Integer sort;

    private Integer status;

    private String remark;
}
