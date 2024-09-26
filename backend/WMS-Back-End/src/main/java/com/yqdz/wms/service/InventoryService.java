package com.yqdz.wms.service;


import com.yqdz.wms.model.domain.Inventory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Galen
* @description 针对表【inventory(库存表)】的数据库操作Service
* @createDate 2024-06-26 14:31:54
*/
public interface InventoryService extends IService<Inventory> {
    List<Inventory> findInventory(String itemNum);

}
