package com.yqdz.wms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutboundDetailUpdateRequest {
    private String outboundNum;
    private List<OutboundDetailRequest> outboundDetail;

    @Data
    public static class OutboundDetailRequest {
        private String itemNum;
        private Integer realQuantity;
        private Integer itemStatus;
    }
}