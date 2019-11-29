package com.suyh;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.NetServer;

import java.util.Arrays;

public class FutureVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        listen01(8091);
        listen02(8092);
        listen03(8093);
        listen04(8094);
        listen05(8095);
    }

    private void listen01(int port) {
        // 一个普通的接口
        vertx.createHttpServer().requestHandler(req -> {
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
        }).listen(port);
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

        server.listen(port);
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

        server.listen(port);
    }

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
        });

        server.listen(port);
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
