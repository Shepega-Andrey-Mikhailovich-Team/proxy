package me.proxy.protocol.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.proxy.ProxyMain;
import me.proxy.connection.ChatConnection;
import me.proxy.connection.Connection;
import me.proxy.helpers.LogHelper;
import me.proxy.protocol.AbstractPacket;
import me.proxy.protocol.protocol.HandshakePacket;

import java.net.InetSocketAddress;

public class InitialHandler extends SimpleChannelInboundHandler<AbstractPacket> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] -> InitialHandler has connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler has disconneted.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler ERROR: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket packet) throws Exception {
        if (packet instanceof HandshakePacket) {
            LogHelper.info("[/" + InitialHandler.getChannelIp(channelHandlerContext.channel()) + "] -> InitialHandler has read handshake: " + packet.toString());
            HandshakePacket handshakePacket = (HandshakePacket) packet;
            if (ProxyMain.getInstance().getUser(handshakePacket.getName()) != null) {
                handshakePacket.setAllowed(false);
                handshakePacket.setCancelReason("Username already exists");
                channelHandlerContext.writeAndFlush(handshakePacket);
                channelHandlerContext.close();
                return;
            }

            Connection connection = new ChatConnection(channelHandlerContext, handshakePacket.getName());
            handshakePacket.setAllowed(true);
            channelHandlerContext.writeAndFlush(handshakePacket);
            channelHandlerContext.pipeline().removeLast();
            connection.onConnect();
            channelHandlerContext.pipeline().addLast(new PacketHandler(connection));
        } else {
            channelHandlerContext.close();
            LogHelper.info("[/" + InitialHandler.getChannelIp(channelHandlerContext.channel()) + "] InitialHandler ERROR: the first packet should be Handshake!");
        }
    }

    public static String getChannelIp(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }
}

