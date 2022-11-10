package com.buinam.orderservice.service;

import com.buinam.orderservice.dto.InventoryResponse;
import com.buinam.orderservice.dto.OrderRequest;
import com.buinam.orderservice.model.Order;
import com.buinam.orderservice.model.OrderLineItems;
import com.buinam.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(orderLineItemsDto -> {
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
            orderLineItems.setPrice(orderLineItemsDto.getPrice());
            orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
            return orderLineItems;
        }).collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItemsList); // save t_order_line_items in order (@OneToMany)

        // call inventory-service to check if the product is available

        // collect all skucodes from orderLineItemsList
        List<String> skuCodes = orderLineItemsList.stream().map(orderLineItems -> orderLineItems.getSkuCode()).collect(Collectors.toList());

        // call inventory-service to check if the product is available
        InventoryResponse[] inventoryResponse = webClientBuilder.build().get()
                .uri("http://mi-inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        log.info("Response received from inventory-service: " + inventoryResponse);

        // check if all products are available
        boolean isAllProductsAvailable = true;
        for (InventoryResponse each : inventoryResponse) {
            if (!each.isInStock()) {
                isAllProductsAvailable = false;
                break;
            }
        }
        if (isAllProductsAvailable) {
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product is not available");
        }

        log.info("Order placed successfully with order number: " + order.getOrderNumber());
    }
}
