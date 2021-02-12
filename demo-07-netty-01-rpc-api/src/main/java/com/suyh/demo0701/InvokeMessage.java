package com.suyh.demo0701;

import lombok.Data;

import java.io.Serializable;

@Data
public class InvokeMessage implements Serializable {
    // 服务名称
    private String className;
    // 要调用的方法名
    private String methodName;
    // 方法参数列表
    private Class<?>[] paramTypes;
    // 方法参数值
    private Object[] paramValues;
}
