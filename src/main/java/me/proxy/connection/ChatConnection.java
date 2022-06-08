package me.proxy.connection;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.proxy.ProxyMain;
import me.proxy.helpers.LogHelper;
import me.proxy.protocol.AbstractPacket;
import me.proxy.protocol.handler.InitialHandler;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatConnection implements Connection {

    ChannelHandlerContext channel;
    String name;

    @Override
    public String getAddress() {
        return InitialHandler.getChannelIp(this.channel.channel());
    }

    @Override
    public Connection getConnection() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ChannelHandlerContext getChannel() {
        return this.channel;
    }

    @Override
    public void onConnect() {
        ProxyMain.getInstance().addUser(this.getName(), this);
        if (LogHelper.isDebugEnabled())
            LogHelper.info("[USER] Connect new user '" + this.name + "'!");
    }

    @Override
    public void onDisconnect() {
        ProxyMain.getInstance().removeUser(this.getName());
        if (LogHelper.isDebugEnabled())
            LogHelper.info("[USER] Disconnect user '" + this.name + "'!");
    }

    @Override
    public void sendPacket(AbstractPacket packet) {
        if (this.channel != null && this.channel.channel().isActive())
            this.channel.writeAndFlush(packet);
    }
}

