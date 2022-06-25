package net.fabricmc.example.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.movement.CardinalDirection;
import net.fabricmc.example.movement.MovementDirection;
import net.fabricmc.example.movement.PlayerManipulator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class LookCommand {

    public LookCommand() {};

    public static int runCommand(CommandContext context, CardinalDirection direction) {
        ExampleMod.LOGGER.info("Forcing player to look " + direction.toString());
        PlayerManipulator.setDirectionToFace(direction);
        PlayerManipulator.mouseInControl = !PlayerManipulator.mouseInControl;
        ExampleMod.LOGGER.info("PlayerManipulator.mouseInControl updated to: " + PlayerManipulator.mouseInControl);
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();
        return 1;

    }
}
