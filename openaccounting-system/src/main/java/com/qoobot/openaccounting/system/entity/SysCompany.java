package com.qoobot.openaccounting.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.system.base.SystemBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公司信息实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_company")
public class SysCompany extends SystemBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父公司ID
     */
    private Long parentId;

    /**
     * 祖级列表（逗号分隔）
     */
    private String ancestors;

    /**
     * 公司编码
     */
    @TableField("company_code")
    private String companyCode;

    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 公司简称
     */
    private String shortName;

    /**
     * 统一社会信用代码
     */
    @TableField("social_credit_code")
    private String socialCreditCode;

    /**
     * 法定代表人
     */
    private String legalPerson;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 排序
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
