package com.suyh;

import com.suyh.suyh.HttpServerVerticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;


/**
 * 生命周期
 */
@ExtendWith(VertxExtension.class)
public class Suyh02LifecycleTest {
  @BeforeEach
  @DisplayName("Deploy a verticle")
  void prepare(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new HttpServerVerticle(), testContext.completing());
  }

  @Test
  @DisplayName("A first test")
  void foo(Vertx vertx, VertxTestContext testContext) {
    // (...)
    testContext.completeNow();
  }

  @Test
  @DisplayName("A second test")
  void bar(Vertx vertx, VertxTestContext testContext) {
    // (...)
    testContext.completeNow();
  }

  @AfterEach
  @DisplayName("Check that the verticle is still there")
  void lastChecks(Vertx vertx) {
    Set<String> strings = vertx.deploymentIDs();
    Assert.assertFalse(strings.isEmpty());
    Assert.assertEquals(1, strings.size());
  }
}
