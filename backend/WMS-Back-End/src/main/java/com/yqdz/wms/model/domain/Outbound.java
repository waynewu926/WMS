package com.yqdz.wms.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 出库单
 * @TableName outbound
 */
@TableName(value ="outbound")
@Data
public class Outbound implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 出库单号
     */
    @TableField(value = "outbound_num")
    private String outboundNum;

    /**
     * 客户
     */
    @TableField(value = "customer")
    private String customer;

    /**
     * 出库单状态（0--已完成、1--部分完成、2--未出库）
     */
    @TableField(value = "outbound_status")
    private Integer outboundStatus;

    /**
     * 创建人
     */
    @TableField(value = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
