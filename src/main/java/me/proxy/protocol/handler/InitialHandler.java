package me.proxy.protocol.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.proxy.ProxyMain;
import me.proxy.connection.ChatConnection;
import me.proxy.helpers.LogHelper;
import me.proxy.protocol.AbstractPacket;
import me.proxy.protocol.protocol.HandshakePacket;
import me.proxy.protocol.protocol.LoginPacket;

import java.net.InetSocketAddress;

public class InitialHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] -> InitialHandler has connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler has disconneted.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler ERROR: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket packet) {
        if (packet instanceof HandshakePacket) {
            if (LogHelper.isDebugEnabled())
                LogHelper.info("[/" + InitialHandler.getChannelIp(channelHandlerContext.channel()) + "] -> InitialHandler has read handshake: " + packet.toString());

            HandshakePacket handshakePacket = (HandshakePacket) packet;

            // hostname check

            channelHandlerContext.writeAndFlush(handshakePacket);
            return;
        }

        if (packet instanceof LoginPacket) {
            LoginPacket loginPacket = (LoginPacket) packet;

            if (ProxyMain.getInstance().getUser(loginPacket.getUserName()) != null) {
                loginPacket.setAllowed(false);
                loginPacket.setReason("Username already exists");
                channelHandlerContext.writeAndFlush(loginPacket);
                channelHandlerContext.close();
                return;
            }

            ChatConnection chatConnection = new ChatConnection(channelHandlerContext, loginPacket.getUserName());
            chatConnection.onConnect();
            loginPacket.setAllowed(true);
            channelHandlerContext.writeAndFlush(loginPacket);

            LogHelper.info("test");
            channelHandlerContext.pipeline().removeLast();
            channelHandlerContext.pipeline().addLast(new PacketHandler(chatConnection));
            return;
        }

        channelHandlerContext.close();
        LogHelper.info("[/" + InitialHandler.getChannelIp(channelHandlerContext.channel()) + "] InitialHandler ERROR: the first packet should be Handshake!");
    }

    public static String getChannelIp(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }
}

