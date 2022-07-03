package net.fabricmc.example.commands;

import com.mojang.brigadier.context.CommandContext;

public interface CommandTemplate {

    public static int runCommand(CommandContext context) {
        return 1;
    };
}
