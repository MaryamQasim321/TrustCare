package com.example.trustcare;


import jakarta.ws.rs.GET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {
//    private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);
    static {
//    logger.info(LogUtils.info("application loaded"));
    System.out.println("HelloResource constructor called");
}
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from TrustCare API!";
    }
}