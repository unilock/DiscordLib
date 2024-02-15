package cc.unilock.discordlib.discord;

import cc.unilock.discordlib.DiscordLib;
import cc.unilock.discordlib.discord.command.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class Discord extends ListenerAdapter {
    private final JDABuilder builder;
    private JDA jda;
    private static final Map<String, ICommand> COMMAND_MAP = new HashMap<>();

    public Discord() {
        this.builder = JDABuilder.createDefault(DiscordLib.CONFIG.discord.token.value()).addEventListeners(this);
    }

    public void build() {
        try {
            this.jda = this.builder.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to log into Discord!", e);
        }
    }

    public void registerCommand(ICommand command) {
        if (this.jda != null) {
            throw new RuntimeException("Plugin tried to register a command after proxy initialization!\nCommand: " + command.getName() + " - " + command.getDescription());
        }
        COMMAND_MAP.put(command.getName(), command);
    }

    public <T extends ListenerAdapter> void registerListener(T listener) {
        if (this.jda != null) {
            throw new RuntimeException("Plugin tried to register a listener after proxy initialization!\nListener: " + listener.getClass());
        }
        this.builder.addEventListeners(listener);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        DiscordLib.getLogger().info("Bot ready!");

        var guild = this.jda.getGuildById(DiscordLib.CONFIG.discord.guild.value());

        if (guild == null) {
            throw new RuntimeException("Invalid guild ID!");
        }

        for (ICommand command : COMMAND_MAP.values()) {
            DiscordLib.getLogger().debug("Registering command: {} - {}", command.getName(), command.getDescription());
            guild.upsertCommand(command.getName(), command.getDescription()).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        var command = event.getName();

        if (!COMMAND_MAP.containsKey(command)) {
            return;
        }

        COMMAND_MAP.get(command).handle(event);
    }

    public void shutdown() {
        this.jda.shutdown();
    }
}
