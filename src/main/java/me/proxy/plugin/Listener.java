package me.proxy.plugin;

import me.proxy.connection.Connection;
import me.proxy.protocol.AbstractPacket;

public interface Listener {

    public void handle(Connection connection, AbstractPacket packet);
}

