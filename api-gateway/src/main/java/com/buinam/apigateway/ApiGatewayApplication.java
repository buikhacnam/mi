package com.buinam.apigateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(ApiGatewayApplication.class, args);
        }

}
