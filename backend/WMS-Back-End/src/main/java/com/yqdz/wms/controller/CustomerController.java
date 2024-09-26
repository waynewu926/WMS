package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.Customer;
import com.yqdz.wms.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;

    @GetMapping()
    public List<Customer> getCustomers(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerCode
    ) {
        return customerService.findCustomer(customerName,customerCode);
    }
}
