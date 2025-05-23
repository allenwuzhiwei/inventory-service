package com.iss.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.inventory.entity.InventoryLog;
import org.apache.ibatis.annotations.Mapper;

/*
 InventoryLogMapper 接口
 继承 BaseMapper，提供库存变动日志的操作
 */
@Mapper
public interface InventoryLogMapper extends BaseMapper<InventoryLog> {
    // 可扩展按 productId 分组查询日志等功能
}
