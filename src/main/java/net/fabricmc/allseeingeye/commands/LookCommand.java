package net.fabricmc.allseeingeye.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.movement.CardinalDirection;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class LookCommand{

    public LookCommand() {};

    public static int runCommand(CommandContext context, CardinalDirection direction) {
        AllSeeingEye.LOGGER.info("Forcing player to look " + direction.toString());
        //PlayerManipulator.setDirectionToFace(direction);
        //PlayerManipulator.mouseInControl = !PlayerManipulator.mouseInControl;
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();
        return 1;
    }
}
