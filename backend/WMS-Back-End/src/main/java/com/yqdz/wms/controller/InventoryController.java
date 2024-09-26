package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.Inventory;
import com.yqdz.wms.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @GetMapping()
    public List<Inventory> getInventory(
            @RequestParam(required = false) String itemNum
    ) {
        return inventoryService.findInventory(itemNum);
    }
}
