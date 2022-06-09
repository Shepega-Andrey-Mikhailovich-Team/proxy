package me.proxy.protocol;

import io.netty.buffer.ByteBuf;
import me.proxy.protocol.exception.BadPacketException;
import me.proxy.protocol.protocol.HandshakePacket;
import me.proxy.protocol.protocol.LoginPacket;
import me.proxy.protocol.protocol.UserMessagePacket;

import java.util.concurrent.ConcurrentHashMap;

public class PacketMapper {

    private static final ConcurrentHashMap<Integer, Class<? extends AbstractPacket>> packets = new ConcurrentHashMap<>();

    public static AbstractPacket readPacket(ByteBuf buf) throws BadPacketException {
        int id = buf.readInt();
        Class<? extends AbstractPacket> clazz = packets.get(id);
        if (clazz == null)
            throw new BadPacketException("Bad packet ID: " + id);

        try {
            AbstractPacket packet = clazz.newInstance();
            packet.read(buf);
            return packet;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static void writePacket(AbstractPacket packet, ByteBuf buf) {
        buf.writeInt(packet.getId());
        packet.write(buf);
    }

    static {
        packets.put(0, HandshakePacket.class);
        packets.put(1, LoginPacket.class);
        packets.put(2, UserMessagePacket.class);
    }
}

