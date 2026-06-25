package com.qoobot.openaccounting.ledger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.ledger.dto.AccountCreateRequest;
import com.qoobot.openaccounting.ledger.dto.AccountQueryRequest;
import com.qoobot.openaccounting.ledger.dto.AccountUpdateRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.mapper.GlAccountMapper;
import com.qoobot.openaccounting.ledger.service.GlAccountService;
import com.qoobot.openaccounting.ledger.vo.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会计科目Service实现
 *
 * @author openaccounting
 */
@Service
public class GlAccountServiceImpl extends ServiceImpl<GlAccountMapper, GlAccount> implements GlAccountService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAccount(AccountCreateRequest request) {
        // 检查科目编码是否已存在
        if (checkAccountCodeExists(request.getAccountCode(), null)) {
            throw new BusinessException("科目编码已存在");
        }

        // 解析科目层级和父级
        parseAccountHierarchy(request);

        GlAccount account = new GlAccount();
        BeanUtils.copyProperties(request, account);
        account.setStatus(0); // 默认启用

        save(account);
        return account.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccount(Long id, AccountUpdateRequest request) {
        GlAccount account = getById(id);
        if (account == null) {
            throw new BusinessException("科目不存在");
        }

        // 检查科目编码是否被其他科目使用
        // 这里假设科目编码不能修改，如需修改需要额外校验

        BeanUtils.copyProperties(request, account);
        updateById(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAccount(Long id) {
        GlAccount account = getById(id);
        if (account == null) {
            throw new BusinessException("科目不存在");
        }

        // 检查是否有子科目
        int childrenCount = baseMapper.countChildren(id);
        if (childrenCount > 0) {
            throw new BusinessException("该科目存在子科目，无法删除");
        }

        // 检查是否被凭证使用
        int usedCount = baseMapper.countUsedInVoucher(id);
        if (usedCount > 0) {
            throw new BusinessException("该科目已被凭证使用，无法删除");
        }

        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAccounts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            deleteAccount(id);
        }
    }

    @Override
    public AccountVO getAccountById(Long id) {
        GlAccount account = getById(id);
        if (account == null) {
            return null;
        }
        return convertToVO(account);
    }

    @Override
    public List<AccountVO> listAccounts(AccountQueryRequest request, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<GlAccount> wrapper = buildQueryWrapper(request);

        Page<GlAccount> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        return page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countAccounts(AccountQueryRequest request) {
        LambdaQueryWrapper<GlAccount> wrapper = buildQueryWrapper(request);
        return count(wrapper);
    }

    @Override
    public List<AccountVO> getAccountTree(Long companyId, Integer status) {
        List<GlAccount> accounts = baseMapper.selectAccountTree(companyId, status);
        return buildAccountTree(accounts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccountStatus(Long id, Integer status) {
        GlAccount account = getById(id);
        if (account == null) {
            throw new BusinessException("科目不存在");
        }

        if (status != 0 && status != 1) {
            throw new BusinessException("状态值不正确");
        }

        account.setStatus(status);
        updateById(account);
    }

    @Override
    public boolean checkAccountCodeExists(String accountCode, Long excludeId) {
        LambdaQueryWrapper<GlAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GlAccount::getAccountCode, accountCode);
        if (excludeId != null) {
            wrapper.ne(GlAccount::getId, excludeId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public List<AccountVO> getLeafAccounts(Long companyId, Integer status) {
        List<GlAccount> accounts = baseMapper.selectLeafAccounts(companyId, status);
        return accounts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 解析科目层级和父级
     */
    private void parseAccountHierarchy(AccountCreateRequest request) {
        String[] parts = request.getAccountCode().split("-");
        int level = parts.length;

        request.setLevel(level);

        if (level == 1) {
            // 一级科目，父级为0
            request.setParentId(0L);
        } else {
            // 二级及以上科目，需要获取父级科目
            String parentCode = buildParentCode(parts);
            GlAccount parent = baseMapper.selectByAccountCode(parentCode);
            if (parent == null) {
                throw new BusinessException("上级科目不存在");
            }
            request.setParentId(parent.getId());
        }
    }

    /**
     * 构建父级科目编码
     */
    private String buildParentCode(String[] parts) {
        if (parts.length == 1) {
            return parts[0];
        }
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length - 1; i++) {
            sb.append("-").append(parts[i]);
        }
        return sb.toString();
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<GlAccount> buildQueryWrapper(AccountQueryRequest request) {
        LambdaQueryWrapper<GlAccount> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(request.getCompanyId() != null, GlAccount::getCompanyId, request.getCompanyId())
                .like(StringUtils.hasText(request.getAccountCode()), GlAccount::getAccountCode, request.getAccountCode())
                .like(StringUtils.hasText(request.getAccountName()), GlAccount::getAccountName, request.getAccountName())
                .eq(StringUtils.hasText(request.getAccountType()), GlAccount::getAccountType, request.getAccountType())
                .eq(request.getParentId() != null, GlAccount::getParentId, request.getParentId())
                .eq(request.getLevel() != null, GlAccount::getLevel, request.getLevel())
                .eq(StringUtils.hasText(request.getBalanceDirection()), GlAccount::getBalanceDirection, request.getBalanceDirection())
                .eq(request.getStatus() != null, GlAccount::getStatus, request.getStatus())
                .orderByAsc(GlAccount::getAccountCode);

        // 辅助核算过滤
        if (request.getHasAuxiliary() != null && request.getHasAuxiliary()) {
            wrapper.and(w -> w.eq(GlAccount::getAuxiliaryDept, 1)
                    .or().eq(GlAccount::getAuxiliaryProject, 1)
                    .or().eq(GlAccount::getAuxiliaryCustomer, 1)
                    .or().eq(GlAccount::getAuxiliarySupplier, 1)
                    .or().eq(GlAccount::getAuxiliaryEmployee, 1));
        }

        return wrapper;
    }

    /**
     * 构建科目树
     */
    private List<AccountVO> buildAccountTree(List<GlAccount> accounts) {
        List<AccountVO> vos = accounts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        List<AccountVO> roots = new ArrayList<>();
        vos.forEach(vo -> {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                roots.add(vo);
            }
        });

        roots.forEach(root -> buildChildren(root, vos));

        return roots;
    }

    /**
     * 递归构建子节点
     */
    private void buildChildren(AccountVO parent, List<AccountVO> all) {
        List<AccountVO> children = all.stream()
                .filter(child -> parent.getId().equals(child.getParentId()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parent.setChildren(children);
            parent.setHasChildren(true);
            children.forEach(child -> buildChildren(child, all));
        } else {
            parent.setHasChildren(false);
        }
    }

    /**
     * 转换为VO
     */
    private AccountVO convertToVO(GlAccount account) {
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);

        // 转换科目类型名称
        vo.setAccountTypeName(getAccountTypeName(account.getAccountType()));

        // 获取父级名称
        if (account.getParentId() != null && account.getParentId() != 0) {
            GlAccount parent = getById(account.getParentId());
            if (parent != null) {
                vo.setParentName(parent.getAccountName());
            }
        }

        // 检查是否有子科目
        int childrenCount = baseMapper.countChildren(account.getId());
        vo.setHasChildren(childrenCount > 0);

        return vo;
    }

    /**
     * 获取科目类型名称
     */
    private String getAccountTypeName(String accountType) {
        return switch (accountType) {
            case "assets" -> "资产";
            case "liabilities" -> "负债";
            case "equity" -> "所有者权益";
            case "revenue" -> "收入";
            case "expense" -> "费用";
            default -> "未知";
        };
    }
}
