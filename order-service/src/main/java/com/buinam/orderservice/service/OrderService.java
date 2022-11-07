package com.buinam.orderservice.service;

import com.buinam.orderservice.dto.OrderRequest;
import com.buinam.orderservice.model.Order;
import com.buinam.orderservice.model.OrderLineItems;
import com.buinam.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

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
        order.setOrderLineItemsList(orderLineItemsList);
        orderRepository.save(order);
        log.info("Order placed successfully with order number: " + order.getOrderNumber());
    }
}
