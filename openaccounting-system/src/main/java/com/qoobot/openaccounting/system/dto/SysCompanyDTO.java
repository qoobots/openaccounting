package com.qoobot.openaccounting.system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 公司DTO
 *
 * @author openaccounting
 */
@Data
public class SysCompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    @NotBlank(message = "公司代码不能为空")
    private String companyCode;

    private String socialCreditCode;

    private String legalPerson;

    private String contactPhone;

    private String address;

    private Integer sort;

    private Integer status;

    private String remark;
}
