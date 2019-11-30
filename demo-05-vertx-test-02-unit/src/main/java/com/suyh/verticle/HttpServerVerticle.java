package com.suyh.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerVerticle extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Override
    public void start() throws Exception {
        super.start();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(routingContext -> {
            routingContext.request().bodyHandler(buffer -> {
                logger.debug("buffer: " + buffer);
                System.out.println("buffer: " + buffer);

                HttpServerResponse response = routingContext.response();
                response.end("Plop");
            });
        });

        int port = 8080;
        server.requestHandler(router).listen(port, res -> {
            if (res.succeeded()) {
                logger.info("listening port: " + port);
                System.out.println("listening port: " + port);
            } else {
                logger.error("listen {} failed.", port, res.cause());
                res.cause().printStackTrace();
                System.out.println("listen " + port + " failed.");
            }
        });
    }
}
