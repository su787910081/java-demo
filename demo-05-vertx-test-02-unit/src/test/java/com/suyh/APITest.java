package com.suyh;

import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class APITest {
    static {
        // 可以在这里添加一些配置数据
        System.setProperty("temp.data", "nothing");
    }

    // 重复多次时必须要有这个属性，否则重复多次将不会生效
    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    // 异步超时
    @Rule
    public Timeout timeoutRule = Timeout.seconds(60);


}
