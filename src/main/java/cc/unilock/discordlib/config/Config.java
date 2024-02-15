package cc.unilock.discordlib.config;

import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;

public final class Config extends ReflectiveConfig {
    @Comment("Settings related to Discord")
    public final Discord discord = new Discord();

    public static class Discord extends Section {
        @Comment("Bot token")
        public final TrackedValue<String> token = value("NULL");

        @Comment("Guild ID")
        public final TrackedValue<String> guild = value("NULL");
    }
}
