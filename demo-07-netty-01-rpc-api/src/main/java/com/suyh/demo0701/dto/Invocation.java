package com.suyh.demo0701.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Invocation implements Serializable {
    // 服务名称(接口名)
    private String className;
    // 要调用的方法名
    private String methodName;
    // 方法参数列表
    private Class<?>[] paramTypes;
    // 方法参数值
    private Object[] paramValues;
}
