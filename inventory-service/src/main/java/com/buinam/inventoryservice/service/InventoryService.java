package com.buinam.inventoryservice.service;

import com.buinam.inventoryservice.dto.InventoryResponse;
import com.buinam.inventoryservice.repository.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    @SneakyThrows // to avoid try-catch. Not recommended in production
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Checking Inventory...");

        // mimic the delay in inventory-service
//        Thread.sleep(10000); // this will delay the response by 10 seconds which will throw exception cause circuit breaker will timeout after 3 seconds

        log.info("Inventory done checking.");

        List<InventoryResponse> result = inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).collect(Collectors.toList());
        for (InventoryResponse each : result) {
            log.info("Inventory: " + each.isInStock());
        }
        return result;
    }
}
