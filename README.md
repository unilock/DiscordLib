# DiscordLib

Velocity plugin to provide a common Discord bot instance for other plugins to use.

## Usage

In your `build.gradle`:

```groovy
repositories {
    maven { url = "https://jitpack.io" }
}

dependecies {
    implementation "com.github.unilock:discordlib:1.0.0"
}
```

In your plugin constructor:

```java
if (DiscordLib.getDiscord() != null) {
    DiscordLib.getDiscord().registerCommand(new ExampleCommand());
    DiscordLib.getDiscord().registerListener(new ExampleListener());
}
```

`ExampleCommand` should implement `cc.unilock.discordlib.discord.command.ICommand`.  
`ExampleListener` should extend `cc.unilock.discordlib.lib.net.dv8tion.jda.api.hooks.ListenerAdapter`.

JDA is shaded and relocated.