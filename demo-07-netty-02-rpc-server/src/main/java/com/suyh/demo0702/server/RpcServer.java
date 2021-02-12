package com.suyh.demo0702.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RpcServer {
    // 提供者注册表
    // key: 服务名称，即业务接口名
    // value: 业务接口实例类实例
    private Map<String, Object> registryMap = new HashMap<>();
    // 用于缓存提供者的类名
    private List<String> classCache = new ArrayList<>();

    // 完成服务发布与注册
    public void publish(String basePackage) throws Exception {
        // 获取指定包下的所有业务接口实现类名，并缓存
        fetchClassName(basePackage);
        // 将实现类名实例化后注册到map
        doRegister();
    }

    private void doRegister() throws Exception {
        if (classCache.isEmpty()) {
            return;
        }

        for (String className : classCache) {
            Class<?> clazz = Class.forName(className);
            Class<?>[] interfaces = clazz.getInterfaces();
            // 这里只允许实现一个业务接口的类
            if (interfaces.length == 1) {
                String interfaceName = interfaces[0].getName();
                registryMap.put(interfaceName, clazz.newInstance());
            }
        }
    }

    private void fetchClassName(String basePackage) {
        URL resource = this.getClass().getClassLoader()
                .getResource(basePackage.replaceAll("\\.", "/"));
        if (resource == null) {
            // 若目录中没有任何资源，则直接返回
            return;
        }
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                fetchClassName(basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "").trim();
                classCache.add(basePackage + "." + fileName);
            }
        }
    }

    // 启动当前server
    public void start() throws InterruptedException {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    // 指定存放请求的队列的长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 指定是否启用心跳机制来检测长连接的存活性，即客户端的存活性
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(
                                    Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new RpcServerHandler(registryMap));
                        }
                    });

            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("RPC SERVER启动成功：监听端口号为8888");
            future.channel().closeFuture().sync();
        } finally {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }
    }
}
