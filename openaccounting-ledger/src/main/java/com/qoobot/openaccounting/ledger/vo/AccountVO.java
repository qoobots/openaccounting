package com.qoobot.openaccounting.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 科目响应VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "科目信息")
public class AccountVO {

    @Schema(description = "科目ID")
    private Long id;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "科目编码")
    private String accountCode;

    @Schema(description = "科目名称")
    private String accountName;

    @Schema(description = "上级科目ID")
    private Long parentId;

    @Schema(description = "上级科目名称")
    private String parentName;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "科目类型")
    private String accountType;

    @Schema(description = "科目类型名称")
    private String accountTypeName;

    @Schema(description = "余额方向")
    private String balanceDirection;

    @Schema(description = "部门核算: 0-否, 1-是")
    private Integer auxiliaryDept;

    @Schema(description = "项目核算: 0-否, 1-是")
    private Integer auxiliaryProject;

    @Schema(description = "客户核算: 0-否, 1-是")
    private Integer auxiliaryCustomer;

    @Schema(description = "供应商核算: 0-否, 1-是")
    private Integer auxiliarySupplier;

    @Schema(description = "员工核算: 0-否, 1-是")
    private Integer auxiliaryEmployee;

    @Schema(description = "状态: 0-启用, 1-停用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否有子科目")
    private Boolean hasChildren;

    @Schema(description = "子科目列表")
    private List<AccountVO> children;
}
