package com.suyh.component;

import com.alibaba.fastjson.JSON;
import com.suyh.constant.KafkaConstant;
import com.suyh.utils.MQEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.suyh.constant.KafkaConstant.TOPIC_PREFIX;

/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-07 14:55
 */
@Component
public class KafkaReceiver {

    @KafkaListener(topics = {TOPIC_PREFIX + KafkaConstant.TOPIC_WMS_ORDER,
            TOPIC_PREFIX + "oms"})
    public void listen(ConsumerRecord<?, ?> record) {
        try {
            String topic = record.topic();
            System.out.println("topic: " + topic);
            String topicWmsOrder = TOPIC_PREFIX + KafkaConstant.TOPIC_WMS_ORDER;
            if (topicWmsOrder.equals(topic)) {
                //处理消费事件

                String msg = (String) record.value();
                System.out.println("CustomEvent.msg: " + msg);
                MQEvent mqEvent = JSON.parseObject(msg, MQEvent.class);
                String eventType = mqEvent.getEventType();
            }
        } catch (Exception ex) {
            System.out.println("kafka消费日志异常-->>" + record.value() + " " + ex.getMessage());
        }
    }
}
