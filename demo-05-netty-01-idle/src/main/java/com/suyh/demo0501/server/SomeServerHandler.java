package com.suyh.demo0501.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

    // 连接被激活时被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接成功");
        super.channelActive(ctx);
    }

    // 连接断开时被调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开");
        super.channelInactive(ctx);
    }

    // 所有“规定动作”之外的所有事件都可以通过以下方法触发
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventDes = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventDes = "读空闲超时";
                    break;
                case WRITER_IDLE:
                    eventDes = "写空闲超时";
                    break;
                case ALL_IDLE:
                    eventDes = "读和写空闲都超时";
                    break;
                default:
                    break;
            }
            System.out.println(eventDes);
            // 关闭连接
            // ctx.close();

        } else {    // 其它事件触发
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("xxxxxxxxx");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
