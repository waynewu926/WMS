package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.Outbound;
import com.yqdz.wms.model.dto.OutboundCreateRequest;
import com.yqdz.wms.service.OutboundService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/outbound")
public class OutboundController {
    @Resource
    private OutboundService outboundService;

    @PostMapping()
    public void createOutbound(@RequestBody OutboundCreateRequest request) {
        if(request != null){
            boolean result = outboundService.createOutbound(request);
        }
    }

    @GetMapping()
    public List<Outbound> getOutbound(
            @RequestParam(required = false) String outboundStatus,
            @RequestParam(required = false) String outboundNum,
            @RequestParam(required = false) String customer) {
        return outboundService.findOutbound(outboundStatus, outboundNum, customer);
    }

    @DeleteMapping("{id}")
    public void deleteOutbound(@PathVariable Integer id) {
        boolean result = outboundService.deleteOutbound(id);
    }

}
