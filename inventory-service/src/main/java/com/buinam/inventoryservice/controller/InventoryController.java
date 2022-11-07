package com.buinam.inventoryservice.controller;

import com.buinam.inventoryservice.dto.InventoryResponse;
import com.buinam.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    // http://localhost:8082/api/inventory?skuCode=iphone_13,iphone13_red
    // http://localhost:8082/api/inventory?skuCode=iphone_13&skuCode=iphone_13_red

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
