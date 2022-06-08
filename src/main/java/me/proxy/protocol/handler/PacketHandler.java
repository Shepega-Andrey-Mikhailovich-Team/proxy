package me.proxy.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import me.proxy.ProxyMain;
import me.proxy.connection.Connection;
import me.proxy.helpers.LogHelper;
import me.proxy.protocol.AbstractPacket;

@AllArgsConstructor
public class PacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    private final Connection connection;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.connection.onDisconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogHelper.info("[" + this.connection.getName() + "] PacketHandler ERROR: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket abstractPacket) throws Exception {
        if (LogHelper.isDebugEnabled())
            LogHelper.info("[" + this.connection.getName() + "] PacketHandler has read: " + abstractPacket.toString());
        ProxyMain.getInstance().getListenerManager().handleListeners(this.connection, abstractPacket);
    }
}

