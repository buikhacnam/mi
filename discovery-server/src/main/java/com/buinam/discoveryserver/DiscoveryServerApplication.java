package com.buinam.discoveryserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// check status : http://localhost:8761/
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(DiscoveryServerApplication.class, args);
        }

}
