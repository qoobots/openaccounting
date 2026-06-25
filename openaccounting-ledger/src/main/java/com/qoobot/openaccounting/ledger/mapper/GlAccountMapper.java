package com.qoobot.openaccounting.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会计科目Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface GlAccountMapper extends BaseMapper<GlAccount> {

    /**
     * 根据公司ID查询科目列表
     *
     * @param companyId 公司ID
     * @return 科目列表
     */
    List<GlAccount> listByCompanyId(@Param("companyId") Long companyId);

    /**
     * 根据科目编码查询
     *
     * @param accountCode 科目编码
     * @return 科目信息
     */
    GlAccount selectByAccountCode(@Param("accountCode") String accountCode);

    /**
     * 根据父级ID查询子科目
     *
     * @param parentId 父级ID
     * @return 子科目列表
     */
    List<GlAccount> listByParentId(@Param("parentId") Long parentId);

    /**
     * 统计科目下是否有子科目
     *
     * @param parentId 父级ID
     * @return 子科目数量
     */
    int countChildren(@Param("parentId") Long parentId);

    /**
     * 统计科目是否被凭证使用
     *
     * @param accountId 科目ID
     * @return 使用数量
     */
    int countUsedInVoucher(@Param("accountId") Long accountId);

    /**
     * 查询科目树
     *
     * @param companyId 公司ID
     * @param status 状态
     * @return 科目树列表
     */
    List<GlAccount> selectAccountTree(@Param("companyId") Long companyId, @Param("status") Integer status);

    /**
     * 批量删除科目
     *
     * @param ids 科目ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 查询所有叶子节点科目
     *
     * @param companyId 公司ID
     * @param status 状态
     * @return 叶子节点科目列表
     */
    List<GlAccount> selectLeafAccounts(@Param("companyId") Long companyId, @Param("status") Integer status);
}
