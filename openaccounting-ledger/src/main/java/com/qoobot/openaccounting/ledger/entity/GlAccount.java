package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会计科目实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gl_account")
public class GlAccount extends BaseEntity {

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
     * 科目编码
     */
    @TableField("account_code")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 上级科目ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 层级
     */
    @TableField("level")
    private Integer level;

    /**
     * 科目类型
     */
    @TableField("account_type")
    private String accountType;

    /**
     * 余额方向：debit借方 credit贷方
     */
    @TableField("balance_direction")
    private String balanceDirection;

    /**
     * 部门核算
     */
    @TableField("auxiliary_dept")
    private Integer auxiliaryDept;

    /**
     * 项目核算
     */
    @TableField("auxiliary_project")
    private Integer auxiliaryProject;

    /**
     * 客户核算
     */
    @TableField("auxiliary_customer")
    private Integer auxiliaryCustomer;

    /**
     * 供应商核算
     */
    @TableField("auxiliary_supplier")
    private Integer auxiliarySupplier;

    /**
     * 员工核算
     */
    @TableField("auxiliary_employee")
    private Integer auxiliaryEmployee;

    /**
     * 状态：0启用 1停用
     */
    @TableField("status")
    private Integer status;
}
