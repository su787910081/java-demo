package com.suyh.demo0302.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class SomeClientHandler extends SimpleChannelInboundHandler<String> {

    // msg的消息类型与类中的泛型类型是一致的

    /**
     * SimpleChannelInboundHandler 中的channelRead0 的msg 对象会被父类处理，并释放掉。
     * 所以在这里的实现中如果异步写用到了msg 会出现使用了被标记为释放的数据，所以如果要使用到msg
     * 做异步操作，则可以选择ChannelInboundHandlerAdapter
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "，" + msg);
        ctx.channel().writeAndFlush("from client：" + LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    // 当Channel被激活后会触发该方法的执行，即连接成功。
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("from client：begin talking");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
