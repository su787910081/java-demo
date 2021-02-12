package com.suyh.demo0702.server;

public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        RpcServer server = new RpcServer();
        server.publish("com.suyh.demo0702.service");
        server.start();
    }
}
