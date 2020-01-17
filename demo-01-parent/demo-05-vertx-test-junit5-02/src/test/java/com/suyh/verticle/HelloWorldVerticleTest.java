package com.suyh.verticle;


import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

// 官网: https://vertx.io/docs/vertx-web-client/java/
@ExtendWith(VertxExtension.class)
public class HelloWorldVerticleTest {

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext testContext) throws InterruptedException {
        vertx.deployVerticle(HelloWorldVerticle.class.getName(), testContext.completing());
    }

    @RepeatedTest(1)
    @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
    void some_test(Vertx vertx, VertxTestContext testContext) {
        int port = 8000;

        // 部署vertx 服务
        // testContext.succeeding  部署成功的处理逻辑
        vertx.deployVerticle(new HelloWorldVerticle(), testContext.succeeding(id -> {
            // 部署成功之后添加测试逻辑
            WebClient client = WebClient.create(vertx);
            client.get(port, "localhost", "/")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(response -> testContext.verify(() -> {
                        Assert.assertEquals("响应结果出错",
                                "HelloWorld, I am from port " + port, response.body());
                        testContext.completeNow();
                    })));
        }));
    }

    @Test
    void someTest02(Vertx vertx, VertxTestContext testContext) throws InterruptedException {
        int port = 8000;

        // 部署成功之后添加测试逻辑
        WebClient client = WebClient.create(vertx);
        client.get(port, "localhost", "/")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    Assert.assertEquals("响应结果出错",
                            "HelloWorld, I am from port " + port, response.body());
                    testContext.completeNow();
                })));
    }

    @Test
    @DisplayName("A first test")
    void foo(Vertx vertx, VertxTestContext testContext) {
        // (...)
        testContext.completeNow();
    }
}
