package com.yqdz.wms.mapper;

import com.yqdz.wms.model.domain.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
* @author Galen
* @description 针对表【inventory(库存表)】的数据库操作Mapper
* @createDate 2024-06-26 14:31:54
* @Entity com.yqdz.wms.model.domain.Inventory
*/
public interface InventoryMapper extends BaseMapper<Inventory> {
    @Select("SELECT * FROM inventory WHERE item_num = #{itemNum}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "item_num", property = "itemNum"),
        @Result(column = "quantity", property = "quantity"),
        @Result(column = "created_by", property = "createdBy"),
        @Result(column = "created_time", property = "createdTime"),
        @Result(column = "updated_by", property = "updatedBy"),
        @Result(column = "updated_time", property = "updatedTime")
})
Inventory selectByItemNum(@Param("itemNum") String itemNum);

}




