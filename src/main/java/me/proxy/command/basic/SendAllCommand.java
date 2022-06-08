package me.proxy.command.basic;

import me.proxy.ProxyMain;
import me.proxy.command.Command;
import me.proxy.command.CommandException;
import me.proxy.helpers.LogHelper;
import me.proxy.protocol.protocol.UserMessagePacket;

public final class SendAllCommand extends Command {

    public SendAllCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return "[message]";
    }

    @Override
    public String getUsageDescription() {
        return "Send message to all users command";
    }

    @Override
    public void invoke(String... args) throws CommandException {
        if (args.length < 1) {
            LogHelper.subInfo("%s %s - %s", "send", args, this.getUsageDescription());
            return;
        }

        String message = args[0];
        for (int i = 1; i != args.length; i++)
            message = String.valueOf(message) + " " + args[i];

        UserMessagePacket packet = new UserMessagePacket();
        packet.setMessage(message);
        packet.setUserName("Andriy Shepeha [admin]");
        this.proxyMain.getUsers().values().forEach(connection -> connection.sendPacket(packet));
    }
}
