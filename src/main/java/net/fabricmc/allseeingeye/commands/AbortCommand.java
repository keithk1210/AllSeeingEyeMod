package net.fabricmc.allseeingeye.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AbortCommand {

    public AbortCommand() {};

    public static int runCommand(CommandContext context) {
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getPlayer();
        player.sendMessage(Text.literal("Current process aborted"));
        PlayerManipulator.setCurrentProcess(null);
        PlayerManipulator.clearToggled();
        PlayerManipulator.setCurrentSearchType(null);
        return 1;
    }
}
