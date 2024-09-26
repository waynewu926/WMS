package com.yqdz.wms.mapper;

import com.yqdz.wms.model.domain.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Galen
* @description 针对表【customer(客户)】的数据库操作Mapper
* @createDate 2024-06-26 14:31:44
* @Entity com.yqdz.wms.model.domain.Customer
*/

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}




