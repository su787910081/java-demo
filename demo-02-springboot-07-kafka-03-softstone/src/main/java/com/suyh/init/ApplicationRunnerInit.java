package com.suyh.init;

import com.suyh.component.KafkaCustomMsgListener;
import com.suyh.utils.KafkaConfig;
import com.suyh.utils.KafkaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.suyh.constant.KafkaConstant.TOPIC_PREFIX;
import static com.suyh.constant.KafkaConstant.TOPIC_WMS_ORDER;

/**
 * 在springboot 启动之后立即执行下面代码
 * 与它相同功能的还有一个叫 CommandLineRunner的接口
 * 唯一不同的就是args 的参数类型。
 * <p>
 * 需要实现 ApplicationRunner 接口
 */
@Component  // 交给spring bean 管理
public class ApplicationRunnerInit implements ApplicationRunner {
    /**
     * 直接通过spring bean 来获取到环境上下文数据。
     * 这个对象就是 SpringApplication.run() 调用返回的那个对象。
     * 将地址打印出来可以看出地址是完全相同的。
     */
    @Autowired
    private ConfigurableApplicationContext ctx;

    @Resource
    private KafkaUtil kaf;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //初始化kafka
        KafkaConfig.getInstance().initProducer(ctx);
        KafkaConfig.getInstance().initConsumer(ctx);


        //监听标题
        String topicWmsOrder = TOPIC_PREFIX + TOPIC_WMS_ORDER;
        String topicOmsService = TOPIC_PREFIX + "oms";
//        kaf.kafkaListenerConsumer(new String[]{topicOmsService, topicWmsOrder});

        ctx.addApplicationListener(new KafkaCustomMsgListener());
        System.out.println("com.suyh.init.ApplicationRunnerInit ctx: " + ctx);
    }
}
