package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.model.domain.Customer;
import com.yqdz.wms.model.domain.Inventory;
import com.yqdz.wms.service.InventoryService;
import com.yqdz.wms.mapper.InventoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Galen
* @description 针对表【inventory(库存表)】的数据库操作Service实现
* @createDate 2024-06-26 14:31:54
*/
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>
    implements InventoryService{
    @Override
    public List<Inventory> findInventory(String itemNum) {
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        if (itemNum != null && !itemNum.isEmpty()) {
            queryWrapper.eq("item_num", itemNum);
        }

        return this.list(queryWrapper);
    }

}




