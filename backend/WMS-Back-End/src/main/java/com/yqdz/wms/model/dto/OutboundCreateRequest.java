package com.yqdz.wms.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OutboundCreateRequest implements Serializable {

    private static final long serialVersionUID = 1211535658212L;

    private String outboundNum;
    private String customer;
    private List<OutboundDetailRequest> outboundDetail;

    @Data
    public static class OutboundDetailRequest {
        private String itemNum;
        private Integer packagingCapacity;
        private Integer planQuantity;
    }
}