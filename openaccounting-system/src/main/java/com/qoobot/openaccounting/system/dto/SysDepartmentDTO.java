package com.qoobot.openaccounting.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门DTO
 *
 * @author openaccounting
 */
@Data
public class SysDepartmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    private String departmentName;

    @NotBlank(message = "部门代码不能为空")
    private String departmentCode;

    private Integer sort;

    private Integer status;

    private String remark;
}
