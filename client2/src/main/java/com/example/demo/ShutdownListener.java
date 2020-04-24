package com.example.demo;

import com.netflix.discovery.DiscoveryManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ShutdownListener implements DisposableBean {

    @Override
    public void destroy() {
        System.out.println("服务下线 !!!");
        DiscoveryManager.getInstance().shutdownComponent();
    }
}
