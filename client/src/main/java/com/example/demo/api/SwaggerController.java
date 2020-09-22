package com.example.demo.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Hua wb on 2018/5/11.
 */
@EnableEurekaClient
@RestController
@Api(tags = "test")
public class SwaggerController {
    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String hi(@RequestParam String name) {
        return "hi " + name + ",i am from port:" + port;
    }

    @RequestMapping("/home")
    public String home() {
        return "hi,i am from port:" + port;
    }

    @ApiOperation(value = "获取otp", notes="通过手机号获取OTP验证码")
    @ApiImplicitParam(name = "telephone", value = "电话号码", paramType = "query", required = true, dataType = "Integer")
    @RequestMapping(value = "getotp", method= RequestMethod.GET)
    @ResponseBody
    public String getOtp(@RequestParam(name = "telephone", required = false) String telphone) {
        return "hello world";
    }
}
