package com.iss.inventory.dto;

import lombok.Data;

/*
 扣减库存请求 DTO
 */
@Data
public class InventoryChangeRequest {
    private Long productId;
    private Integer quantity;
    private String operator;
}
