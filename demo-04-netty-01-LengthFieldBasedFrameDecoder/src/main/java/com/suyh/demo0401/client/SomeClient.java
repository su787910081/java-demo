package com.suyh.demo0401.client;

import com.suyh.demo0401.server.SomeServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SomeClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            /*
             * TODO: suyh - 参数说明
             * @param maxFrameLength        整个帧允许的最大长度
             * @param lengthFieldOffset     长度域的偏移量，即：从开始位置偏移多少个字节得到长度域的开始下标位置。
             * @param lengthFieldLength     长度域的长度值，可选值为: 1, 2, 3, 4, 8。即这个长度域我们约定了它是几个字节。
             * @param lengthAdjustment      补偿值，添加到长度域中值的补偿值。该值可以为正值、负值、零值。
             *                              它的值将会与长度值相加来得到一个实际的长度值。
             *                              然后再从剥离后的位置开始计算，取出补偿后的长度的数据，这个数据就是我们关心的实际数据。
             *                              而这个数据，根据实际的约定有可能包含长度域的值，也有可能不包含长度域的值。
             *                              甚至，如果长度域与数据信息中间还有其他的值，那么中三个数据的值都可能被这个长度值表示了。
             * @param initialBytesToStrip   从开始位置剥离的字节数，即：一些辅助我们的实际数据的辅助信息，
             *                              我们将它们剥离掉，剩下的一些就是我们业务需要的实际关心的数据信息。
             * LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip)
             */
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            int lengthAdjustment = 0;
                            if (SomeServer.FLAG_INCLUDE_LENGTH) {
                                // 如果长度域里面的值包含了长度域的字节长度，则需要校正补偿值。
                                lengthAdjustment = 0 - SomeServer.LENGTH_SIZE;
                            }
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(
                                    1024, 0, SomeServer.LENGTH_SIZE, lengthAdjustment, SomeServer.LENGTH_SIZE));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new SomeClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
