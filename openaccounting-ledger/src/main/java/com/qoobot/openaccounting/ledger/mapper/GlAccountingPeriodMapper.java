package com.qoobot.openaccounting.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.ledger.entity.GlAccountingPeriod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会计期间Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface GlAccountingPeriodMapper extends BaseMapper<GlAccountingPeriod> {

    /**
     * 根据公司ID查询期间列表
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @return 期间列表
     */
    List<GlAccountingPeriod> listByCompanyAndYear(@Param("companyId") Long companyId,
                                                  @Param("accountingYear") Integer accountingYear);

    /**
     * 根据公司和年度期间查询
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @return 期间信息
     */
    GlAccountingPeriod selectByCompanyAndPeriod(@Param("companyId") Long companyId,
                                                @Param("accountingYear") Integer accountingYear,
                                                @Param("accountingPeriod") Integer accountingPeriod);

    /**
     * 查询当前开启的期间
     *
     * @param companyId 公司ID
     * @return 开启的期间
     */
    GlAccountingPeriod selectCurrentOpenedPeriod(@Param("companyId") Long companyId);

    /**
     * 检查期间是否可关闭
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @return 未审核凭证数量
     */
    Long countUnauditedVouchers(@Param("companyId") Long companyId,
                               @Param("accountingYear") Integer accountingYear,
                               @Param("accountingPeriod") Integer accountingPeriod);
}
