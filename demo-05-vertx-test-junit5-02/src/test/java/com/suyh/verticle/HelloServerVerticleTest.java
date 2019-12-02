package com.suyh.verticle;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class HelloServerVerticleTest {

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(HttpServerVerticle.class.getName(), testContext.completing());
    }

    // 重复3 次
    @RepeatedTest(3)
    @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
    // 设定超时时间
    void httpServerCheckResponse(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(8080, "localhost", "/")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    String body = response.body();
                    if (body.equals("Plop")) {
                        testContext.completeNow();
                    } else {
                        testContext.failNow(new RuntimeException("no Plop"));
                    }
                })));
    }
}
