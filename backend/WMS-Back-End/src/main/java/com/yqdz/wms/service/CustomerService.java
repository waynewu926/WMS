package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqdz.wms.model.domain.Supplier;

import java.util.List;

/**
* @author Galen
* @description 针对表【customer(客户)】的数据库操作Service
* @createDate 2024-06-26 14:31:44
*/
public interface CustomerService extends IService<Customer> {

    List<Customer> findCustomer(String customerName, String customerCode);

}
