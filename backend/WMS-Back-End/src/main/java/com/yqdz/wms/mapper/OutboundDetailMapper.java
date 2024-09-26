package com.yqdz.wms.mapper;

import com.yqdz.wms.model.domain.InboundDetail;
import com.yqdz.wms.model.domain.OutboundDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author Galen
* @description 针对表【outound_detail(出库单明细表)】的数据库操作Mapper
* @createDate 2024-06-26 14:32:08
* @Entity com.yqdz.wms.model.domain.OutoundDetail
*/
public interface OutboundDetailMapper extends BaseMapper<OutboundDetail> {
    @Update("UPDATE outbound_detail SET real_quantity = #{realQuantity}, item_status = #{itemStatus} " +
            "WHERE outbound_num = #{outboundNum} AND item_num = #{itemNum}")
    void updateOutboundDetail(@Param("outboundNum") String outboundNum,
                             @Param("itemNum") String itemNum,
                             @Param("realQuantity") int realQuantity,
                             @Param("itemStatus") int itemStatus);

    @Select("SELECT * FROM outbound_detail WHERE outbound_num = #{outboundNum} AND item_num = #{itemNum}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "outbound_num", property = "outboundNum"),
            @Result(column = "item_num", property = "itemNum"),
            @Result(column = "packaging_capacity", property = "packagingCapacity"),
            @Result(column = "plan_quantity", property = "planQuantity"),
            @Result(column = "real_quantity", property = "realQuantity"),
            @Result(column = "item_status", property = "itemStatus"),
            @Result(column = "created_by", property = "createdBy"),
            @Result(column = "created_time", property = "createdTime"),
            @Result(column = "updated_by", property = "updatedBy"),
            @Result(column = "updated_time", property = "updatedTime")
    })
    OutboundDetail selectByOutboundNumAndItemNum(@Param("outboundNum") String outboundNum, @Param("itemNum") String itemNum);

    @Select("SELECT * FROM outbound_detail WHERE outbound_num = #{outboundNum}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "outbound_num", property = "outboundNum"),
            @Result(column = "item_num", property = "itemNum"),
            @Result(column = "packaging_capacity", property = "packagingCapacity"),
            @Result(column = "plan_quantity", property = "planQuantity"),
            @Result(column = "real_quantity", property = "realQuantity"),
            @Result(column = "item_status", property = "itemStatus"),
            @Result(column = "created_by", property = "createdBy"),
            @Result(column = "created_time", property = "createdTime"),
            @Result(column = "updated_by", property = "updatedBy"),
            @Result(column = "updated_time", property = "updatedTime")
    })
    List<OutboundDetail> selectByOutboundNum(@Param("outboundNum") String outboundNum);
}




