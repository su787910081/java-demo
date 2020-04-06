package com.suyh.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Random;

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

    /**
     * FilterConstants
     * 过滤器类型，前置过滤器
     *     ERROR_TYPE = "error";
     *          在其他阶段发生错误时执行该过滤器。
     *          这个过滤器可以用来添加统一的异常处理，
     *          给客户端返回一个统一的错误响应。
     *     POST_TYPE = "post";
     *          在请求被路由到对应的微服务以后执行，可用来为Response添加HTTP Header、
     *          将微服务的Response发送给客户端等。
     *     PRE_TYPE = "pre";
     *          在请求被路由之前调用，可以使用这种过滤器实现身份验证、
     *          在集群中选择请求的微服务、记录调试Log等。
     *     ROUTE_TYPE = "route";
     *          将请求路由到对应的微服务，用于构建发送给微服务的请求。
     *
     * 一个请求的过程:
     *      HttpRequest  -->
     *      pre filters  -->
     *      routing filters -->
     *      post/error filters -->
     *      HttpResponse
     *
     * @return 拦截器发生的时机
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器优先级，值越小，越优先执行
     *    可以借鉴  org.springframework.cloud.netflix.zuul.filters.support.
     *          FilterConstants.PRE_DECORATION_FILTER_ORDER
     *    这些定义的常量，然后在这之前或者之后取一个整数
     * @return 过滤器优先级，值越小，越优先执行
     */
    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * 指定需要执行该Filter的规则
     * 是否需要执行过滤器的逻辑
     * 我们可以在这里处理一下，有一些请求我们不需要处理过滤器逻辑，
     * 所以在这里我们可以提前过滤掉
     *
     * @return 是否需要执行run() 方法。true: 则执行, false: 则不执行
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
     * 过滤器的业务逻辑
     * 如果shouldFilter（）为true，则将调用此方法。
     * 该方法是ZuulFilter的核心方法
     *
     * @return 当前的实现可以忽略它。
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


        // 这里不应该去调用登录接口，因为这里通过之后Zuul 会自动跳转到对应的微服务的API
        // ResultModel<String> loginRes = loginController.login("suyh", "pwd");

        // 获取请求头中的值
        String token = request.getHeader("token");

        Random random = new Random();
        int index = random.nextInt();
        // 登录校验逻辑
        if (index % 2 == 0) {
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
