package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.mapper.InboundMapper;
import com.yqdz.wms.mapper.InventoryMapper;
import com.yqdz.wms.model.domain.InboundDetail;
import com.yqdz.wms.model.domain.Inventory;
import com.yqdz.wms.model.domain.Item;
import com.yqdz.wms.model.dto.InboundDetailUpdateRequest;
import com.yqdz.wms.service.InboundDetailService;
import com.yqdz.wms.mapper.InboundDetailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Galen
 * @description 针对表【inbound_detail(入库单明细表)】的数据库操作Service实现
 * @createDate 2024-06-21 13:09:32
 */
@Service
public class InboundDetailServiceImpl extends ServiceImpl<InboundDetailMapper, InboundDetail>
        implements InboundDetailService {
    @Resource
    private InboundMapper inboundMapper;
    @Resource
    private InboundDetailMapper inboundDetailMapper;
    @Resource
    private InventoryMapper inventoryMapper;

    @Override
    public List<InboundDetail> findInboundDetail(String inboundNum) {
        QueryWrapper<InboundDetail> queryWrapper = new QueryWrapper<>();
        if (inboundNum != null && !inboundNum.isEmpty()) {
            queryWrapper.eq("inbound_num", inboundNum);
            return list(queryWrapper);
        } else {
            return null;
        }
    }

    @Override
    public void updateInboundDetail(InboundDetailUpdateRequest request) {
        String inboundNum = request.getInboundNum();
        if(request.getInboundDetail() != null && !request.getInboundDetail().isEmpty()){
            for (InboundDetailUpdateRequest.InboundDetailRequest detailRequest : request.getInboundDetail()) {
                String itemNum = detailRequest.getItemNum();
                Integer realQuantity = detailRequest.getRealQuantity();
                Integer itemStatus = detailRequest.getItemStatus();

                InboundDetail oldDetail = inboundDetailMapper.selectByInboundNumAndItemNum(inboundNum, itemNum);
                Integer oldRealQuantity = (oldDetail != null && oldDetail.getRealQuantity() != null) ? oldDetail.getRealQuantity() : 0;
                // 更新入库单明细表
                inboundDetailMapper.updateInboundDetail(inboundNum, itemNum, realQuantity, itemStatus);

                // 更新库存
                Inventory inventory = inventoryMapper.selectByItemNum(itemNum);
                if (inventory != null) {
                    // 库存表中已经存在该item
                    inventory.setQuantity(inventory.getQuantity() + realQuantity - oldRealQuantity);
                    inventory.setUpdatedBy("admin"); // 设置更新人，实际应用中根据具体需求设置
                    inventory.setUpdatedTime(new Date());
                    inventoryMapper.updateById(inventory);
                } else {
                    // 库存表中不存在该item，新建一条记录
                    Inventory newInventory = new Inventory();
                    newInventory.setItemNum(itemNum);
                    newInventory.setQuantity(realQuantity);
                    newInventory.setCreatedBy("admin"); // 设置创建人，实际应用中根据具体需求设置
                    newInventory.setCreatedTime(new Date());
                    newInventory.setUpdatedBy("admin"); // 设置更新人，实际应用中根据具体需求设置
                    newInventory.setUpdatedTime(new Date());
                    inventoryMapper.insert(newInventory);
                }
            }
            // 更新入库单状态
            updateInboundStatus(inboundNum);
        }
    }

    private void updateInboundStatus(String inboundNum) {
        List<InboundDetail> inboundDetails = inboundDetailMapper.selectByInboundNum(inboundNum);
        boolean allZero = true;
        boolean allTwo = true;

        for (InboundDetail detail : inboundDetails) {
            if (detail.getItemStatus() != 0) {
                allZero = false;
            }
            if (detail.getItemStatus() != 2) {
                allTwo = false;
            }
        }

        int newStatus;
        if (allZero) {
            newStatus = 0;
        } else if (allTwo) {
            newStatus = 2;
        } else {
            newStatus = 1;
        }
        inboundMapper.updateInboundStatus(inboundNum, newStatus);
    }
}




