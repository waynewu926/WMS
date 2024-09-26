package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.Inbound;
import com.yqdz.wms.model.domain.InboundDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqdz.wms.model.dto.InboundDetailUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Galen
* @description 针对表【inbound_detail(入库单明细表)】的数据库操作Service
* @createDate 2024-06-21 13:09:32
*/
public interface InboundDetailService extends IService<InboundDetail> {
    List<InboundDetail> findInboundDetail(String inboundNum);

    @Transactional
    void updateInboundDetail(InboundDetailUpdateRequest request);
}
