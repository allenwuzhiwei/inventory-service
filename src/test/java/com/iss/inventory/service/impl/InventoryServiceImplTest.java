package com.iss.inventory.service.impl;

import com.iss.inventory.entity.Inventory;
import com.iss.inventory.entity.InventoryLog;
import com.iss.inventory.mapper.InventoryLogMapper;
import com.iss.inventory.mapper.InventoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private InventoryLogMapper inventoryLogMapper;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInventoryByProductId_Found() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        when(inventoryMapper.selectOne(any())).thenReturn(inventory);

        Inventory result = inventoryService.getInventoryByProductId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
    }

    @Test
    void testAddInventory_Success() {
        Inventory inventory = new Inventory();
        when(inventoryMapper.insert(ArgumentMatchers.<Inventory>any())).thenReturn(1);

        boolean result = inventoryService.addInventory(inventory);
        assertTrue(result);
    }

    @Test
    void testUpdateInventory_Success() {
        Inventory inventory = new Inventory();
        when(inventoryMapper.updateById(ArgumentMatchers.<Inventory>any())).thenReturn(1);

        boolean result = inventoryService.updateInventory(inventory);
        assertTrue(result);
    }

    @Test
    void testDeleteInventory_Success() {
        when(inventoryMapper.delete(any())).thenReturn(1);

        boolean result = inventoryService.deleteInventory(1L);
        assertTrue(result);
    }

    @Test
    void testDeductInventory_Success() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantityAvailable(10);

        when(inventoryMapper.selectOne(any())).thenReturn(inventory);
        when(inventoryMapper.updateById(ArgumentMatchers.<Inventory>any())).thenReturn(1);
        when(inventoryLogMapper.insert(ArgumentMatchers.<InventoryLog>any())).thenReturn(1);

        boolean result = inventoryService.deductInventory(1L, 5, "test-user");
        assertTrue(result);
        assertEquals(5, inventory.getQuantityAvailable());
    }

    @Test
    void testDeductInventory_InsufficientStock() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantityAvailable(3);

        when(inventoryMapper.selectOne(any())).thenReturn(inventory);

        boolean result = inventoryService.deductInventory(1L, 5, "test-user");
        assertFalse(result);
    }

    @Test
    void testDeductInventory_NotFound() {
        when(inventoryMapper.selectOne(any())).thenReturn(null);

        boolean result = inventoryService.deductInventory(1L, 1, "test-user");
        assertFalse(result);
    }

    @Test
    void testRestoreInventory_Success() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantityAvailable(10);

        when(inventoryMapper.selectOne(any())).thenReturn(inventory);
        when(inventoryMapper.updateById(ArgumentMatchers.<Inventory>any())).thenReturn(1);
        when(inventoryLogMapper.insert(ArgumentMatchers.<InventoryLog>any())).thenReturn(1);

        boolean result = inventoryService.restoreInventory(1L, 5, "admin", "cancel");
        assertTrue(result);
        assertEquals(15, inventory.getQuantityAvailable());
    }

    @Test
    void testRestoreInventory_NotFound() {
        when(inventoryMapper.selectOne(any())).thenReturn(null);

        boolean result = inventoryService.restoreInventory(1L, 5, "admin", "cancel");
        assertFalse(result);
    }

    @Test
    void testGetLogsByProductId_Success() {
        InventoryLog log = new InventoryLog();
        log.setProductId(1L);

        when(inventoryLogMapper.selectList(any())).thenReturn(Collections.singletonList(log));

        List<InventoryLog> result = inventoryService.getLogsByProductId(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProductId());
    }
}
