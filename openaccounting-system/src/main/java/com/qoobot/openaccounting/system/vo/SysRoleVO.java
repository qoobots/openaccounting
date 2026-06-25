package com.qoobot.openaccounting.system.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色VO
 *
 * @author openaccounting
 */
@Data
public class SysRoleVO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色排序
     */
    private Integer sortOrder;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 权限列表
     */
    private List<PermissionInfo> permissions;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 权限信息
     */
    @Data
    public static class PermissionInfo {
        private Long id;
        private String name;
        private String code;
    }
}
