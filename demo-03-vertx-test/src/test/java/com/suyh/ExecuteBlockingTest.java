package com.suyh;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ExecuteBlockingTest {
    private static Vertx vertx;
    private static int timesExecuteBlocking = 0;

    // 重复执行必须要有
    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    @BeforeClass
    public static void beforeClass(TestContext ctx) {
        vertx = Vertx.vertx();
    }

    @AfterClass
    public static void afterClass(TestContext ctx) {
        vertx.close();
    }

    // 重复多次，必须要有 RepeatRule
    @Repeat(2)
    @Test
    public void testExecuteBlocking(TestContext ctx) {
        Async async = ctx.async();
        vertx.executeBlocking(asyncResult -> {
            int times = timesExecuteBlocking++;
            if (times % 2 == 0) {
                asyncResult.complete(times);
            } else {
                asyncResult.fail("executeBlocking failed.");
            }
        }, res -> {
            if (res.succeeded()) {
                int result = (int) res.result();
                ctx.assertTrue(result % 2 == 0);
            } else {
                res.cause().printStackTrace();
            }

            // 必须要调用，否则ctx 不知道你结束了
            async.complete();
            // ctx.fail(e);    // 这里会导致测试用例直接失败
        });

        async.await(5 * 1000);
    }

}