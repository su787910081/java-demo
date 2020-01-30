package com.suyh.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class LoginFilter extends ZuulFilter {

    /**
     * FilterConstants
     * 过滤器类型，前置过滤器
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器优先级，值越小，越优先执行
     *    可以借鉴  org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER
     *    这些定义的常量，然后在这之前或者之后取一个整数
     * @return
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    /**
     * 是否需要执行过滤器的逻辑
     * 我们可以在这里处理一下，有一些请求我们不需要处理过滤器逻辑，所以在这里我们可以提前过滤掉
     * @return
     */
    @Override
    public boolean shouldFilter() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        System.out.println("URI: " + request.getRequestURI());
        System.out.println("URL: " + request.getRequestURL());

        // ACL 访问控制列表
        // http://localhost:9000/apigateway/product/api/v1/product/list
        // localhost:9000/apigateway/order/api/v1/order/save?user_id=2&product_id=2
        if ("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())) {
            // 需要拦截，需要执行下面的 run 方法
            return true;
        }

        return false;
    }

    /**
     * 过滤器的业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("拦截器");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String token = request.getHeader("token");

        if (StringUtils.isBlank(token)) {
            // 从GET 参数中取
            token = request.getParameter("token");
        }

        // JWT springboot 技术，可以用在这个地方
        // 登录校验逻辑
        if (StringUtils.isBlank(token)) {
            // 网关校验失败，响应结果。
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
