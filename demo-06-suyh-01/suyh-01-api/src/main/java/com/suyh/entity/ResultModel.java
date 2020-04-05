package com.suyh.entity;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-03 17:09
 */
@ApiModel(description = "返回实体")
public class ResultModel<T> implements Serializable {

    private static final long serialVersionUID = 2168115660376016205L;

    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_DESC = "SUCCESS";

    /**
     * 执行返回的实体,可以为空.
     */
    private T model;

    /**
     * 错误描述
     */
    private String errDesc = "";

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
     * @param errDesc  异常信息
     */
    public ResultModel(int errCode, String errDesc) {
        this.errDesc = errDesc;
        this.errCode = errCode;
    }

    /**
     * 设置错误消息
     *
     * @param errCode 异常编码
     * @param errDesc  异常信息
     * @param model   执行返回的实体,可以为空
     */
    public ResultModel(int errCode, String errDesc, T model) {
        this.model = model;
        this.errDesc = errDesc;
        this.errCode = errCode;
    }

    /**
     *
     * 直接构造一个成功的返回消息
     * @param model
     */
    public ResultModel(T model) {
        this.errCode = SUCCESS_CODE;
        this.errDesc = SUCCESS_DESC;
        this.model = model;
    }


    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}

