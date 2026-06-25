package com.qoobot.openaccounting.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.asset.dto.InventoryCreateRequest;
import com.qoobot.openaccounting.asset.entity.FaInventory;
import com.qoobot.openaccounting.asset.vo.InventoryVO;

import java.util.List;

/**
 * 盘点Service接口
 *
 * @author openaccounting
 */
public interface FaInventoryService extends IService<FaInventory> {

    /**
     * 创建盘点单
     *
     * @param request 创建请求
     * @param userId 操作人ID
     * @param username 操作人
     * @return 盘点ID
     */
    Long createInventory(InventoryCreateRequest request, Long userId, String username);

    /**
     * 开始盘点
     *
     * @param inventoryId 盘点ID
     * @param detailRequests 盘点明细
     */
    void startInventory(Long inventoryId, List<com.qoobot.openaccounting.asset.dto.InventoryDetailRequest> detailRequests);

    /**
     * 完成盘点
     *
     * @param inventoryId 盘点ID
     */
    void completeInventory(Long inventoryId);

    /**
     * 查询盘点详情
     *
     * @param inventoryId 盘点ID
     * @return 盘点详情
     */
    InventoryVO getInventoryDetail(Long inventoryId);
}
