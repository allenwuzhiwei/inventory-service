package com.iss.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/*
 InventoryLog 实体类，用于记录每一次库存的增减记录
 */
@Data
@TableName("inventory_log")
public class InventoryLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private Long productId; // 商品 ID

    @TableField("change_type")
    private String changeType; // 变动类型：IN（补货）/ OUT（扣减）

    @TableField("quantity_changed")
    private Integer quantityChanged; // 改变数量（可正可负）

    @TableField("reference_note")
    private String referenceNote; // 引用备注，如“订单扣减”、“补货单”

    @TableField("create_user")
    private String createUser;

    @TableField("create_datetime")
    private LocalDateTime createDatetime;
}
