package com.qoobot.openaccounting.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.ledger.entity.GlBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 科目余额Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface GlBalanceMapper extends BaseMapper<GlBalance> {

    /**
     * 根据公司、科目、期间查询余额
     *
     * @param companyId 公司ID
     * @param accountId 科目ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @return 余额信息
     */
    GlBalance selectByAccountAndPeriod(@Param("companyId") Long companyId,
                                        @Param("accountId") Long accountId,
                                        @Param("accountingYear") Integer accountingYear,
                                        @Param("accountingPeriod") Integer accountingPeriod);

    /**
     * 查询科目余额列表
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param accountIds 科目ID列表
     * @return 余额列表
     */
    List<GlBalance> listBalances(@Param("companyId") Long companyId,
                                 @Param("accountingYear") Integer accountingYear,
                                 @Param("startPeriod") Integer startPeriod,
                                 @Param("endPeriod") Integer endPeriod,
                                 @Param("accountIds") List<Long> accountIds);

    /**
     * 查询总账
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param accountCodeLike 科目编码
     * @param accountNameLike 科目名称
     * @param onlyWithBalance 是否只显示有余额的
     * @return 总账列表
     */
    List<GlBalance> selectGeneralLedger(@Param("companyId") Long companyId,
                                        @Param("accountingYear") Integer accountingYear,
                                        @Param("startPeriod") Integer startPeriod,
                                        @Param("endPeriod") Integer endPeriod,
                                        @Param("accountCodeLike") String accountCodeLike,
                                        @Param("accountNameLike") String accountNameLike,
                                        @Param("onlyWithBalance") Boolean onlyWithBalance);

    /**
     * 批量插入余额
     *
     * @param balances 余额列表
     * @return 插入数量
     */
    int batchInsert(@Param("balances") List<GlBalance> balances);

    /**
     * 更新余额
     *
     * @param balance 余额信息
     * @return 更新数量
     */
    int updateBalance(@Param("balance") GlBalance balance);
}
