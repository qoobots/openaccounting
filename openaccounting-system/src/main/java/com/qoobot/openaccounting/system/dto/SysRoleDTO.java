package com.qoobot.openaccounting.system.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 角色DTO
 *
 * @author openaccounting
 */
@Data
public class SysRoleDTO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 数据范围（1-全部，2-自定义，3-本部门，4-本部门及以下，5-仅本人）
     */
    private Integer dataScope;

    /**
     * 角色排序
     */
    private Integer sort;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
