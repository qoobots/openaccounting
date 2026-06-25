package com.qoobot.openaccounting.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.asset.dto.InventoryCreateRequest;
import com.qoobot.openaccounting.asset.dto.InventoryDetailRequest;
import com.qoobot.openaccounting.asset.entity.FaAsset;
import com.qoobot.openaccounting.asset.entity.FaInventory;
import com.qoobot.openaccounting.asset.entity.FaInventoryDetail;
import com.qoobot.openaccounting.asset.mapper.FaAssetMapper;
import com.qoobot.openaccounting.asset.mapper.FaInventoryDetailMapper;
import com.qoobot.openaccounting.asset.mapper.FaInventoryMapper;
import com.qoobot.openaccounting.asset.service.FaInventoryService;
import com.qoobot.openaccounting.asset.vo.InventoryDetailVO;
import com.qoobot.openaccounting.asset.vo.InventoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 盘点Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class FaInventoryServiceImpl extends ServiceImpl<FaInventoryMapper, FaInventory> implements FaInventoryService {

    private final FaInventoryDetailMapper inventoryDetailMapper;
    private final FaAssetMapper assetMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createInventory(InventoryCreateRequest request, Long userId, String username) {
        // 生成盘点单号
        String inventoryNo = generateInventoryNo(request.getInventoryYear(), request.getInventoryPeriod());

        // 创建盘点单
        FaInventory inventory = new FaInventory();
        inventory.setCompanyId(request.getCompanyId());
        inventory.setInventoryNo(inventoryNo);
        inventory.setInventoryYear(request.getInventoryYear());
        inventory.setInventoryPeriod(request.getInventoryPeriod());
        inventory.setInventoryDate(request.getInventoryDate());
        inventory.setInventoryUserId(userId);
        inventory.setInventoryUserName(username);
        inventory.setRemark(request.getRemark());

        save(inventory);

        return inventory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startInventory(Long inventoryId, List<InventoryDetailRequest> detailRequests) {
        // 查询盘点单
        FaInventory inventory = getById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("盘点单不存在");
        }

        // 创建盘点明细
        List<FaInventoryDetail> details = new ArrayList<>();
        for (InventoryDetailRequest detailRequest : detailRequests) {
            // 查询资产信息
            FaAsset asset = assetMapper.selectById(detailRequest.getAssetId());
            if (asset == null) {
                continue;
            }

            // 计算盘点结果
            Integer bookQuantity = 1;
            Integer actualQuantity = detailRequest.getActualQuantity() != null ? detailRequest.getActualQuantity() : 1;
            Integer profitQuantity = actualQuantity > bookQuantity ? actualQuantity - bookQuantity : 0;
            Integer lossQuantity = actualQuantity < bookQuantity ? bookQuantity - actualQuantity : 0;

            BigDecimal bookValue = asset.getNetValue();
            BigDecimal actualValue = detailRequest.getActualValue() != null ? detailRequest.getActualValue() : bookValue;
            BigDecimal profitAmount = profitQuantity > 0 ? asset.getNetValue() : BigDecimal.ZERO;
            BigDecimal lossAmount = lossQuantity > 0 ? asset.getNetValue() : BigDecimal.ZERO;

            String inventoryResult;
            if (profitQuantity > 0) {
                inventoryResult = "盘盈";
            } else if (lossQuantity > 0) {
                inventoryResult = "盘亏";
            } else {
                inventoryResult = "正常";
            }

            FaInventoryDetail detail = new FaInventoryDetail();
            detail.setInventoryId(inventoryId);
            detail.setAssetId(detailRequest.getAssetId());
            detail.setAssetCode(asset.getAssetCode());
            detail.setAssetName(asset.getAssetName());
            detail.setBookQuantity(bookQuantity);
            detail.setActualQuantity(actualQuantity);
            detail.setProfitQuantity(profitQuantity);
            detail.setLossQuantity(lossQuantity);
            detail.setBookValue(bookValue);
            detail.setActualValue(actualValue);
            detail.setProfitAmount(profitAmount);
            detail.setLossAmount(lossAmount);
            detail.setInventoryResult(inventoryResult);
            detail.setRemark(detailRequest.getRemark());

            details.add(detail);
        }

        if (!details.isEmpty()) {
            for (FaInventoryDetail detail : details) {
                inventoryDetailMapper.insert(detail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeInventory(Long inventoryId) {
        // 查询盘点单
        FaInventory inventory = getById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("盘点单不存在");
        }

        // 查询盘点明细
        LambdaQueryWrapper<FaInventoryDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FaInventoryDetail::getInventoryId, inventoryId);
        List<FaInventoryDetail> details = inventoryDetailMapper.selectList(wrapper);

        // 汇总数据
        int normalCount = 0;
        int profitCount = 0;
        int lossCount = 0;
        BigDecimal profitAmount = BigDecimal.ZERO;
        BigDecimal lossAmount = BigDecimal.ZERO;

        for (FaInventoryDetail detail : details) {
            if ("正常".equals(detail.getInventoryResult())) {
                normalCount++;
            } else if ("盘盈".equals(detail.getInventoryResult())) {
                profitCount++;
                profitAmount = profitAmount.add(detail.getProfitAmount());
            } else if ("盘亏".equals(detail.getInventoryResult())) {
                lossCount++;
                lossAmount = lossAmount.add(detail.getLossAmount());
            }
        }

        // 更新盘点单状态
        // TODO: 更新盘点单状态为"已完成"

        // TODO: 生成盘点凭证
    }

    @Override
    public InventoryVO getInventoryDetail(Long inventoryId) {
        // 查询盘点单
        FaInventory inventory = getById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("盘点单不存在");
        }

        // 查询盘点明细
        LambdaQueryWrapper<FaInventoryDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FaInventoryDetail::getInventoryId, inventoryId);
        List<FaInventoryDetail> details = inventoryDetailMapper.selectList(wrapper);

        // 转换为VO
        List<InventoryDetailVO> detailVOs = details.stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());

        // 汇总数据
        int totalAssets = details.size();
        int normalCount = (int) details.stream().filter(d -> "正常".equals(d.getInventoryResult())).count();
        int profitCount = (int) details.stream().filter(d -> "盘盈".equals(d.getInventoryResult())).count();
        int lossCount = (int) details.stream().filter(d -> "盘亏".equals(d.getInventoryResult())).count();
        BigDecimal profitAmountSum = details.stream()
                .map(FaInventoryDetail::getProfitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal lossAmountSum = details.stream()
                .map(FaInventoryDetail::getLossAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return InventoryVO.builder()
                .id(inventory.getId())
                .inventoryNo(inventory.getInventoryNo())
                .inventoryYear(inventory.getInventoryYear())
                .inventoryPeriod(inventory.getInventoryPeriod())
                .inventoryDate(inventory.getInventoryDate())
                .inventoryUserName(inventory.getInventoryUserName())
                .remark(inventory.getRemark())
                .status("进行中")
                .totalAssets(totalAssets)
                .normalCount(normalCount)
                .profitCount(profitCount)
                .lossCount(lossCount)
                .profitAmount(profitAmountSum)
                .lossAmount(lossAmountSum)
                .details(detailVOs)
                .build();
    }

    /**
     * 生成盘点单号
     */
    private String generateInventoryNo(Integer year, Integer period) {
        String yearStr = String.valueOf(year);
        String periodStr = period < 10 ? "0" + period : String.valueOf(period);
        return "PD" + yearStr + periodStr + System.currentTimeMillis() % 10000;
    }

    /**
     * 转换为明细VO
     */
    private InventoryDetailVO convertToDetailVO(FaInventoryDetail detail) {
        return InventoryDetailVO.builder()
                .id(detail.getId())
                .assetId(detail.getAssetId())
                .assetCode(detail.getAssetCode())
                .assetName(detail.getAssetName())
                .bookQuantity(detail.getBookQuantity())
                .actualQuantity(detail.getActualQuantity())
                .profitQuantity(detail.getProfitQuantity())
                .lossQuantity(detail.getLossQuantity())
                .bookValue(detail.getBookValue())
                .actualValue(detail.getActualValue())
                .profitAmount(detail.getProfitAmount())
                .lossAmount(detail.getLossAmount())
                .inventoryResult(detail.getInventoryResult())
                .remark(detail.getRemark())
                .build();
    }
}
