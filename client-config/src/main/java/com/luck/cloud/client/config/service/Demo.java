package com.luck.cloud.client.config.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created by Hua wb on 2018/5/11.
 */
@RestController
public class Demo {
    @Resource
    private RestTemplate restTemplate;

    @Value("${server.port}")
    String port;
    @Value("${luck}")
    String luck;

    @RequestMapping("/hi")
    public String hi(@RequestParam String name) {
        return "hi " + name + ",i am from port:" + port + ",config name:" + luck;
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
