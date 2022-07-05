package net.fabricmc.blockfinder.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.movement.CardinalDirection;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class LookCommand{

    public LookCommand() {};

    public static int runCommand(CommandContext context, CardinalDirection direction) {
        BlockFinder.LOGGER.info("Forcing player to look " + direction.toString());
        //PlayerManipulator.setDirectionToFace(direction);
        //PlayerManipulator.mouseInControl = !PlayerManipulator.mouseInControl;
        BlockFinder.LOGGER.info("PlayerManipulator.lookDirectionInControl updated to: " + PlayerManipulator.lookDirectionInControl);
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();
        return 1;
    }
}
