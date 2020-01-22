package com.suyh.service.fallback;

import com.suyh.service.ProductClient;
import org.springframework.stereotype.Component;

/**
 * 添加对product-service 调用的降级处理
 * 比如product-service 服务挂了。
 *
 * 这里还要注意的是：这个类必须要给到spring 扫描到，所以必须要加上@component 的注解
 */
@Component
public class ProductClientFallback implements ProductClient {
    @Override
    public String findById(int id) {
        // 一般情况我们都是做一个日志处理就好了。然后返回一个null
        // 这个null 值会正常给到调用API 的返回值，那么这个调用基本都会出现nul　异常。
        // 这样就会走到熔煅处理那里去了
        System.out.println("调用product service 异常");
        return null;
    }
}
