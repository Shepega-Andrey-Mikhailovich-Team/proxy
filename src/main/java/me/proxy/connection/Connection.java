package me.proxy.connection;

import io.netty.channel.ChannelHandlerContext;
import me.proxy.protocol.AbstractPacket;

public interface Connection {

    public String getName();

    public ChannelHandlerContext getChannel();

    public void onConnect();

    public void onDisconnect();

    public void sendPacket(AbstractPacket packet);

    public String getAddress();

    public Connection getConnection();
}

