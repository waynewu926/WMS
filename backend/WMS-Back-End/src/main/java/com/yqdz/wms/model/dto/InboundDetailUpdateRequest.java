package com.yqdz.wms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class InboundDetailUpdateRequest {
    private String inboundNum;
    private List<InboundDetailRequest> inboundDetail;

    @Data
    public static class InboundDetailRequest {
        private String itemNum;
        private Integer realQuantity;
        private Integer itemStatus;
    }
}
