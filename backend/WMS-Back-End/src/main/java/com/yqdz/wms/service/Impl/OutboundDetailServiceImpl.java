package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.mapper.InventoryMapper;
import com.yqdz.wms.mapper.OutboundMapper;
import com.yqdz.wms.model.domain.InboundDetail;
import com.yqdz.wms.model.domain.Inventory;
import com.yqdz.wms.model.domain.OutboundDetail;
import com.yqdz.wms.model.dto.InboundDetailUpdateRequest;
import com.yqdz.wms.model.dto.OutboundDetailUpdateRequest;
import com.yqdz.wms.service.OutboundDetailService;
import com.yqdz.wms.mapper.OutboundDetailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @author Galen
* @description 针对表【outound_detail(出库单明细表)】的数据库操作Service实现
* @createDate 2024-06-26 14:32:08
*/
@Service
public class OutboundDetailServiceImpl extends ServiceImpl<OutboundDetailMapper, OutboundDetail>
    implements OutboundDetailService {

    @Resource
    OutboundMapper outboundMapper;

    @Resource
    OutboundDetailMapper outboundDetailMapper;

    @Resource
    InventoryMapper inventoryMapper;

    @Override
    public List<OutboundDetail> findOutboundDetail(String outboundNum) {
        QueryWrapper<OutboundDetail> queryWrapper = new QueryWrapper<>();
        if (outboundNum != null && !outboundNum.isEmpty()) {
            queryWrapper.eq("outbound_num", outboundNum);
            return list(queryWrapper);
        } else {
            return null;
        }
    }

    @Override
    public void updateOutboundDetail(OutboundDetailUpdateRequest request) {
        String outboundNum = request.getOutboundNum();
        if(request.getOutboundDetail() != null && !request.getOutboundDetail().isEmpty()){
            for (OutboundDetailUpdateRequest.OutboundDetailRequest detailRequest : request.getOutboundDetail()) {
                String itemNum = detailRequest.getItemNum();
                Integer realQuantity = detailRequest.getRealQuantity();
                Integer itemStatus = detailRequest.getItemStatus();

                OutboundDetail oldDetail = outboundDetailMapper.selectByOutboundNumAndItemNum(outboundNum, itemNum);
                Integer oldRealQuantity = (oldDetail != null && oldDetail.getRealQuantity() != null) ? oldDetail.getRealQuantity() : 0;
                // 更新入库单明细表
                outboundDetailMapper.updateOutboundDetail(outboundNum, itemNum, realQuantity, itemStatus);

                // 更新库存
                Inventory inventory = inventoryMapper.selectByItemNum(itemNum);
                if (inventory != null) {
                    // 库存表中已经存在该item
                    inventory.setQuantity(inventory.getQuantity() - realQuantity + oldRealQuantity);
                    inventory.setUpdatedBy("admin"); // 设置更新人，实际应用中根据具体需求设置
                    inventory.setUpdatedTime(new Date());
                    inventoryMapper.updateById(inventory);
                }
            }
            // 更新出库单状态
            updateOutboundStatus(outboundNum);
        }
    }

    private void updateOutboundStatus(String outboundNum) {
        List<OutboundDetail> outboundDetails = outboundDetailMapper.selectByOutboundNum(outboundNum);
        boolean allZero = true;
        boolean allTwo = true;

        for (OutboundDetail detail : outboundDetails) {
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
        outboundMapper.updateOutboundStatus(outboundNum, newStatus);
    }
}




