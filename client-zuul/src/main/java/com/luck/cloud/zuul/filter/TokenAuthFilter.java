package com.luck.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * token过滤器
 */
@Component
public class TokenAuthFilter extends ZuulFilter {
    private static final String AUTH_TOKEN = "token";

    @Override
    public String filterType() {
        // 1、pre：在请求发出之前执行过滤，如果要进行访问，肯定在请求前设置头信息
        // 2、route：在进行路由请求的时候被调用；
        // 3、post：在路由之后发送请求信息的时候被调用；
        // 4、error：出现错误之后进行调用
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;  // 设置优先级
    }

    @Override
    public boolean shouldFilter() {
        return true; // 进入拦截器
    }

    private boolean checkToken(String token){
        return "token".equals(token);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");

        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String token = request.getHeader(AUTH_TOKEN);
        if(this.checkToken(token)){
            String uuidToken = UUID.randomUUID().toString();
            response.setHeader("Access-Control-Expose-Headers",
                    "Cache-Control,Content-Type,Expires,Pragma,Content-Language,Last-Modified,token");
            response.setHeader("token", uuidToken);
            response.setStatus(HttpStatus.SC_OK);
        } else {
            //不存在，直接返回验证失败，让其重新登录
            requestContext.setSendZuulResponse(false);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            requestContext.setResponseBody("token验证失败!");
        }
        return null;
    }
}
