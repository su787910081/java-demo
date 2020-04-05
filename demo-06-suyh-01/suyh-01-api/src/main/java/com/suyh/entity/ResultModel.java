package com.suyh.entity;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-03 17:09
 */
@ApiModel(description = "返回实体")
public class ResultModel<T> implements Serializable {

    private static final long serialVersionUID = 2168115660376016205L;

    /**
     * 执行返回的实体,可以为空.
     */
    private T model;

    /**
     * 错误描述
     */
    private String errMsg = "";

    /**
     * 错误编码
     */
    private int errCode = -1;

    /**
     * 构造函数
     */
    public ResultModel() {
    }

    /**
     * 构造函数，返回False的方法
     *
     * @param errCode 异常编码
     * @param errMsg  异常信息
     */
    public ResultModel(int errCode, String errMsg) {
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    /**
     * 设置错误消息
     *
     * @param errCode 异常编码
     * @param errMsg  异常信息
     * @param model   执行返回的实体,可以为空
     */
    public ResultModel(int errCode, String errMsg, T model) {
        this.model = model;
        this.errMsg = errMsg;
        this.errCode = errCode;
    }


    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}

