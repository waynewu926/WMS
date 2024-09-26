package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.model.domain.Supplier;
import com.yqdz.wms.service.SupplierService;
import com.yqdz.wms.mapper.SupplierMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Galen
* @description 针对表【supplier(供应商)】的数据库操作Service实现
* @createDate 2024-06-21 13:09:45
*/
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier>
    implements SupplierService {

    @Override
    public List<Supplier> findSupplier(String supplierName,String supplierCode) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        if (supplierName != null && !supplierName.isEmpty()) {
            queryWrapper.eq("supplier_name", supplierName);
        }

        if (supplierCode != null && !supplierCode.isEmpty()) {
            queryWrapper.eq("supplier_code", supplierCode);
        }
        return this.list(queryWrapper);
    }
}




