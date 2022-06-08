package me.proxy.protocol.protocol;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.proxy.protocol.AbstractPacket;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMessagePacket extends AbstractPacket {

    String userName, message;

    public UserMessagePacket() {
        super(1);
    }

    @Override
    protected void read(ByteBuf buf) {
        this.userName = UserMessagePacket.readString(buf);
        this.message = UserMessagePacket.readString(buf);
    }

    @Override
    protected void write(ByteBuf buf) {
        UserMessagePacket.writeString(buf, this.userName);
        UserMessagePacket.writeString(buf, this.message);
    }
}

