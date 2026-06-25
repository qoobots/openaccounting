package com.qoobot.openaccounting.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 公司VO
 *
 * @author openaccounting
 */
@Data
public class SysCompanyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    private String parentName;

    private String companyName;

    private String companyCode;

    private String socialCreditCode;

    private String legalPerson;

    private String contactPhone;

    private String address;

    private Integer sort;

    private Integer status;

    private String remark;

    private List<SysCompanyVO> children;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
