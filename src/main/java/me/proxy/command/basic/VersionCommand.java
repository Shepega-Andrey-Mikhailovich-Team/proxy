package me.proxy.command.basic;

import me.proxy.ProxyMain;
import me.proxy.command.Command;
import me.proxy.helpers.LogHelper;

public final class VersionCommand extends Command {
    public VersionCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Print Proxy version";
    }

    @Override
    public void invoke(String... args) {
        LogHelper.subInfo("Shepega Proxy version: %s (build #%s)", "2.0.3", "5.0.1");
    }
}
