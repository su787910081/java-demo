package com.suyh.component;

import com.alibaba.fastjson.JSON;
import com.suyh.constant.KafkaConstant;
import com.suyh.utils.CustomEvent;
import com.suyh.utils.MQEvent;
import org.springframework.context.ApplicationListener;



/**
 * Kafka消费者消费信息监听事件
 *
 * @author 枫澜  on 2019-12-18
 */
public class KafkaCustomMsgListener implements ApplicationListener<CustomEvent> {
    private static String topicPrefix = "local";

    /**
     * 对监听到的事件进行处理
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(CustomEvent event) {
        try {
            String topic = event.getTopic();
            System.out.println("topic: " + topic);
            String topicWmsOrder = topicPrefix + "_" + KafkaConstant.TOPIC_WMS_ORDER;
            if (topicWmsOrder.equals(topic)) {
                //处理消费事件
                String msg = event.getMsg();
                System.out.println("CustomEvent.msg: " + msg);
                MQEvent mqEvent = JSON.parseObject(msg, MQEvent.class);
                String eventType = mqEvent.getEventType();
            }
        } catch (Exception ex) {
            System.out.println("kafka消费日志异常-->>" + event.getMsg() + " " + ex.getMessage());
        }

    }
}
