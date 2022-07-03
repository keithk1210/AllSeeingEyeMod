package net.fabricmc.example.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.movement.PlayerManipulator;
import net.fabricmc.example.scanning.ChunkScanner;
import net.fabricmc.example.utils.SearchType;
import net.fabricmc.example.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;


public class SearchCommand {

    public SearchCommand() {};


    public static int runCommand(CommandContext context, SearchType type, int diameter) {
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();
        BlockPos foundBlock = null;
        if (type == SearchType.CHUNK) {
            foundBlock = ChunkScanner.chunkScan(context.getSource());
        } else if (type == SearchType.CIRCLE) {
            foundBlock = ChunkScanner.circleScan(context.getSource(),diameter);
        }
        if (foundBlock != null) {
            double centerX = (foundBlock.getX() > 0) ? foundBlock.getX() + .5 : foundBlock.getX() - .5;
            double centerZ = (foundBlock.getZ() > 0) ? foundBlock.getZ() + .5 : foundBlock.getZ() - .5;

            ExampleMod.LOGGER.info("Block found at " + foundBlock.getX() + ", " + foundBlock.getZ() + "! Center coords are: " + centerX + ", " + centerZ + " (inside SearchCommand.runCommand()");
            ExampleMod.LOGGER.info("Player was at: " + player.getBlockX() + ", " + player.getBlockZ());

            double angle = Utils.getAngle(player.getYaw(),centerX - player.getX(), centerZ - player.getZ());

            ExampleMod.LOGGER.info("At line 27 of SearchCommand.java");
            player.sendMessage(Text.literal("Block found at " + foundBlock.getX() + ", " + foundBlock.getZ() + "!"));
            player.sendMessage(Text.literal("Look at this angle: " + angle));

            double convertedYaw = Utils.convertYaw(player.getYaw());
            double convertedAngle = Utils.convertYaw(angle);

            ExampleMod.LOGGER.info("Look at this angle: " + angle + " Converted: " + convertedAngle + " Current player yaw: " + player.getYaw() + " converted: " + convertedYaw);

            int idealIncrement = Utils.getIdealYawIncrement(convertedYaw,convertedAngle);

            ExampleMod.LOGGER.info("Setting ideal yaw increment: " + idealIncrement);

            ExampleMod.playerManipulatorHashSet.add(new PlayerManipulator(player));

            PlayerManipulator playerManipulator;

            for (PlayerManipulator pm: ExampleMod.playerManipulatorHashSet) {
                if (pm.getPlayer().equals(player)) {
                    playerManipulator = pm;
                }
            }
            .setYawIncrement(idealIncrement);
            PlayerManipulator.setDirectionToFace(angle);
            PlayerManipulator.mouseInControl = true;
            PlayerManipulator.setDestination(new BlockPos(foundBlock.getX(),foundBlock.getY()+1,foundBlock.getZ())); //add one to 1 because the player will end up on top of the block
        }
        return 1;
    }

}
