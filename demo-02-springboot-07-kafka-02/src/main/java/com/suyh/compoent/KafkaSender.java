package com.suyh.compoent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suyh.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-07 14:53
 */
@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        System.out.println("+++++++++++++++++++++  message = " + gson.toJson(message));
        kafkaTemplate.send("zhisheng", gson.toJson(message));
    }
}