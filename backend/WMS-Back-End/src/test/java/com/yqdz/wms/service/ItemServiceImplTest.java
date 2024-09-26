package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.Item;
import com.yqdz.wms.model.domain.Supplier;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class ItemServiceImplTest {
    @Resource
    private SupplierService supplierService;
    @Resource
    private ItemService itemService;

    @Test
    void testGetItems() throws Exception {
//
//        Supplier supplier = new Supplier();
//        supplier.setSupplierCode("3FC");
//        supplier.setSupplierName("banana");
//        supplier.setCreatedBy("zya");
//        supplier.setCreatedTime(new Date());
//        supplier.setUpdatedBy("zya");
//        supplier.setUpdatedTime(new Date());
//        supplierService.save(supplier);
//

        Item item = new Item();
        item.setItemNum("2516");
        item.setItemSerial("963548");
        item.setSupplier("3NC");
        item.setCreatedBy("zya");
        item.setCreatedTime(new Date());
        item.setUpdatedBy("zya");
        item.setUpdatedTime(new Date());
        itemService.save(item);

    }
}
