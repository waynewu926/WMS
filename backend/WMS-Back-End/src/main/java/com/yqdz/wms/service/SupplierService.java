package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Galen
* @description 针对表【supplier(供应商)】的数据库操作Service
* @createDate 2024-06-21 13:09:45
*/
public interface SupplierService extends IService<Supplier> {
//    List<Supplier> findSupplier();

    List<Supplier> findSupplier(String supplierName, String supplierCode);
}
