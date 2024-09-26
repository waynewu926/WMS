package com.yqdz.wms.controller;

import com.yqdz.wms.model.domain.Inbound;
import com.yqdz.wms.model.dto.InboundCreateRequest;
import com.yqdz.wms.service.InboundService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/inbound")
public class InboundController {

    @Resource
    private InboundService inboundService;

    @PostMapping()
    public void createInbound(@RequestBody InboundCreateRequest request) {
        if(request != null) {
            boolean result = inboundService.createInbound(request);
        }
    }

    @GetMapping()
    public List<Inbound> getInbound(
            @RequestParam(required = false) String inboundStatus,
            @RequestParam(required = false) String inboundNum,
            @RequestParam(required = false) String supplier) {
        return inboundService.findInbound(inboundStatus, inboundNum, supplier);
    }

    @DeleteMapping("{id}")
    public void deleteInbound(@PathVariable Integer id) {
        boolean result = inboundService.deleteInbound(id);
    }
}
