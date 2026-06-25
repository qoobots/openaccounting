package com.qoobot.openaccounting.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qoobot.openaccounting.ledger.entity.GlVoucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 凭证Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface GlVoucherMapper extends BaseMapper<GlVoucher> {

    /**
     * 根据凭证号查询
     *
     * @param voucherNo 凭证号
     * @return 凭证信息
     */
    GlVoucher selectByVoucherNo(@Param("voucherNo") String voucherNo);

    /**
     * 查询最大凭证号
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @return 最大凭证号
     */
    Long selectMaxVoucherNo(@Param("companyId") Long companyId,
                            @Param("accountingYear") Integer accountingYear,
                            @Param("accountingPeriod") Integer accountingPeriod);

    /**
     * 根据公司ID查询凭证列表
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param status 状态
     * @param page 分页
     * @return 凭证分页列表
     */
    IPage<GlVoucher> selectVoucherPage(@Param("companyId") Long companyId,
                                       @Param("accountingYear") Integer accountingYear,
                                       @Param("accountingPeriod") Integer accountingPeriod,
                                       @Param("status") String status,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("voucherNo") String voucherNo,
                                       Page<GlVoucher> page);

    /**
     * 统计期间内凭证数量
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param status 状态
     * @return 数量
     */
    Long countVouchers(@Param("companyId") Long companyId,
                      @Param("accountingYear") Integer accountingYear,
                      @Param("accountingPeriod") Integer accountingPeriod,
                      @Param("status") String status);

    /**
     * 更新凭证状态
     *
     * @param id 凭证ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 更新审核信息
     *
     * @param id 凭证ID
     * @param auditorId 审核人ID
     * @param auditorName 审核人
     * @return 更新数量
     */
    int updateAuditInfo(@Param("id") Long id, @Param("auditorId") Long auditorId, @Param("auditorName") String auditorName);

    /**
     * 更新过账信息
     *
     * @param id 凭证ID
     * @return 更新数量
     */
    int updatePostInfo(@Param("id") Long id);
}
