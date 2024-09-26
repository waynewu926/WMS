package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.OutboundDetail;
import com.yqdz.wms.model.dto.OutboundDetailUpdateRequest;
import com.yqdz.wms.service.OutboundDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/outboundDetail")
public class OutboundDetailController {
    @Resource
    private OutboundDetailService outboundDetailService;

    @GetMapping("{outboundNum}")
    public List<OutboundDetail> getOutboundDetail(@PathVariable String outboundNum) {
        return outboundDetailService.findOutboundDetail(outboundNum);
    }

    @PutMapping()
    public void updateOutboundDetail(@RequestBody OutboundDetailUpdateRequest request) {
        outboundDetailService.updateOutboundDetail(request);
    }
}
