package com.yqdz.wms.model.dto;

import lombok.Data;
import java.util.List;
import java.io.Serializable;

@Data
public class InboundCreateRequest implements Serializable{

    private static final long serialVersionUID = 1213742546733212L;

    private String inboundNum;
    private String supplier;
    private List<InboundDetailRequest> inboundDetail;

    @Data
    public static class InboundDetailRequest {
        private String itemNum;
        private Integer packagingCapacity;
        private Integer planQuantity;
    }
}
