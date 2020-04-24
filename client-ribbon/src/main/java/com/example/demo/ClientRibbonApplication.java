package com.example.demo;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker  // 熔断器
@EnableHystrix
@EnableDiscoveryClient
public class ClientRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
        // 开启负载均衡
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 负载均衡策略，默认轮询
	/*@Bean
	public IRule ribbonRule() {
		return new RandomRule(); // 随机
	}*/

    @Bean
    public IRule ribbonRule() {
        return new RoundRobinRule(); // 轮询
    }
}
