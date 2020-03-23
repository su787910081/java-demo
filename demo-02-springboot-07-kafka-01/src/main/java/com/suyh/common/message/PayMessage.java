package com.suyh.common.message;

//@Getter
//@Setter
//@ToString
public class PayMessage {
    private String orderCode;
    private Float fee;
    private Long sendTime;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "PayMessage{" +
                "orderCode='" + orderCode + '\'' +
                ", fee=" + fee +
                ", sendTime=" + sendTime +
                '}';
    }
}
