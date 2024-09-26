package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.Item;
import org.springframework.web.bind.annotation.*;
import com.yqdz.wms.service.ItemService;


import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Resource
    private ItemService itemService;

    @GetMapping()
    public List<Item> getItems(
            @RequestParam(required = false) String itemNum,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) String itemSerial
    ) {
        return itemService.findItems(itemNum,supplier,itemSerial);
    }

}
