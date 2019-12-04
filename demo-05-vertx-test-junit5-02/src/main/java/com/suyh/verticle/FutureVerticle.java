package com.suyh.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class FutureVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(FutureVerticle.class);

    @Override
    public void start() throws Exception {
        listen01(8091);
        listen02(8092);
        listen03(8093);
        listen04(8094);
        listen04_list_01(8191);
        listen04_list_02(8192);
//        listen05(8095);
    }

    private void listen01(int port) {
        // 一个普通的接口
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                    .end("Hello, I am FutureVerticle. no Future.");

            // 未使用Future 处理evertBus
            // 这个接口应该是一个事件总线发送消息的接口，第一个参数是总线地址，
            // 第二个参数是消息，第三个参数是处理结果
            vertx.eventBus().send("address01", "message", asyncResult -> {
                if (asyncResult.succeeded()) {
                    System.out.println(asyncResult.result().body().toString());
                } else if (asyncResult.failed()) {
                    System.out.println("failed.");
                    asyncResult.cause().printStackTrace();
                } else {
                    System.out.println("Why I am here?");
                }
            });
        });

        server.listen(port, new ListenHandler(port, this.getClass().getName()));
    }

    private void listen02(int port) {
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                    .end("Hello， I am FutureVerticle. no Future.");

            // 未使用Future 处理eventBus
            // 这个接口应该是一个事件总线发送消息的接口
            // 第一个参数是总线地址，第二个参数是消息，第三个参数是处理结果
            vertx.eventBus().send("address01", "message", asyncResult01 -> {
                // 处理address01 的响应结果，需要再往address02 发送消息请求
                vertx.eventBus().send("address02", "message02", asyncResult02 -> {
                    System.out.println("内层处理结果");
                });
            });

        });

        server.listen(port, new ListenHandler(port, this.getClass().getName()));
    }

    private void listen03(int port) {
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                    .end("Hello I am FutureVerticle. use future.");

            // 使用future 处理eventBus
            Future<Message<String>> future01 = Future.future();
            Future<Message<String>> future02 = Future.future();

            // 2. future01 处理结果之后，再发往address02
            future01.setHandler(asyncResult -> {
                System.out.println("future01 finished.start future02.");
                vertx.eventBus().send("address02", "message02", future02);
            });

            // 3. future02 处理future01 的请求响应
            future02.setHandler(asyncResult -> {
                System.out.println("future02 result body: " + asyncResult.result().body());
            });

            // 1. 首先发到address01 的future01 处理结果
            vertx.eventBus().send("address01", "message01", future01);
        });

        server.listen(port, new ListenHandler(port, this.getClass().getName()));
    }

    // 链式操作
    private void listen04_list_01(int port) {
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(req -> {
            // 客户端请求到达
            // ...
            logger.debug("client http request is arrived.");

            // 请求数据到达
            req.bodyHandler(bodyBuffer -> {
                logger.debug("body handler ok.");
                String codeParam = req.getParam("code");
                int code = 0;
                if (codeParam == null || codeParam.isEmpty()) {
                    code = 0;
                }
                logger.debug("body handler code: " + codeParam);

                /**
                 * 传入参数为
                 * 1.调用此compose方法的future的handler,
                 * 2.下一个future
                 * 处理思路:
                 * 1.执行compose调用future的回调处理
                 * 2.如果当前future的回调处理中出错,那么将下一个future置为失败,
                 * 3.未出错则直接将下一个future返回.
                 *
                 * 解释:
                 * 1.定义2个future
                 * 2.第4行模拟第1个异步调用完毕,f1得到结果,状态completed.
                 * 3.f1发起compose,参数1为f1的handler,参数2为下一个future f2
                 * 4.注意,在f1的handler中,模拟第2个异步调用完毕,f2状态转为completed,从而触发f2的handler.
                 *
                 */
                Future<String> f1 = Future.future();
                Future<Integer> f2 = Future.future();

                f1.complete("f1's result");

                f1.compose(r -> {
                    System.out.println("f1 handler:" + r);
                    f2.complete(123);
                }, f2).setHandler(r -> {
                    System.out.println("f2 handler:" + r.result());

                    req.response().end("result from: " + port);
                });

            });
        });

        server.listen(port, new ListenHandler(port, this.getClass().getName()));
    }

    private void listen04_list_02(int port) {
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(req -> {
            // 客户端请求到达
            // ...

            // 请求数据到达
            req.bodyHandler(bodyBuffer -> {
                /**
                 * 传处理思路:
                 * 1.传入参数类型Function<T, Future<U>>,说明传入的是一个转换函数,
                 *      此函数将future中的调用结果T转换为链中的下一个future.
                 * 2.如果调用是成功的,那么将调用结果作为参数传入这个function执行,
                 *      就是这句"apply = mapper.apply(ar.result());",返回结果为Future<U>.
                 * 3.由于事先需要对调用结果ar是否成功判断,所以外面再套了个Future<U> ret.
                 * 4.将ret返回.
                 * 这个封装蛮有意思,在compose方法中设置调用者future的handler,
                 *      在handler中将future中的结果ar传递给compose参数(function),
                 *      然后执行function,
                 *      最后将function返回的future用compose内部生成的future封装下返回.
                 *
                 */
                Future<String> f1 = Future.future();
                f1.complete("f1's result");

                f1.compose(r -> {
                    System.out.println(r);
                    Future<String> f2 = Future.future();
                    f2.complete("f2's result");
                    return f2;
                }).compose(r -> {
                    System.out.println(r);
                    Future<String> f3 = Future.future();
                    f3.complete("f3's result");
                    return f3;
                }).setHandler(r -> {
                    System.out.println(r.result());

                    req.response().end("result from: " + port);
                });
            });
        });

        server.listen(port, new ListenHandler(port, this.getClass().getName()));
    }

    // 链式操作，一步接一步顺序操作，任何一步失败，则失败。
    private void listen04(int port) {
        /**
         * 3.4.0+ 链式操作，其中任何一步失败，则失败。
         * 下面两种写法等效
         * vertx.eventBus().send("address", "message", future.completer());
         * vertx.eventBus().send("address", "message", future);
         */

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(req -> {
            /**
             * 每一个compose 方法需要传入一个 Function, Function 的输入是前一个异步过程的返回值(
             *  此处的返回值不是AsyncResult, 而是具体的返回内容), 需要返回一个新的需要链接的Future,
             *  该Function 方法当且仅当前一个民步流程执行成功时才会被调用
             */
            /**
             * 这个例子描述了这样一个流程
             *
             *      首先通过eventbus 发送消息 message 到address1
             *      如果第一步成功，则发送第一步的消息的返回值到address2
             *      如果第二步成功，则发送第二步的消息的返回值到address3
             *      如果以上任何一步失败，则不会继续执行下一个异步流程，直接执行最终的Handler
             *      并且res.succeeded() 为false, 可以通过res.cause() 来获得异常对象
             *      如果以上三步全部成功，则同样执行Handler, res.succeeded() 为true,
             *      可以通过res.result() 获取最后一步的结果。
             */
            Future.<Message<String>>future(
                    f -> vertx.eventBus().send("address1", "message", f)
            ).compose(
                    (msg) -> Future.<Message<String>>future(f ->
                            vertx.eventBus().send("address2", msg.body(), f))
            ).compose(
                    (msg) -> Future.<Message<String>>future(f ->
                            vertx.eventBus().send("address3", msg.body(), f)
                    ).setHandler((res) -> {
                        // 这个handler 总是会被执行
                        if (res.failed()) {
                            // deal with execption
                            return;
                        }
                        // deal with the result
                    })
            );

            req.response().end();
        });

        server.listen(port, new ListenHandler(port, this.getClass().getName()));
    }

    // 并发合并，同时进行
    private void listen05(int port) {
        Future<HttpServer> httpServerFuture = Future.future();
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.listen(httpServerFuture.completer());

        Future<NetServer> netServerFuture = Future.future();
        NetServer netServer = vertx.createNetServer();
        netServer.listen(netServerFuture.completer());

        /**
         * CompositeFuture.all 方法接受多个Future 对象作为参数(最多6 个，或者传入list)。
         * 当所有的Future 都成功完成，该方法将返回一个成功的Future;
         * 当任一个Future 执行失败，则返回一个失败的Future;
         *
         * 所有被 合并的Future 中的操作同时运行。
         */
        CompositeFuture.all(httpServerFuture, netServerFuture).setHandler(ar -> {
            if (ar.succeeded()) {
                // 所有服务启动完成
            } else {
                // 有一个服务器启动失败
            }
        });

        // 或者可以传入一个数组
        // CompositeFuture.all(Arrays.asList(httpServerFuture, netServerFuture));

        // ############################################
        /**
         * any 方法的合并会等待第一个成功执行的Future,
         * 当任意一个Future 成功得到结果，则该Future 成功；
         * 当所有的future 都执行失败，则该Future 失败。
         */
        CompositeFuture.any(httpServerFuture, netServerFuture).setHandler(ar -> {
            if (ar.succeeded()) {
                // 至少一个成功
            } else {
                // 所有的都失败
            }
        });

        // 或者可以传入一个数组
        CompositeFuture.any(Arrays.asList(httpServerFuture, netServerFuture));
    }

}
