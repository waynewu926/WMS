package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.InboundDetail;
import com.yqdz.wms.model.dto.InboundDetailUpdateRequest;
import com.yqdz.wms.service.InboundDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/inboundDetail")
public class InboundDetailController {
    @Resource
    private InboundDetailService inboundDetailService;

    @GetMapping("{inboundNum}")
    public List<InboundDetail> getInboundDetail(@PathVariable String inboundNum) {
        return inboundDetailService.findInboundDetail(inboundNum);
    }

    @PutMapping()
    public void updateInboundDetail(@RequestBody InboundDetailUpdateRequest request) {
        inboundDetailService.updateInboundDetail(request);
    }
}
