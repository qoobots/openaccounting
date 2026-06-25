package com.qoobot.openaccounting.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限VO
 *
 * @author openaccounting
 */
@Data
public class SysPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    private String parentName;

    private String permissionName;

    private String permissionCode;

    private String permissionType;

    private String path;

    private String component;

    private String icon;

    private String method;

    private Integer sort;

    private Integer status;

    private String remark;

    private List<SysPermissionVO> children;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
