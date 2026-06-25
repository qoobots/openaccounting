package com.qoobot.openaccounting.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.ledger.entity.GlVoucherEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 凭证分录Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface GlVoucherEntryMapper extends BaseMapper<GlVoucherEntry> {

    /**
     * 根据凭证ID查询分录列表
     *
     * @param voucherId 凭证ID
     * @return 分录列表
     */
    List<GlVoucherEntry> listByVoucherId(@Param("voucherId") Long voucherId);

    /**
     * 批量插入分录
     *
     * @param entries 分录列表
     * @return 插入数量
     */
    int batchInsert(@Param("entries") List<GlVoucherEntry> entries);

    /**
     * 根据凭证ID删除分录
     *
     * @param voucherId 凭证ID
     * @return 删除数量
     */
    int deleteByVoucherId(@Param("voucherId") Long voucherId);

    /**
     * 统计科目在凭证中的使用次数
     *
     * @param accountId 科目ID
     * @return 使用次数
     */
    long countByAccountId(@Param("accountId") Long accountId);
}
