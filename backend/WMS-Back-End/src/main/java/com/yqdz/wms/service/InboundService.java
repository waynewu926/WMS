package com.yqdz.wms.service;

import com.yqdz.wms.model.dto.InboundCreateRequest;
import com.yqdz.wms.model.domain.Inbound;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Galen
* @description 针对表【inbound(入库单)】的数据库操作Service
* @createDate 2024-06-21 13:09:11
*/
public interface InboundService extends IService<Inbound> {
    @Transactional
    boolean createInbound(InboundCreateRequest request);

    List<Inbound> findInbound(String inboundStatus, String inboundNum, String supplier);

    boolean deleteInbound(Integer id);
}
