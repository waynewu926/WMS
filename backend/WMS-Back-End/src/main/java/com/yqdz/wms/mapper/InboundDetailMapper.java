package com.yqdz.wms.mapper;

import com.yqdz.wms.model.domain.InboundDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author Galen
* @description 针对表【inbound_detail(入库单明细表)】的数据库操作Mapper
* @createDate 2024-06-21 13:09:32
* @Entity generator.domain.InboundDetail
*/
public interface InboundDetailMapper extends BaseMapper<InboundDetail> {

    @Update("UPDATE inbound_detail SET real_quantity = #{realQuantity}, item_status = #{itemStatus} " +
            "WHERE inbound_num = #{inboundNum} AND item_num = #{itemNum}")
    void updateInboundDetail(@Param("inboundNum") String inboundNum,
                             @Param("itemNum") String itemNum,
                             @Param("realQuantity") int realQuantity,
                             @Param("itemStatus") int itemStatus);

    @Select("SELECT * FROM inbound_detail WHERE inbound_num = #{inboundNum} AND item_num = #{itemNum}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "inbound_num", property = "inboundNum"),
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
    InboundDetail selectByInboundNumAndItemNum(@Param("inboundNum") String inboundNum, @Param("itemNum") String itemNum);

    @Select("SELECT * FROM inbound_detail WHERE inbound_num = #{inboundNum}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "inbound_num", property = "inboundNum"),
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
    List<InboundDetail> selectByInboundNum(@Param("inboundNum") String inboundNum);

}




