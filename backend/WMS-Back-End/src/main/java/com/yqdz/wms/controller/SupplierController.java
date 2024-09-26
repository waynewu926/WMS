package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.Supplier;
import com.yqdz.wms.service.SupplierService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Resource
    private SupplierService supplierService;

    @GetMapping()
    public List<Supplier> getSuppliers(
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String supplierCode
    ) {
        return supplierService.findSupplier(supplierName,supplierCode);
    }
}
