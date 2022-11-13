package com.buinam.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OrderServiceApplication {

	// check actuator health: http://localhost:8081/actuator/health
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
