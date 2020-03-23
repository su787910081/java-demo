package com.suyh.controller;

import com.suyh.common.message.PayMessage;
import com.suyh.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/kafka/{msg}")
    public String getSetMsg(@PathVariable String msg) {
        System.out.println("getSetMsg: " + msg);

        PayMessage payMessage = new PayMessage();
        payMessage.setFee(0.1F);
        payMessage.setOrderCode("orderCode");
        payMessage.setSendTime(System.currentTimeMillis());

        messageProducer.send(payMessage);

        return msg;
    }
}
