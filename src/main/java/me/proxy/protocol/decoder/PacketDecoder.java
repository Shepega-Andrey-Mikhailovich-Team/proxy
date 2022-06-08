package me.proxy.protocol.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import me.proxy.protocol.AbstractPacket;
import me.proxy.protocol.PacketMapper;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<AbstractPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        out.add(PacketMapper.readPacket(buf));
    }
}

