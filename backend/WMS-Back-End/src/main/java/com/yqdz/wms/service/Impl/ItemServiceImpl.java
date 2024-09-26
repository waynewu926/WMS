package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.model.domain.Item;
import com.yqdz.wms.model.domain.Supplier;
import com.yqdz.wms.service.ItemService;
import com.yqdz.wms.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Galen
* @description 针对表【item(零件表)】的数据库操作Service实现
* @createDate 2024-06-21 13:09:39
*/
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item>
    implements ItemService {

    @Override
    public List<Item> findItems(String itemNum,String supplier,String itemSerial) {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        if (itemNum != null && !itemNum.isEmpty()) {
            queryWrapper.eq("item_num", itemNum);
        }
        if (supplier != null && !supplier.isEmpty()) {
            queryWrapper.eq("supplier", supplier);
        }
        if (itemSerial != null && !itemSerial.isEmpty()) {
            queryWrapper.eq("item_serial", itemSerial);
        }
        return this.list(queryWrapper);
    }
}




