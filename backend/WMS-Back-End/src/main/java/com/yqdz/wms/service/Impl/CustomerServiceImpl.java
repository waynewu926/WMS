package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.model.domain.Customer;
import com.yqdz.wms.model.domain.Supplier;
import com.yqdz.wms.service.CustomerService;
import com.yqdz.wms.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Galen
* @description 针对表【customer(客户)】的数据库操作Service实现
* @createDate 2024-06-26 14:31:44
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService{

    @Override
    public List<Customer> findCustomer(String customerName, String customerCode) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        if (customerName != null && !customerName.isEmpty()) {
            queryWrapper.eq("customer_name", customerName);
        }

        if (customerCode != null && !customerCode.isEmpty()) {
            queryWrapper.eq("customer_code", customerCode);
        }
        return this.list(queryWrapper);
    }

}




