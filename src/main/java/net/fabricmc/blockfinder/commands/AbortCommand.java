package net.fabricmc.blockfinder.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AbortCommand {

    public AbortCommand() {};

    public static int runCommand(CommandContext context) {
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getPlayer();
        player.sendMessage(Text.literal("Current process aborted"));
        PlayerManipulator.terminateHorizontalMovement();
        return 1;
    }
}
