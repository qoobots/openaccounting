package com.qoobot.openaccounting.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.system.entity.SysCompany;
import com.qoobot.openaccounting.system.vo.SysCompanyVO;

import java.util.List;

/**
 * 公司服务接口
 *
 * @author openaccounting
 */
public interface SysCompanyService extends IService<SysCompany> {

    /**
     * 分页查询公司列表
     *
     * @param page      分页参数
     * @param companyName 公司名称（模糊查询）
     * @param status    状态
     * @return 公司分页列表
     */
    Page<SysCompanyVO> selectCompanyPage(Page<SysCompanyVO> page, String companyName, Integer status);

    /**
     * 查询公司树形结构
     *
     * @return 公司树
     */
    List<SysCompanyVO> selectCompanyTree();

    /**
     * 根据父公司ID查询子公司列表
     *
     * @param parentId 父公司ID
     * @return 子公司列表
     */
    List<SysCompanyVO> selectByParentId(Long parentId);

    /**
     * 创建公司
     *
     * @param company 公司
     * @return 公司ID
     */
    Long createCompany(SysCompany company);

    /**
     * 更新公司
     *
     * @param company 公司
     * @return 是否成功
     */
    Boolean updateCompany(SysCompany company);

    /**
     * 删除公司
     *
     * @param id 公司ID
     * @return 是否成功
     */
    Boolean deleteCompany(Long id);
}
