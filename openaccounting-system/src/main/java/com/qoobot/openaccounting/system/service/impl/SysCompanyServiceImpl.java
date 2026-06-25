package com.qoobot.openaccounting.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.constant.ResultCode;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.system.entity.SysCompany;
import com.qoobot.openaccounting.system.mapper.SysCompanyMapper;
import com.qoobot.openaccounting.system.service.SysCompanyService;
import com.qoobot.openaccounting.system.vo.SysCompanyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公司服务实现
 *
 * @author openaccounting
 */
@Service
public class SysCompanyServiceImpl extends ServiceImpl<SysCompanyMapper, SysCompany> implements SysCompanyService {

    @Override
    public Page<SysCompanyVO> selectCompanyPage(Page<SysCompanyVO> page, String companyName, Integer status) {
        return baseMapper.selectCompanyPage(page, companyName, status);
    }

    @Override
    public List<SysCompanyVO> selectCompanyTree() {
        List<SysCompany> allCompanies = baseMapper.selectList(new LambdaQueryWrapper<>());
        return buildTree(allCompanies, 0L);
    }

    @Override
    public List<SysCompanyVO> selectByParentId(Long parentId) {
        List<SysCompany> companies = baseMapper.selectByParentId(parentId);
        return companies.stream().map(company -> {
            SysCompanyVO vo = new SysCompanyVO();
            BeanUtils.copyProperties(company, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCompany(SysCompany company) {
        LambdaQueryWrapper<SysCompany> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCompany::getCompanyCode, company.getCompanyCode());
        SysCompany existCompany = baseMapper.selectOne(wrapper);
        if (existCompany != null) {
            throw new BusinessException(ResultCode.CONFLICT, "公司代码已存在");
        }

        if (StringUtils.hasText(company.getSocialCreditCode())) {
            LambdaQueryWrapper<SysCompany> creditWrapper = new LambdaQueryWrapper<>();
            creditWrapper.eq(SysCompany::getSocialCreditCode, company.getSocialCreditCode());
            SysCompany creditCompany = baseMapper.selectOne(creditWrapper);
            if (creditCompany != null) {
                throw new BusinessException(ResultCode.CONFLICT, "统一社会信用代码已存在");
            }
        }

        if (company.getParentId() != null && company.getParentId() > 0) {
            SysCompany parentCompany = baseMapper.selectById(company.getParentId());
            if (parentCompany == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "父公司不存在");
            }
        }

        baseMapper.insert(company);
        return company.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCompany(SysCompany company) {
        SysCompany existCompany = baseMapper.selectById(company.getId());
        if (existCompany == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "公司不存在");
        }

        if (!existCompany.getCompanyCode().equals(company.getCompanyCode())) {
            LambdaQueryWrapper<SysCompany> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysCompany::getCompanyCode, company.getCompanyCode());
            wrapper.ne(SysCompany::getId, company.getId());
            SysCompany codeCompany = baseMapper.selectOne(wrapper);
            if (codeCompany != null) {
                throw new BusinessException(ResultCode.CONFLICT, "公司代码已存在");
            }
        }

        if (StringUtils.hasText(company.getSocialCreditCode())
                && !company.getSocialCreditCode().equals(existCompany.getSocialCreditCode())) {
            LambdaQueryWrapper<SysCompany> creditWrapper = new LambdaQueryWrapper<>();
            creditWrapper.eq(SysCompany::getSocialCreditCode, company.getSocialCreditCode());
            creditWrapper.ne(SysCompany::getId, company.getId());
            SysCompany creditCompany = baseMapper.selectOne(creditWrapper);
            if (creditCompany != null) {
                throw new BusinessException(ResultCode.CONFLICT, "统一社会信用代码已存在");
            }
        }

        if (company.getParentId() != null && company.getParentId() > 0) {
            if (company.getParentId().equals(company.getId())) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "父公司不能是自己");
            }

            SysCompany parentCompany = baseMapper.selectById(company.getParentId());
            if (parentCompany == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "父公司不存在");
            }
        }

        return baseMapper.updateById(company) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCompany(Long id) {
        SysCompany company = baseMapper.selectById(id);
        if (company == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "公司不存在");
        }

        LambdaQueryWrapper<SysCompany> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(SysCompany::getParentId, id);
        Long childCount = baseMapper.selectCount(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "该公司下有子公司，无法删除");
        }

        return baseMapper.deleteById(id) > 0;
    }

    private List<SysCompanyVO> buildTree(List<SysCompany> companies, Long parentId) {
        List<SysCompanyVO> tree = new ArrayList<>();
        for (SysCompany company : companies) {
            if (company.getParentId() == null || company.getParentId().equals(parentId)) {
                SysCompanyVO vo = new SysCompanyVO();
                BeanUtils.copyProperties(company, vo);
                vo.setChildren(buildTree(companies, company.getId()));
                tree.add(vo);
            }
        }
        return tree;
    }
}
