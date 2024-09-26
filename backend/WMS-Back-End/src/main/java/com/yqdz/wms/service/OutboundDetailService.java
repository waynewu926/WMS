package com.yqdz.wms.service;

import com.yqdz.wms.model.domain.InboundDetail;
import com.yqdz.wms.model.domain.OutboundDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqdz.wms.model.dto.InboundDetailUpdateRequest;
import com.yqdz.wms.model.dto.OutboundDetailUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Galen
* @description 针对表【outound_detail(出库单明细表)】的数据库操作Service
* @createDate 2024-06-26 14:32:08
*/
public interface OutboundDetailService extends IService<OutboundDetail> {
    List<OutboundDetail> findOutboundDetail(String outboundNum);

    @Transactional
    void updateOutboundDetail(OutboundDetailUpdateRequest request);
}
