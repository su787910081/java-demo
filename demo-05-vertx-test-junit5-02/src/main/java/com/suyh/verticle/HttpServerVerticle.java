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

        int port = 8196;
        server.requestHandler(router).listen(port, new ListenHandler(port, this.getClass().getName()));
    }
}
