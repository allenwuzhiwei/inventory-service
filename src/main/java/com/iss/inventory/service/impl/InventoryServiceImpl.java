package com.iss.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iss.inventory.entity.Inventory;
import com.iss.inventory.entity.InventoryLog;
import com.iss.inventory.mapper.InventoryLogMapper;
import com.iss.inventory.mapper.InventoryMapper;
import com.iss.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
 InventoryService 实现类
 实现库存模块的所有业务逻辑，包括查询、扣减、恢复及日志记录
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    /*
     根据 productId 查询库存记录
     */
    @Override
    public Inventory getInventoryByProductId(Long productId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        return inventoryMapper.selectOne(wrapper);
    }

    /*
     添加一条库存记录
     */
    @Override
    public boolean addInventory(Inventory inventory) {
        inventory.setCreateDatetime(LocalDateTime.now());
        inventory.setUpdateDatetime(LocalDateTime.now());
        return inventoryMapper.insert(inventory) > 0;
    }

    /*
     更新库存记录
     */
    @Override
    public boolean updateInventory(Inventory inventory) {
        inventory.setUpdateDatetime(LocalDateTime.now());
        return inventoryMapper.updateById(inventory) > 0;
    }

    /*
     删除库存记录（根据 productId 删除）
     */
    @Override
    public boolean deleteInventory(Long productId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        return inventoryMapper.delete(wrapper) > 0;
    }

    /*
     扣减库存（用于下单场景），若库存不足返回 false
     */
    @Override
    public boolean deductInventory(Long productId, int quantity, String operator) {
        Inventory inventory = getInventoryByProductId(productId);
        if (inventory == null || inventory.getQuantityAvailable() < quantity) {
            return false; // 库存不足或不存在
        }

        // 更新库存数量
        inventory.setQuantityAvailable(inventory.getQuantityAvailable() - quantity);
        inventory.setUpdateUser(operator);
        inventory.setUpdateDatetime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        // 写入日志
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setChangeType("OUT");
        log.setQuantityChanged(quantity);
        log.setReferenceNote("订单扣减");
        log.setCreateUser(operator);
        log.setCreateDatetime(LocalDateTime.now());
        inventoryLogMapper.insert(log);

        return true;
    }

    /*
     回滚库存（如订单取消），增加库存并写入日志
     */
    @Override
    public boolean restoreInventory(Long productId, int quantity, String operator, String reason) {
        Inventory inventory = getInventoryByProductId(productId);
        if (inventory == null) {
            return false;
        }

        inventory.setQuantityAvailable(inventory.getQuantityAvailable() + quantity);
        inventory.setUpdateUser(operator);
        inventory.setUpdateDatetime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        // 写入日志
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setChangeType("IN");
        log.setQuantityChanged(quantity);
        log.setReferenceNote(reason);
        log.setCreateUser(operator);
        log.setCreateDatetime(LocalDateTime.now());
        inventoryLogMapper.insert(log);

        return true;
    }

    /*
     查询某商品的库存变动日志
     */
    @Override
    public List<InventoryLog> getLogsByProductId(Long productId) {
        QueryWrapper<InventoryLog> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId).orderByDesc("create_datetime");
        return inventoryLogMapper.selectList(wrapper);
    }
}
