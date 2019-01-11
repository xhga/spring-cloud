package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Hua wb on 2018/5/11.
 */
@EnableEurekaClient
@RestController
public class Demo {
    @Value("${server.port}")
    String port;
    @RequestMapping("/hi")
    public String hi(@RequestParam String name) {
        return "hi "+name+",i am from port:" + port;
    }
    @RequestMapping("/home")
    public String home() {
        return "hi,i am from port:" + port;
    }
}
