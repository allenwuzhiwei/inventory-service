package com.iss.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/*
 Inventory 实体类, 用于记录每个商品的当前库存信息
 */
@Data
@TableName("inventory") // 指定数据库表名
public class Inventory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private Long productId; // 商品 ID，对应 Product 表主键

    @TableField("quantity_available")
    private Integer quantityAvailable; // 当前可用库存数量

    @TableField("warehouse_location")
    private String warehouseLocation; // 仓库位置

    @TableField("restock_threshold")
    private Integer restockThreshold; // 补货阈值，低于此值可触发预警

    @TableField("create_user")
    private String createUser;

    @TableField("update_user")
    private String updateUser;

    @TableField("create_datetime")
    private LocalDateTime createDatetime;

    @TableField("update_datetime")
    private LocalDateTime updateDatetime;
}
