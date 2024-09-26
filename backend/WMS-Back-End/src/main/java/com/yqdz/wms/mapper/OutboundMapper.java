package com.yqdz.wms.mapper;

import com.yqdz.wms.model.domain.Outbound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author Galen
* @description 针对表【outbound(出库单)】的数据库操作Mapper
* @createDate 2024-06-26 14:32:02
* @Entity com.yqdz.wms.model.domain.Outbound
*/
public interface OutboundMapper extends BaseMapper<Outbound> {
    @Update("UPDATE outbound SET outbound_status = #{status} WHERE outbound_num = #{outboundNum}")
    void updateOutboundStatus(@Param("outboundNum") String outboundNum, @Param("status") int status);

}




