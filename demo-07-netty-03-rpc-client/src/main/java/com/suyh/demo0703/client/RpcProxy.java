package com.suyh.demo0703.client;

import com.suyh.demo0701.dto.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {
    public static <T> T create(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 如果是调用
                        if (Object.class.equals(method.getDeclaringClass())) {
                            return method.invoke(this, args);
                        }
                        // 完成远程调用
                        return rpcInvoke(clazz, method, args);
                    }
                });
    }

    private static Object rpcInvoke(Class<?> clazz, Method method, Object[] args) throws InterruptedException {
        RpcClientHandler handler = new RpcClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    // 关闭Nagel 算法
                    .option(ChannelOption.TCP_NODELAY, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(
                                    Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(handler);
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
            // 创建并初始化调用信息实例
            Invocation invocation = new Invocation();
            invocation.setClassName(clazz.getName());
            invocation.setMethodName(method.getName());
            invocation.setParamTypes(method.getParameterTypes());
            invocation.setParamValues(args);
            future.channel().writeAndFlush(invocation);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
        return handler.getResult();
    }
}
