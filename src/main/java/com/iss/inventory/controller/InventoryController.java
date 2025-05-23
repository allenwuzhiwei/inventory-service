package com.iss.inventory.controller;

import com.iss.inventory.config.ApiResponse;
import com.iss.inventory.dto.InventoryChangeRequest;
import com.iss.inventory.dto.InventoryRestoreRequest;
import com.iss.inventory.entity.Inventory;
import com.iss.inventory.entity.InventoryLog;
import com.iss.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 InventoryController 控制器
 提供库存管理的接口，包括基础 CRUD、库存变动操作及日志查询
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // ========== 基础功能 ==========

    /*
     查询某商品库存信息
     */
    @GetMapping("/{productId}")
    public ApiResponse<Inventory> getInventory(@PathVariable Long productId) {
        Inventory inv = inventoryService.getInventoryByProductId(productId);
        return inv != null ? ApiResponse.success(inv) : ApiResponse.fail("未找到库存记录");
    }

    /*
     添加库存记录
     */
    @PostMapping("/add")
    public ApiResponse<Boolean> addInventory(@RequestBody Inventory inventory) {
        return ApiResponse.success(inventoryService.addInventory(inventory));
    }

    /*
     更新库存记录（如仓库位置、阈值）
     */
    @PutMapping("/update")
    public ApiResponse<Boolean> updateInventory(@RequestBody Inventory inventory) {
        return ApiResponse.success(inventoryService.updateInventory(inventory));
    }

    /*
     删除库存记录
     */
    @DeleteMapping("/{productId}")
    public ApiResponse<Boolean> deleteInventory(@PathVariable Long productId) {
        return ApiResponse.success(inventoryService.deleteInventory(productId));
    }

    // ========== 扣减与恢复 ==========

    /*
     扣减库存（订单调用）
     */
    @PostMapping("/deduct")
    public ApiResponse<Boolean> deduct(@RequestBody InventoryChangeRequest request) {
        boolean result = inventoryService.deductInventory(
                request.getProductId(), request.getQuantity(), request.getOperator());
        return result ? ApiResponse.success(true) : ApiResponse.fail("库存不足或不存在");
    }

    /*
     恢复库存（订单取消）
     */
    @PostMapping("/restore")
    public ApiResponse<Boolean> restore(@RequestBody InventoryRestoreRequest request) {
        boolean result = inventoryService.restoreInventory(
                request.getProductId(), request.getQuantity(), request.getOperator(), request.getReason());
        return result ? ApiResponse.success(true) : ApiResponse.fail("库存记录不存在");
    }

    // ========== 日志查询 ==========

    /*
     查询某商品的库存操作日志
     */
    @GetMapping("/logs/{productId}")
    public ApiResponse<List<InventoryLog>> getLogs(@PathVariable Long productId) {
        return ApiResponse.success(inventoryService.getLogsByProductId(productId));
    }

    // ========== 外部接口调用，用于Product微服务定时同步商品数量 ==========
    /*
     查询某商品的可用库存数量（供外部服务调用）
     */
    @GetMapping("/quantity/{productId}")
    public ApiResponse<Integer> getInventoryQuantity(@PathVariable Long productId) {
        Inventory inv = inventoryService.getInventoryByProductId(productId);
        if (inv == null) {
            return ApiResponse.fail("未找到该商品的库存记录");
        }
        return ApiResponse.success(inv.getQuantityAvailable());
    }

}
