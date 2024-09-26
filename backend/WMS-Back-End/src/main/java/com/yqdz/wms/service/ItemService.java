package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.Item;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Galen
* @description 针对表【item(零件表)】的数据库操作Service
* @createDate 2024-06-21 13:09:39
*/
public interface ItemService extends IService<Item> {
    List<Item> findItems(String itemNum,String supplier,String itemSerial);
}
