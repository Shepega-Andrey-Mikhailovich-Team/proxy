package me.proxy.protocol.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.proxy.protocol.AbstractPacket;
import me.proxy.protocol.PacketMapper;

public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {
    
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket packet, ByteBuf buf) throws Exception {
        PacketMapper.writePacket(packet, buf);
    }
}

