package com.suyh;

import com.suyh.compoent.KafkaSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-07 14:52
 */
@SpringBootApplication
public class DemoKafka02Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context  = SpringApplication.run(DemoKafka02Application.class, args);

        KafkaSender sender = context.getBean(KafkaSender.class);

        for (int i = 0; i < 3; i++) {
            //调用消息发送类中的消息发送方法
            sender.send();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
