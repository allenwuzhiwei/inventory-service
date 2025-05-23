package com.iss.inventory.service;

import com.iss.inventory.entity.Inventory;
import com.iss.inventory.entity.InventoryLog;

import java.util.List;

/*
 InventoryService 接口
 提供库存管理相关的服务方法，包括库存的 CRUD、扣减、恢复及日志查询等功能
 */
public interface InventoryService {

    /*
     根据 productId 查询对应商品的库存信息
     @param productId 商品 ID
     @return 库存记录对象，若不存在返回 null
     */
    Inventory getInventoryByProductId(Long productId);

    /*
     添加一条新的库存记录
     @param inventory 待添加的库存信息（productId 唯一）
     @return 添加是否成功
     */
    boolean addInventory(Inventory inventory);

    /*
     更新指定商品的库存信息（如仓库位置、补货阈值等）
     @param inventory 修改后的库存对象（必须带 productId）
     @return 是否更新成功
     */
    boolean updateInventory(Inventory inventory);

    /*
     删除指定商品的库存记录
     @param productId 商品 ID
     @return 是否删除成功
     */
    boolean deleteInventory(Long productId);

    /*
     扣减库存（下单场景）与Order微服务进行联动
     若库存不足则返回 false，扣减成功后自动记录日志
     @param productId 商品 ID
     @param quantity 扣减数量
     @param operator 操作人（用于写入日志）
     @return 是否扣减成功
     */
    boolean deductInventory(Long productId, int quantity, String operator);

    /*
     回滚库存（取消订单或失败回退）与Order微服务进行联动
     增加库存并写入日志
     @param productId 商品 ID
     @param quantity 恢复数量
     @param operator 操作人（用于写入日志）
     @param reason 回滚原因备注（写入日志）
     @return 是否恢复成功
     */
    boolean restoreInventory(Long productId, int quantity, String operator, String reason);

    /*
     查询某商品的库存变动日志（inventory_log）
     @param productId 商品 ID
     @return 日志列表（按时间倒序）
     */
    List<InventoryLog> getLogsByProductId(Long productId);
}
