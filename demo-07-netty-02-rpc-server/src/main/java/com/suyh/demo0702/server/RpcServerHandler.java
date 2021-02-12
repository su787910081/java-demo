package com.suyh.demo0702.server;

import com.suyh.demo0701.dto.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Object> registerMap;

    public RpcServerHandler(Map<String, Object> registerMap) {
        this.registerMap = registerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Invocation) {
            Invocation message = (Invocation) msg;
            if (registerMap.containsKey(message.getClassName())) {
                Object provider = registerMap.get(message.getClassName());
                Object result = provider.getClass().getMethod(message.getMethodName(), message.getParamTypes())
                        .invoke(provider, message.getParamValues());
                ctx.writeAndFlush(result);
                 ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
