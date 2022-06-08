package me.proxy.command.basic;

import me.proxy.ProxyMain;
import me.proxy.command.Command;
import me.proxy.helpers.LogHelper;

public final class ClearCommand extends Command {
    public ClearCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Clear terminal";
    }

    @Override
    public void invoke(String... args) throws Throwable {
        this.proxyMain.getCommandHandler().clear();
        LogHelper.subInfo("Terminal cleared");
    }
}
