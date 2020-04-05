package com.suyh.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.suyh.inner.LoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 登录过滤拦截器
 * <p>
 * 所有的访问先到这里来，检查是否需要登录权限。
 * 需要登录权限的就检查是否有登录TOKEN
 * 不需要登录权限的直接放行
 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private LoginController loginController;

    /**
     * 网关发生时机为预处理阶段
     *
     * @return 拦截器发生的时机
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * @return true: 直接通过，false: 执行shouldFilter() 方法
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        System.out.println("URI: " + request.getRequestURI());
        System.out.println("URL: " + request.getRequestURL());

        // 如果是登录请求，则放行
        if ("/login".equals(request.getRequestURI())) {
            return false;
        }

        // 其他的所有都需要拦截，执行run() 方法
        return true;
    }

    /**
     * shouldFilter() 返回true 的处理逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("拦截器");

        RequestContext requestContext = RequestContext.getCurrentContext();

        // 这个应该是获取请求体数据吧。
        String body = requestContext.getResponseBody();
        System.out.println("request body: " + body);

        HttpServletRequest request = requestContext.getRequest();

        // 获取请求头中的值
        String token = request.getHeader("token");

        // 登录校验逻辑
        if (!"tokenValue".equals(token)) {
            // JWT springboot 技术，可以用在这个地方
            // 网关校验失败，响应结果。
            requestContext.setSendZuulResponse(false);  // 这个值被置为false 了就说明被拦截禁止了。
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

            requestContext.setResponseBody("suyh response body: no login, access failed");
        }

        // 登录校验通过，直接放行
        return null;
    }
}
