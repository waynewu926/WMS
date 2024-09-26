package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.Outbound;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqdz.wms.model.dto.OutboundCreateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Galen
* @description 针对表【outbound(出库单)】的数据库操作Service
* @createDate 2024-06-26 14:32:02
*/
public interface OutboundService extends IService<Outbound> {
    @Transactional
    boolean createOutbound(OutboundCreateRequest request);

    List<Outbound> findOutbound(String outboundStatus, String outboundNum, String customer);

    boolean deleteOutbound(Integer id);
}
