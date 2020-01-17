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

    // 在一个测试用例中要进行多次异步调用时需要指定调用的个数
    @Test
    public void testMany(TestContext ctx) {
        // 指定要做的调用次数，最多这多次全部都被调用了才结束
        int count = 2;
        Async async = ctx.async(count);

        for (int i = 0; i < count; i++) {
            vertx.executeBlocking(asyncResult -> {
                int times = timesExecuteBlocking++;
                if (times % 2 == 0) {
                    asyncResult.complete(times);
                } else {
                    asyncResult.fail("executeBlocking failed.");
                }
            }, res -> {
                if (res.succeeded()) {
                    int times = (int) res.result();
                    ctx.assertTrue(times % 2 == 0);

                    async.countDown(); // 指定了个数就不能简单的使用 complete(); 来结束了。
                } else {
                    res.cause().printStackTrace();
                    ctx.fail(res.cause());
                }
            });
        }

        // 前面指定的count 次必须都被正确执行了才结束
        async.await(5 * 1000);
    }
}