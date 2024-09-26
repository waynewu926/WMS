package com.yqdz.wms.mapper;

import com.yqdz.wms.model.domain.Inbound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
* @author Galen
* @description 针对表【inbound(入库单)】的数据库操作Mapper
* @createDate 2024-06-21 13:09:11
* @Entity generator.domain.Inbound
*/
public interface InboundMapper extends BaseMapper<Inbound> {
    @Update("UPDATE inbound SET inbound_status = #{status} WHERE inbound_num = #{inboundNum}")
    void updateInboundStatus(@Param("inboundNum") String inboundNum, @Param("status") int status);

}




