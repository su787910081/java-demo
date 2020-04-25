package com.suyh.utils;

import org.springframework.context.ApplicationEvent;

/**
 * 用户自定义事件
 *
 * @author 枫澜 on 2019-12-18 17:00
 */
public class CustomEvent extends ApplicationEvent {
    private String msg;
    private String topic;

    public CustomEvent(Object source, String topic, String msg) {
        super(source);
        this.msg = msg;
        this.topic = topic;
    }

    /**
     * 自定义监听器触发的透传打印方法
     *
     * @param msg
     * @Title: printMsg
     * @author OnlyMate
     * @Date 2018年9月14日 下午3:10:45
     */
    public void printMsg(String msg) {
        System.out.println("CustomEvent ==> printMsg method 自定义事件: " + msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
