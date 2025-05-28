package com.iss.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iss.inventory.dto.InventoryChangeRequest;
import com.iss.inventory.dto.InventoryRestoreRequest;
import com.iss.inventory.entity.Inventory;
import com.iss.inventory.entity.InventoryLog;
import com.iss.inventory.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetInventory_Success() throws Exception {
        Inventory inv = new Inventory();
        inv.setProductId(1L);
        inv.setQuantityAvailable(100);

        when(inventoryService.getInventoryByProductId(1L)).thenReturn(inv);

        mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1));
    }

    @Test
    void testGetInventory_NotFound() throws Exception {
        when(inventoryService.getInventoryByProductId(99L)).thenReturn(null);

        mockMvc.perform(get("/inventory/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testAddInventory() throws Exception {
        Inventory inv = new Inventory();
        inv.setProductId(2L);
        inv.setQuantityAvailable(50);

        when(inventoryService.addInventory(any())).thenReturn(true);

        mockMvc.perform(post("/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testUpdateInventory() throws Exception {
        Inventory inv = new Inventory();
        inv.setProductId(3L);

        when(inventoryService.updateInventory(any())).thenReturn(true);

        mockMvc.perform(put("/inventory/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testDeleteInventory() throws Exception {
        when(inventoryService.deleteInventory(4L)).thenReturn(true);

        mockMvc.perform(delete("/inventory/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testDeductInventory_Success() throws Exception {
        InventoryChangeRequest request = new InventoryChangeRequest();
        request.setProductId(1L);
        request.setQuantity(5);
        request.setOperator("order-service");
        when(inventoryService.deductInventory(1L, 5, "order-service")).thenReturn(true);

        mockMvc.perform(post("/inventory/deduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testRestoreInventory_Success() throws Exception {
        InventoryRestoreRequest request = new InventoryRestoreRequest();
        request.setProductId(1L);
        request.setQuantity(10);
        request.setOperator("admin");
        request.setReason("cancel");
        when(inventoryService.restoreInventory(1L, 10, "admin", "cancel")).thenReturn(true);

        mockMvc.perform(post("/inventory/restore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testGetLogs() throws Exception {
        when(inventoryService.getLogsByProductId(1L)).thenReturn(Collections.singletonList(new InventoryLog()));

        mockMvc.perform(get("/inventory/logs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetInventoryQuantity() throws Exception {
        Inventory inv = new Inventory();
        inv.setQuantityAvailable(88);
        when(inventoryService.getInventoryByProductId(10L)).thenReturn(inv);

        mockMvc.perform(get("/inventory/quantity/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(88));
    }

    @Test
    void testGetInventoryQuantity_NotFound() throws Exception {
        when(inventoryService.getInventoryByProductId(20L)).thenReturn(null);

        mockMvc.perform(get("/inventory/quantity/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}
