package com.qoobot.openaccounting.ledger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.ledger.dto.VoucherCreateRequest;
import com.qoobot.openaccounting.ledger.dto.VoucherUpdateRequest;
import com.qoobot.openaccounting.ledger.entity.GlVoucher;
import com.qoobot.openaccounting.ledger.vo.VoucherVO;

import java.util.List;

/**
 * 凭证Service
 *
 * @author openaccounting
 */
public interface GlVoucherService extends IService<GlVoucher> {

    /**
     * 创建凭证
     *
     * @param request 创建请求
     * @param makerId 制单人ID
     * @param makerName 制单人
     * @return 凭证ID
     */
    Long createVoucher(VoucherCreateRequest request, Long makerId, String makerName);

    /**
     * 更新凭证
     *
     * @param id 凭证ID
     * @param request 更新请求
     */
    void updateVoucher(Long id, VoucherUpdateRequest request);

    /**
     * 删除凭证
     *
     * @param id 凭证ID
     */
    void deleteVoucher(Long id);

    /**
     * 批量删除凭证
     *
     * @param ids 凭证ID列表
     */
    void batchDeleteVouchers(List<Long> ids);

    /**
     * 根据ID查询凭证
     *
     * @param id 凭证ID
     * @return 凭证信息
     */
    VoucherVO getVoucherById(Long id);

    /**
     * 提交凭证
     *
     * @param id 凭证ID
     */
    void submitVoucher(Long id);

    /**
     * 审核凭证
     *
     * @param id 凭证ID
     * @param auditorId 审核人ID
     * @param auditorName 审核人
     */
    void auditVoucher(Long id, Long auditorId, String auditorName);

    /**
     * 取消审核
     *
     * @param id 凭证ID
     */
    void unAuditVoucher(Long id);

    /**
     * 过账凭证
     *
     * @param id 凭证ID
     */
    void postVoucher(Long id);

    /**
     * 反过账凭证
     *
     * @param id 凭证ID
     */
    void unPostVoucher(Long id);

    /**
     * 分页查询凭证列表
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param status 状态
     * @param voucherNo 凭证号
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 凭证列表
     */
    List<VoucherVO> listVouchers(Long companyId, Integer accountingYear, Integer accountingPeriod,
                                 String status, String voucherNo, Integer pageNum, Integer pageSize);

    /**
     * 统计凭证数量
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param status 状态
     * @return 总数
     */
    Long countVouchers(Long companyId, Integer accountingYear, Integer accountingPeriod, String status);

    /**
     * 复制凭证
     *
     * @param id 凭证ID
     * @return 新凭证ID
     */
    Long copyVoucher(Long id);
}
