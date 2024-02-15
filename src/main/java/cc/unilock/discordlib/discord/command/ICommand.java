package cc.unilock.discordlib.discord.command;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public interface ICommand {
    String getName();

    String getDescription();

    void handle(SlashCommandInteraction interaction);
}
