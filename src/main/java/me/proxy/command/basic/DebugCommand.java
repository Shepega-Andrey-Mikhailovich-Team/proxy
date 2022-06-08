package me.proxy.command.basic;

import me.proxy.ProxyMain;
import me.proxy.command.Command;
import me.proxy.helpers.LogHelper;

public final class DebugCommand extends Command {
    public DebugCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Enable or disable debug logging at runtime";
    }

    @Override
    public void invoke(String... args) {
        LogHelper.setDebugEnabled(!LogHelper.isDebugEnabled());
        LogHelper.subInfo("Debug enabled: " + LogHelper.isDebugEnabled());
    }
}
