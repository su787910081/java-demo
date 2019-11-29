package com.suyh;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// 有空的时候去官网好好看一下
// https://vertx.io/docs/vertx-junit5/java/
@ExtendWith(VertxExtension.class)
public class ATest {
    @Test
    void some_test(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new HttpServerVerticle(), testContext.succeeding(id -> {
            WebClient client = WebClient.create(vertx);
            client.get(8080, "localhost", "/")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(response -> testContext.verify(() -> {
//                        Assert.assertThat(response.body()).isEqualTo("Plop");
                        Assert.assertEquals("", "Plop", response.body());
                        testContext.completeNow();
                    })));
        }));
    }
}
