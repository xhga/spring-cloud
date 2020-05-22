### server eureka注册中心
     访问地址：http://localhost:8761/eureka/
     
### client 服务消费
     访问地址: http://localhost:8762/home, http://localhost:8762/hi?name=a
### client2 服务消费
     访问地址: http://localhost:8763/home, http://localhost:8763/hi?name=a
### client-ribbon 负载均衡1
     访问地址: http://localhost:8764/hi
### client-feign  负载均衡2
     访问地址：http://localhost:8766/hi
### client-zuul   路由网关
     访问地址：http://localhost:8765/hi

### config-server 配置服务中心
    端口: 8771

### config-client 配置客户端
    访问地址: http://localhost:8772/hi

### config-client-bus 配置客户端(动端更改配置)
    访问地址: http://localhost:8773/hi
    
### client-config  服务消费+配置客户端
    访问地址: http://localhost:8767/hi
   
