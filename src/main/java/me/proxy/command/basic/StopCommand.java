package me.proxy.command.basic;

import me.proxy.ProxyMain;
import me.proxy.command.Command;

public final class StopCommand extends Command {
    public StopCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Stop Proxy";
    }

    @Override
    @SuppressWarnings("CallToSystemExit")
    public void invoke(String... args) {
        this.proxyMain.stop();
    }
}
