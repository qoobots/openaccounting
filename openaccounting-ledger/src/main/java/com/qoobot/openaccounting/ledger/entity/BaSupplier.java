package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商档案实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ba_supplier")
public class BaSupplier extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 供应商编码
     */
    @TableField("supplier_code")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @TableField("supplier_name")
    private String supplierName;

    /**
     * 供应商简称
     */
    @TableField("short_name")
    private String shortName;

    /**
     * 供应商类型
     */
    @TableField("supplier_type")
    private String supplierType;

    /**
     * 联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 税号
     */
    @TableField("tax_no")
    private String taxNo;

    /**
     * 开户行
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 银行账号
     */
    @TableField("bank_account")
    private String bankAccount;

    /**
     * 信用额度
     */
    @TableField("credit_limit")
    private BigDecimal creditLimit;

    /**
     * 状态：0启用 1停用
     */
    @TableField("status")
    private Integer status;
}
