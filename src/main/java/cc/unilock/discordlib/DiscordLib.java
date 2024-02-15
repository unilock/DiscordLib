package cc.unilock.discordlib;

import cc.unilock.discordlib.config.Config;
import cc.unilock.discordlib.discord.Discord;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "discordlib",
        name = "DiscordLib",
        description = "Provides a common Discord bot instance for other plugins to use",
        version = Tags.VERSION,
        authors = {"unilock"}
)
public final class DiscordLib {
    public static Config CONFIG;

    private static ProxyServer proxy;
    private static Logger logger;
    private static Discord discord;

    @Inject
    public DiscordLib(ProxyServer proxy, Logger logger, @DataDirectory Path path) {
        DiscordLib.proxy = proxy;
        DiscordLib.logger = logger;

        CONFIG = Config.createToml(path, "", "config", Config.class);

        if (CONFIG.discord.token.value().equals(CONFIG.discord.token.getDefaultValue()) || CONFIG.discord.guild.value().equals(CONFIG.discord.guild.getDefaultValue())) {
            DiscordLib.logger.error("Please configure the plugin via the config.toml file!");
        } else {
            discord = new Discord();
        }
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        if (discord != null) {
            discord.build();
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (discord != null) {
            discord.shutdown();
        }
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Logger getLogger() {
        return logger;
    }

    @Nullable
    public static Discord getDiscord() {
        return discord;
    }
}
