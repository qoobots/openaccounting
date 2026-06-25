package com.qoobot.openaccounting.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.system.entity.SysCompany;
import com.qoobot.openaccounting.system.vo.SysCompanyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公司信息Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface SysCompanyMapper extends BaseMapper<SysCompany> {

    /**
     * 分页查询公司列表
     *
     * @param page        分页参数
     * @param companyName 公司名称
     * @param status      状态
     * @return 公司分页列表
     */
    Page<SysCompanyVO> selectCompanyPage(Page<SysCompanyVO> page, @Param("companyName") String companyName, @Param("status") Integer status);

    /**
     * 根据父公司ID查询子公司列表
     *
     * @param parentId 父公司ID
     * @return 子公司列表
     */
    List<SysCompany> selectByParentId(@Param("parentId") Long parentId);
}
