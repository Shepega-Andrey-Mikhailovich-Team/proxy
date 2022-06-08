package me.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import me.proxy.command.handler.CommandHandler;
import me.proxy.command.handler.StdCommandHandler;
import me.proxy.config.Settings;
import me.proxy.connection.ChatConnection;
import me.proxy.helpers.CommonHelper;
import me.proxy.helpers.JVMHelper;
import me.proxy.helpers.LogHelper;
import me.proxy.plugin.ListenerManager;
import me.proxy.protocol.PipelineInitializer;
import me.proxy.protocol.protocol.UserMessagePacket;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProxyMain {

    private static final Path WORKING_DIR = Paths.get(System.getProperty("user.dir"));
    @Getter
    private final ConcurrentMap<String, ChatConnection> users = new ConcurrentHashMap<>();

    @Getter
    private static ProxyMain instance;
    @Getter
    private boolean enabled;

    @Getter
    final ListenerManager listenerManager;

    private Channel channel;
    private EventLoopGroup bossGroup, workerGroup;
    @Getter
    private final CommandHandler commandHandler;

    public ProxyMain() throws IOException {
        instance = this;
        Settings.IMP.reload(WORKING_DIR.resolve("config.yml").toFile());
        this.listenerManager = new ListenerManager();

        // Initial command handler?
        // TODO: add desktop proxy version support
        this.commandHandler = new StdCommandHandler(this, true);
        // this.commandHandler = new JLineCommandHandler(this);
        CommonHelper.newThread("Command Thread", false, this.commandHandler).start();
    }

    protected void start() {
        this.listenerManager.registerListener(UserMessagePacket.class, (connection, packet) -> {
            UserMessagePacket userMessagePacket = (UserMessagePacket) packet;
            this.users.values().forEach(chatConnection -> chatConnection.sendPacket(userMessagePacket));
            if (LogHelper.isDebugEnabled())
                LogHelper.info("[CHAT] " + userMessagePacket.getUserName() + ": " + userMessagePacket.getMessage());
        });

        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.startListener(Settings.IMP.PORT);
        this.enabled = true;
    }

    private void startListener(int port) {
        ChannelFutureListener listener = channelFuture -> {
            if (channelFuture.isSuccess()) {
                this.channel = channelFuture.channel();
                LogHelper.debug("Listening on /0.0.0.0:" + port);
            } else {
                LogHelper.debug("Could not bind to host /0.0.0.0:" + port);
                channelFuture.cause().printStackTrace();
            }
        };

        new ServerBootstrap().group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class).childHandler(new PipelineInitializer()).bind(port).addListener(listener).syncUninterruptibly();
    }

    public void stop() {
        this.enabled = false;
        LogHelper.info("Closing listener channel...");
        this.stopListener();
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
        LogHelper.info("GGWP!");
        JVMHelper.RUNTIME.exit(0);
    }

    private void stopListener() {
        ChannelFuture future = this.channel.close();
        future.syncUninterruptibly();
    }

    public static void main(String[] args) throws Exception {
        LogHelper.addOutput(WORKING_DIR.resolve("proxy.log"));
        LogHelper.info("Start proxy by Team from big 4el -> Andriy Shepeha (businessman)");

        // Start Proxy
        long start = System.currentTimeMillis();
        try {
            new ProxyMain().start();
        } catch (Throwable exc) {
            LogHelper.error(exc);
            return;
        }
        long end = System.currentTimeMillis();
        LogHelper.info("Proxy started in %dms", end - start);
    }

    public void addUser(String name, ChatConnection connection) {
        this.users.putIfAbsent(name.toLowerCase(), connection);
    }

    public ChatConnection getUser(String name) {
        return this.users.getOrDefault(name.toLowerCase(), null);
    }

    public void removeUser(String name) {
        this.users.remove(name.toLowerCase());
    }
}
