package com.suyh.entity;

import java.util.Date;

/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-07 14:52
 */
public class Message {
    private Long id;    //id

    private String msg; //消息

    private Date sendTime;  //时间戳

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
