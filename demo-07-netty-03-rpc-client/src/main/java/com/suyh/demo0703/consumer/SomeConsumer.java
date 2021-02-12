package com.suyh.demo0703.consumer;

import com.suyh.demo0701.service.SomeService;
import com.suyh.demo0703.client.RpcProxy;

public class SomeConsumer {
    public static void main(String[] args) {
        SomeService service = RpcProxy.create(SomeService.class);

        System.out.println(service.hello("Tom"));
        System.out.println(service.hashCode());
    }
}
