package com.qoobot.openaccounting.ledger.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.ledger.dto.VoucherCreateRequest;
import com.qoobot.openaccounting.ledger.dto.VoucherEntryRequest;
import com.qoobot.openaccounting.ledger.dto.VoucherUpdateRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.entity.GlVoucher;
import com.qoobot.openaccounting.ledger.entity.GlVoucherEntry;
import com.qoobot.openaccounting.ledger.mapper.GlAccountMapper;
import com.qoobot.openaccounting.ledger.mapper.GlVoucherEntryMapper;
import com.qoobot.openaccounting.ledger.mapper.GlVoucherMapper;
import com.qoobot.openaccounting.ledger.service.GlVoucherService;
import com.qoobot.openaccounting.ledger.vo.VoucherEntryVO;
import com.qoobot.openaccounting.ledger.vo.VoucherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 凭证Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class GlVoucherServiceImpl extends ServiceImpl<GlVoucherMapper, GlVoucher> implements GlVoucherService {

    private final GlVoucherEntryMapper voucherEntryMapper;
    private final GlAccountMapper accountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createVoucher(VoucherCreateRequest request, Long makerId, String makerName) {
        // 验证分录数量
        if (request.getEntries() == null || request.getEntries().size() < 2) {
            throw new BusinessException("凭证分录至少需要2条");
        }

        // 验证借贷平衡
        validateBalance(request.getEntries());

        // 验证科目有效性
        validateAccounts(request.getEntries());

        // 生成凭证号
        String voucherNo = generateVoucherNo(request.getCompanyId(),
                request.getAccountingYear(), request.getAccountingPeriod());

        // 创建凭证主表
        GlVoucher voucher = new GlVoucher();
        BeanUtils.copyProperties(request, voucher);
        voucher.setVoucherNo(voucherNo);
        voucher.setStatus("draft");
        voucher.setMakerId(makerId);
        voucher.setMakerName(makerName);
        voucher.setEntryCount(request.getEntries().size());
        voucher.setTotalDebit(calculateTotalDebit(request.getEntries()));
        voucher.setTotalCredit(calculateTotalCredit(request.getEntries()));

        save(voucher);

        // 创建凭证分录
        createVoucherEntries(voucher.getId(), request.getEntries());

        return voucher.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVoucher(Long id, VoucherUpdateRequest request) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        // 只有草稿状态可以修改
        if (!"draft".equals(voucher.getStatus())) {
            throw new BusinessException("只有草稿状态的凭证可以修改");
        }

        // 验证分录数量
        if (request.getEntries() == null || request.getEntries().size() < 2) {
            throw new BusinessException("凭证分录至少需要2条");
        }

        // 验证借贷平衡
        validateBalance(request.getEntries());

        // 验证科目有效性
        validateAccounts(request.getEntries());

        // 更新凭证主表
        if (request.getVoucherDate() != null) {
            voucher.setVoucherDate(request.getVoucherDate());
        }
        if (request.getVoucherType() != null) {
            voucher.setVoucherType(request.getVoucherType());
        }
        if (request.getAbstract() != null) {
            voucher.setAbstract(request.getAbstract());
        }
        voucher.setEntryCount(request.getEntries().size());
        voucher.setTotalDebit(calculateTotalDebit(request.getEntries()));
        voucher.setTotalCredit(calculateTotalCredit(request.getEntries()));

        updateById(voucher);

        // 删除旧分录，创建新分录
        voucherEntryMapper.deleteByVoucherId(id);
        createVoucherEntries(id, request.getEntries());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVoucher(Long id) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        // 只有草稿状态可以删除
        if (!"draft".equals(voucher.getStatus())) {
            throw new BusinessException("只有草稿状态的凭证可以删除");
        }

        // 删除分录
        voucherEntryMapper.deleteByVoucherId(id);

        // 删除凭证
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteVouchers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            deleteVoucher(id);
        }
    }

    @Override
    public VoucherVO getVoucherById(Long id) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            return null;
        }
        return convertToVO(voucher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitVoucher(Long id) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        if (!"draft".equals(voucher.getStatus())) {
            throw new BusinessException("只有草稿状态的凭证可以提交");
        }

        voucher.setStatus("submitted");
        updateById(voucher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditVoucher(Long id, Long auditorId, String auditorName) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        if (!"submitted".equals(voucher.getStatus())) {
            throw new BusinessException("只有已提交状态的凭证可以审核");
        }

        voucher.setStatus("audited");
        voucher.setAuditorId(auditorId);
        voucher.setAuditorName(auditorName);
        voucher.setAuditTime(LocalDateTime.now());

        baseMapper.updateAuditInfo(id, auditorId, auditorName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unAuditVoucher(Long id) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        if (!"audited".equals(voucher.getStatus())) {
            throw new BusinessException("只有已审核状态的凭证可以取消审核");
        }

        voucher.setStatus("submitted");
        voucher.setAuditorId(null);
        voucher.setAuditorName(null);
        voucher.setAuditTime(null);

        updateById(voucher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postVoucher(Long id) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        if (!"audited".equals(voucher.getStatus())) {
            throw new BusinessException("只有已审核状态的凭证可以过账");
        }

        // TODO: 生成总账余额
        // 这里需要实现余额更新的逻辑

        voucher.setStatus("posted");
        voucher.setPostTime(LocalDateTime.now());

        baseMapper.updatePostInfo(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unPostVoucher(Long id) {
        GlVoucher voucher = getById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        if (!"posted".equals(voucher.getStatus())) {
            throw new BusinessException("只有已过账状态的凭证可以反过账");
        }

        // TODO: 回滚总账余额
        // 这里需要实现余额回滚的逻辑

        voucher.setStatus("audited");
        voucher.setPostTime(null);

        updateById(voucher);
    }

    @Override
    public List<VoucherVO> listVouchers(Long companyId, Integer accountingYear, Integer accountingPeriod,
                                       String status, String voucherNo, Integer pageNum, Integer pageSize) {
        Page<GlVoucher> page = new Page<>(pageNum, pageSize);
        IPage<GlVoucher> voucherPage = baseMapper.selectVoucherPage(
                companyId, accountingYear, accountingPeriod, status, null, null, voucherNo, page);

        return voucherPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countVouchers(Long companyId, Integer accountingYear, Integer accountingPeriod, String status) {
        return baseMapper.countVouchers(companyId, accountingYear, accountingPeriod, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyVoucher(Long id) {
        GlVoucher sourceVoucher = getById(id);
        if (sourceVoucher == null) {
            throw new BusinessException("凭证不存在");
        }

        // 创建新凭证
        GlVoucher newVoucher = new GlVoucher();
        BeanUtils.copyProperties(sourceVoucher, newVoucher, "id", "voucherNo", "createTime", "updateTime");
        newVoucher.setStatus("draft");
        newVoucher.setVoucherNo(generateVoucherNo(newVoucher.getCompanyId(),
                newVoucher.getAccountingYear(), newVoucher.getAccountingPeriod()));
        newVoucher.setAuditTime(null);
        newVoucher.setAuditorId(null);
        newVoucher.setAuditorName(null);
        newVoucher.setPostTime(null);

        save(newVoucher);

        // 复制分录
        List<GlVoucherEntry> sourceEntries = voucherEntryMapper.listByVoucherId(id);
        List<GlVoucherEntry> newEntries = new ArrayList<>();
        for (GlVoucherEntry entry : sourceEntries) {
            GlVoucherEntry newEntry = new GlVoucherEntry();
            BeanUtils.copyProperties(entry, newEntry, "id", "createTime", "updateTime");
            newEntry.setVoucherId(newVoucher.getId());
            newEntries.add(newEntry);
        }
        voucherEntryMapper.batchInsert(newEntries);

        return newVoucher.getId();
    }

    /**
     * 生成凭证号
     */
    private String generateVoucherNo(Long companyId, Integer year, Integer period) {
        Long maxNo = baseMapper.selectMaxVoucherNo(companyId, year, period);
        if (maxNo == null) {
            return String.format("%d%02d-001", year, period);
        } else {
            String maxNoStr = maxNo.toString();
            String prefix = maxNoStr.substring(0, 7); // 202401-
            int seq = Integer.parseInt(maxNoStr.substring(7));
            return String.format("%s-%03d", prefix, seq + 1);
        }
    }

    /**
     * 创建凭证分录
     */
    private void createVoucherEntries(Long voucherId, List<VoucherEntryRequest> entryRequests) {
        List<GlVoucherEntry> entries = new ArrayList<>();
        for (int i = 0; i < entryRequests.size(); i++) {
            VoucherEntryRequest request = entryRequests.get(i);
            GlVoucherEntry entry = new GlVoucherEntry();
            BeanUtils.copyProperties(request, entry);
            entry.setVoucherId(voucherId);
            entry.setLineNo(i + 1);

            // 填充科目信息
            GlAccount account = accountMapper.selectById(request.getAccountId());
            if (account != null) {
                entry.setAccountCode(account.getAccountCode());
                entry.setAccountName(account.getAccountName());
            }

            entries.add(entry);
        }
        voucherEntryMapper.batchInsert(entries);
    }

    /**
     * 验证借贷平衡
     */
    private void validateBalance(List<VoucherEntryRequest> entries) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (VoucherEntryRequest entry : entries) {
            if (entry.getDebitAmount() != null) {
                totalDebit = totalDebit.add(entry.getDebitAmount());
            }
            if (entry.getCreditAmount() != null) {
                totalCredit = totalCredit.add(entry.getCreditAmount());
            }
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new BusinessException("借方合计必须等于贷方合计");
        }

        if (totalDebit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("借贷金额不能为零");
        }
    }

    /**
     * 验证科目有效性
     */
    private void validateAccounts(List<VoucherEntryRequest> entries) {
        for (VoucherEntryRequest entry : entries) {
            GlAccount account = accountMapper.selectById(entry.getAccountId());
            if (account == null) {
                throw new BusinessException("科目ID " + entry.getAccountId() + " 不存在");
            }
            if (account.getStatus() != 0) {
                throw new BusinessException("科目 " + account.getAccountName() + " 已停用");
            }
        }
    }

    /**
     * 计算借方合计
     */
    private BigDecimal calculateTotalDebit(List<VoucherEntryRequest> entries) {
        return entries.stream()
                .map(e -> e.getDebitAmount() != null ? e.getDebitAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 计算贷方合计
     */
    private BigDecimal calculateTotalCredit(List<VoucherEntryRequest> entries) {
        return entries.stream()
                .map(e -> e.getCreditAmount() != null ? e.getCreditAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 转换为VO
     */
    private VoucherVO convertToVO(GlVoucher voucher) {
        VoucherVO vo = new VoucherVO();
        BeanUtils.copyProperties(voucher, vo);

        // 转换状态名称
        vo.setStatusName(getStatusName(voucher.getStatus()));

        // 转换凭证类型名称
        vo.setVoucherTypeName(getVoucherTypeName(voucher.getVoucherType()));

        // 获取分录
        List<GlVoucherEntry> entries = voucherEntryMapper.listByVoucherId(voucher.getId());
        vo.setEntries(entries.stream()
                .map(this::convertEntryToVO)
                .collect(Collectors.toList()));

        return vo;
    }

    /**
     * 转换分录为VO
     */
    private VoucherEntryVO convertEntryToVO(GlVoucherEntry entry) {
        VoucherEntryVO vo = new VoucherEntryVO();
        BeanUtils.copyProperties(entry, vo);
        return vo;
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(String status) {
        return switch (status) {
            case "draft" -> "草稿";
            case "submitted" -> "已提交";
            case "audited" -> "已审核";
            case "posted" -> "已过账";
            default -> "未知";
        };
    }

    /**
     * 获取凭证类型名称
     */
    private String getVoucherTypeName(String voucherType) {
        return switch (voucherType) {
            case "receipt" -> "收款凭证";
            case "payment" -> "付款凭证";
            case "transfer" -> "转账凭证";
            case "general" -> "通用凭证";
            default -> "未知";
        };
    }
}
