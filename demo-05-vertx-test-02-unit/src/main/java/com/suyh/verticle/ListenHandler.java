package com.suyh.verticle;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;

/**
 * 处理监听结果
 */
public class ListenHandler implements Handler<AsyncResult<HttpServer>> {
    private String name = "Server";
    private int port;

    public ListenHandler(int port, String name) {
        this.port = port;
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    @Override
    public void handle(AsyncResult<HttpServer> httpServerAsyncResult) {
        HttpServer result = httpServerAsyncResult.result();

        if (httpServerAsyncResult.succeeded()) {
            System.out.println(name + " is listening, port: " + port);
        } else {
            System.out.println(name + " failed bind port: " + port);
        }
    }
}
