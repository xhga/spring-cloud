package com.example.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Hua wb on 2018/5/12.
 */
@RestController
public class HelloControler {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/hi")
    @HystrixCommand(fallbackMethod = "hiError")
    public String hi() {
        return hiService();
    }

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService() {
        return restTemplate.getForObject("http://SERVICE-CLIENT/hi?name=" + "我是ribbon！！！", String.class);
    }

    public String hiError() {
        System.out.println("服务异常...");
        return "hi,sorry,error!";
    }

}