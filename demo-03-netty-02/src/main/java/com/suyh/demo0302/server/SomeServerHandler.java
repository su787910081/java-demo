package com.suyh.demo0302.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SomeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * ChannelInboundHandlerAdapter 中的channelRead 方法不会为我们释放msg 的数据，所以正常情况我们需要对该msg 进行释放
     * 如果有异步的写操作，用到了msg，则不可释放。需要在异步中使用结束 了再进行释放操作。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("channel = " + ctx.channel());

        // 将来自于客户端的数据显示在服务端控制台
        System.out.println(ctx.channel().remoteAddress() + "，" + msg);
        // 向客户端发送数据
        ctx.channel().writeAndFlush("from server：" + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
