package com.example.demo;


import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Hua wb on 2018/5/11.
 */
@EnableEurekaClient
@RestController
public class Demo {
    @Resource
    private RestTemplate restTemplate;

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String hi(@RequestParam String name) {
        String returnS = "hi " + name + ",i am from port:" + port;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        System.out.println(dateTimeFormatter.format(LocalDateTime.now()) + ":" + returnS);
        return returnS;
    }

    @RequestMapping("/hiClient1")
    public String hi2(@RequestParam String name) {
        return restTemplate.getForObject("http://SERVICE-CLIEN/hi?name=" + name, String.class);
    }

    @RequestMapping("/home")
    public String home() {
        return "hi,i am from port:" + port;
    }

}
