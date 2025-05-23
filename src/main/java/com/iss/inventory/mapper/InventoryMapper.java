package com.iss.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.inventory.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;

/*
 InventoryMapper 接口
 继承 BaseMapper，自动提供库存主表的基本 CRUD 操作
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    // (如果之后有需要可扩展自定义 SQL，如查询库存不足商品等)
}
