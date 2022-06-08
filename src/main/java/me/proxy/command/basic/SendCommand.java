package me.proxy.command.basic;

import me.proxy.ProxyMain;
import me.proxy.command.Command;
import me.proxy.command.CommandException;
import me.proxy.connection.ChatConnection;
import me.proxy.helpers.LogHelper;
import me.proxy.protocol.protocol.UserMessagePacket;

public final class SendCommand extends Command {

    public SendCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return "[user name] [message]";
    }

    @Override
    public String getUsageDescription() {
        return "Send message command";
    }

    @Override
    public void invoke(String... args) throws CommandException {
        if (args.length < 2) {
            LogHelper.subInfo("%s %s - %s", "send", args, this.getUsageDescription());
            return;
        }

        ChatConnection chatConnection = this.proxyMain.getUser(args[0]);
        if (chatConnection == null) {
            LogHelper.error("Username is offline!");
            return;
        }

        String message = args[1];
        for (int i = 2; i != args.length; i++)
            message = String.valueOf(message) + " " + args[i];

        UserMessagePacket packet = new UserMessagePacket();
        packet.setMessage(message);
        packet.setUserName("Andriy Shepeha [admin]");
        chatConnection.sendPacket(packet);
    }
}
