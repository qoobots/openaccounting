package com.qoobot.openaccounting.ledger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.ledger.dto.AccountCreateRequest;
import com.qoobot.openaccounting.ledger.dto.AccountQueryRequest;
import com.qoobot.openaccounting.ledger.dto.AccountUpdateRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.vo.AccountVO;

import java.util.List;

/**
 * 会计科目Service
 *
 * @author openaccounting
 */
public interface GlAccountService extends IService<GlAccount> {

    /**
     * 创建科目
     *
     * @param request 创建请求
     * @return 科目ID
     */
    Long createAccount(AccountCreateRequest request);

    /**
     * 更新科目
     *
     * @param id 科目ID
     * @param request 更新请求
     */
    void updateAccount(Long id, AccountUpdateRequest request);

    /**
     * 删除科目
     *
     * @param id 科目ID
     */
    void deleteAccount(Long id);

    /**
     * 批量删除科目
     *
     * @param ids 科目ID列表
     */
    void batchDeleteAccounts(List<Long> ids);

    /**
     * 根据ID查询科目
     *
     * @param id 科目ID
     * @return 科目信息
     */
    AccountVO getAccountById(Long id);

    /**
     * 分页查询科目列表
     *
     * @param request 查询请求
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 科目列表
     */
    List<AccountVO> listAccounts(AccountQueryRequest request, Integer pageNum, Integer pageSize);

    /**
     * 查询科目总数
     *
     * @param request 查询请求
     * @return 总数
     */
    Long countAccounts(AccountQueryRequest request);

    /**
     * 查询科目树
     *
     * @param companyId 公司ID
     * @param status 状态
     * @return 科目树
     */
    List<AccountVO> getAccountTree(Long companyId, Integer status);

    /**
     * 启用/停用科目
     *
     * @param id 科目ID
     * @param status 状态: 0-启用, 1-停用
     */
    void updateAccountStatus(Long id, Integer status);

    /**
     * 检查科目编码是否存在
     *
     * @param accountCode 科目编码
     * @param excludeId 排除的ID(更新时使用)
     * @return 是否存在
     */
    boolean checkAccountCodeExists(String accountCode, Long excludeId);

    /**
     * 获取所有叶子节点科目
     *
     * @param companyId 公司ID
     * @param status 状态
     * @return 叶子节点科目列表
     */
    List<AccountVO> getLeafAccounts(Long companyId, Integer status);
}
