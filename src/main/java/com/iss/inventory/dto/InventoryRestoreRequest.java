package com.iss.inventory.dto;

import lombok.Data;

/*
 恢复库存请求 DTO
 */
@Data
public class InventoryRestoreRequest {
    private Long productId;
    private Integer quantity;
    private String operator;
    private String reason;
}
