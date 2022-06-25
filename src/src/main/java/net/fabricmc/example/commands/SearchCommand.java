package net.fabricmc.example.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.movement.PlayerManipulator;
import net.fabricmc.example.scanning.ChunkScanner;
import net.fabricmc.example.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;


public class SearchCommand {

    public SearchCommand() {};


    public static int runCommand(CommandContext context) {
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();
        BlockPos foundBlock = ChunkScanner.scan(context.getSource());
        if (foundBlock != null) {
            ExampleMod.LOGGER.info("Block found! (inside SearchCommand.runCommand()");
            ExampleMod.LOGGER.info("Look at this angle: " + Utils.getAngle(player.getYaw(),foundBlock.getX() - player.getBlockX(), foundBlock.getZ() - player.getZ()));
        }
        return 1;
    }

}
