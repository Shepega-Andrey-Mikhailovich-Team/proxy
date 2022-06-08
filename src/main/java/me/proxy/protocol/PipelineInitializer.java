package me.proxy.protocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import me.proxy.protocol.decoder.PacketDecoder;
import me.proxy.protocol.encoder.PacketEncoder;
import me.proxy.protocol.handler.InitialHandler;

public class PipelineInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new InitialHandler());
    }
}

