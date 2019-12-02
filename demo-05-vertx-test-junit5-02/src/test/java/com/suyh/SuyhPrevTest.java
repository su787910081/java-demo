package com.suyh;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * 这个类并没有真正被测试，这里面的代码都只是用来解释说明为什么要用Junit5 的
 */
@ExtendWith(VertxExtension.class)
public class SuyhPrevTest {
    private static final Logger logger = LoggerFactory.getLogger(SuyhPrevTest.class);

    // 反例，用来说明普通的单元测试无法对异步操作做测试
    @Test
    void startServer() {
        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer()
                .requestHandler(req -> req.response().end("OK"))
                .listen(16969, ar -> {
                    logger.debug("listen result: " + ar.result());
                });

    }

    @Test
    public void startHttpServer() throws Throwable {
        VertxTestContext testContext = new VertxTestContext();

        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer()
                .requestHandler(req -> req.response().end())
                .listen(16969, testContext.completing());

        // awaitCompletion 等待完成，5秒超时
        boolean b = testContext.awaitCompletion(5, TimeUnit.SECONDS);
        logger.info("awaitCompletion: " + b);

        if (testContext.failed()) {
            throw testContext.causeOfFailure();
        }
    }

    // 这个也没有实际完成调用，只是用来做说明的
    @Test
    public void test01() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        VertxTestContext testContext = new VertxTestContext();

        WebClient client = WebClient.create(vertx);
        client.get(8080, "localhost", "/")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    String strBody = response.body();
                    logger.debug("body == Plop: " + strBody.equals("Plop"));

                    // 如果成功调用 completeNow()
                    testContext.completeNow();
                    // 如果失败调用 failNow()
//        testContext.failNow(new RuntimeException("failed"));
                })));
    }

    // 这个也没有实际完成调用，只是用来做说明的
    @Test
    public void test02() {
        Vertx vertx = Vertx.vertx();
        VertxTestContext testContext = new VertxTestContext();

        // 检查点应该只从测试用例主线程创建，而不是从vert.x异步事件回调创建
        Checkpoint serverStarted = testContext.checkpoint();
        Checkpoint requestServed = testContext.checkpoint(10);
        Checkpoint responsesReceived = testContext.checkpoint(10);

        vertx.createHttpServer()
                .requestHandler(req -> {
                    req.response().end("OK");
                    requestServed.flag();
                }).listen(8080, ar -> {
            if (ar.failed()) {
                testContext.failNow(ar.cause());
            } else {
                serverStarted.flag();
            }
        });

        WebClient client = WebClient.create(vertx);
        for (int i = 0; i < 10; i++) {
            client.get(8080, "localhost", "/")
                    .as(BodyCodec.string())
                    .send(ar -> {
                        if (ar.failed()) {
                            testContext.failNow(ar.cause());
                        } else {
                            testContext.verify(() -> {
                                boolean b = ar.result().body().equals("OK");
                                if (!b) {
                                    throw new RuntimeException("failed");
                                }
                            });
                            responsesReceived.flag();
                        }
                    });
        }
    }

}
