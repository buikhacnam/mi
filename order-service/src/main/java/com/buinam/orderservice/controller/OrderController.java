package com.buinam.orderservice.controller;


import com.buinam.orderservice.dto.OrderRequest;
import com.buinam.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "createOrderFallback")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> createOrderFallback(OrderRequest orderRequest, Exception e) {
        return CompletableFuture.supplyAsync(() -> "Oops!!!!!!!! Something went wrong. Please try again later.");
    }
}

/*
inventory is the name of the circuit breaker. This name is used to identify the circuit breaker in the configuration.

resilience4j.circuitbreaker.instances.inventory.waitDurationInOpenState=5s
the state of the circuit breaker is changed from CLOSED to HALF_OPEN after 5 seconds.

resilience4j.circuitbreaker.instances.inventory.failureRateThreshold=50
the failure rate threshold is set to 50%. If the failure rate is greater than 50%, the circuit breaker will change the state from CLOSED to OPEN.

resilience4j.timelimiter.instances.inventory.timeout-duration=3s
the timeout duration is set to 3 seconds. If the execution time of the method is greater than 3 seconds, the circuit breaker will change the state from CLOSED to OPEN.

resilience4j.retry.instances.inventory.max-attempts=3
the max attempts is set to 3. If the method fails 3 times, the circuit breaker will change the state from CLOSED to OPEN.

resilience4j.retry.instances.inventory.wait-duration=5s
the wait duration is set to 5 seconds. The method will be retried after 5 seconds.



*/