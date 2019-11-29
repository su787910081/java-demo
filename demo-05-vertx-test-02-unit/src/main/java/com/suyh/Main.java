package com.suyh;

import com.sun.deploy.cache.DeployCacheHandler;
import com.suyh.verticle.FutureVerticle;
import com.suyh.verticle.HelloWorldVerticle;
import io.netty.util.Recycler;
import io.vertx.core.*;

import java.util.Arrays;

public class Main {
    static {
        System.setProperty("log4j.configurationFile", "conf/log4j2.xml");
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.Logg4j2LogDelegateFactory");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        start01(vertx);
        // start02(vertx);
    }

    // 正常部署各个verticle
    private static void start01(Vertx  vertx) {
        // 一个vertx 中可以部署多个Verticle
        vertx.deployVerticle(HelloWorldVerticle.class.getName(),
                new DeployHandler("HelloWorldVerticle"));
        vertx.deployVerticle(FutureVerticle.class.getName(),
                new DeployHandler("FutureVerticle"));
//        vertx.deployVerticle(HelloWorldVerticle.class.getName(),
//                new DeployHandler("HelloWorldVerticle"));
    }

    // 使用 Future 部署
    private static void start02(Vertx vertx) {
        Future<String> futureHelloWorld = Future.future();
        futureHelloWorld.setHandler(new DeployHandler("HelloWorldVerticle"));

        Future<String> futureFuture = Future.future();
        futureFuture.setHandler(new DeployHandler("FutureVerticle"));

        vertx.deployVerticle(HelloWorldVerticle.class.getName(), futureHelloWorld);
        vertx.deployVerticle(FutureVerticle.class.getName(), futureFuture);
    }

    // 使用并发合并的方法来进行部署
    private static void start03(Vertx vertx) {
        Future<String> futureHelloWorldVerticle = Future.future();
        futureHelloWorldVerticle.setHandler(new DeployHandler("HelloWorldVerticle"));

        Future<String> futureFutureVerticle = Future.future();
        futureFutureVerticle.setHandler(new DeployHandler("FutureVerticle"));

        CompositeFuture.all(Arrays.asList(futureHelloWorldVerticle, futureFutureVerticle))
                .setHandler(ar -> {
                    if (ar.succeeded()) {
                        // 所有服务器都部署成功
                    } else {
                        // 茉个服务器部署失败
                        ar.cause().printStackTrace();
                    }
                });
    }

    private static class DeployHandler implements Handler<AsyncResult<String>> {
        private String name;
        public DeployHandler(String name) {
            this.name = name;
        }

        @Override
        public void handle(AsyncResult<String> res) {
            if (res.succeeded()) {
                System.out.println(name + " deployment id is: " + res.result());
            } else {
                System.out.println(name + " deployment failed!");
                res.cause().printStackTrace();
            }
        }
    }
}
