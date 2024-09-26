package com.yqdz.wms.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 入库单
 * @TableName inbound
 */
@TableName(value ="inbound")
@Data
public class Inbound implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 入库单号
     */
    @TableField(value = "inbound_num")
    private String inboundNum;

    /**
     * 供应商
     */
    @TableField(value = "supplier")
    private String supplier;

    /**
     * 入库单状态（0--已完成、1--部分完成、2--未入库）
     */
    @TableField(value = "inbound_status")
    private Integer inboundStatus;

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
