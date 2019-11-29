package com.suyh.verticle;

import io.vertx.core.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

public class HelloWorldVerticle extends AbstractVerticle {

    /**
     * 对start() 方法的重写
     * 添加路由处理，监听端口
     * 这里没有作任何路由拦截处理，所有的请求都会到这里来，并且都能得到处理
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        // 像下面这样，允许同时监听多个端口
        int port01 = 8000;
        vertx.createHttpServer().requestHandler(
                new HttpRequestHandler(vertx, port01))
                .listen(port01, new ListenHandler(port01));
    }

    /**
     * 对http 请求的处理
     */
    private static class HttpRequestHandler implements Handler<HttpServerRequest> {
        private Vertx vertx;
        private int port;
        public HttpRequestHandler(Vertx vertx, int port) {
            this.vertx = vertx;
            this.port = port;
        }

        @Override
        public void handle(HttpServerRequest req) {

            req.method();
            req.absoluteURI();
            req.uri();
            req.path();
            req.query();
            req.host();
            req.remoteAddress();
            req.headers();
            req.params();

            // 第一次写入操作会触发响应头的写入，因此，若您不使用HTTP 分块，
            // 那么必须在写入响应之前设置 Content-Length 头，否则会太迟
            // 响应头必须在写入响应正文之前进行设置
            req.bodyHandler(totalBuffer -> {
                Context context = vertx.getOrCreateContext();

                req.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                        .end("HelloWorld, I am from port " + port);
            });
        }
    }

    /**
     * 处理监听结果
     */
    private static class ListenHandler implements Handler<AsyncResult<HttpServer>> {
        private int port;

        public ListenHandler(int port) {
            this.port = port;
        }

        @Override
        public void handle(AsyncResult<HttpServer> httpServerAsyncResult) {
            HttpServer result = httpServerAsyncResult.result();

            if (httpServerAsyncResult.succeeded()) {
                System.out.println("Server is now listening, port: " + port);
            } else {
                System.out.println("failed bind port: " + port);
            }
        }
    }

}
