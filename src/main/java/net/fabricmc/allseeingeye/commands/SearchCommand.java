package net.fabricmc.allseeingeye.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.fabricmc.allseeingeye.scanning.ChunkScanner;
import net.fabricmc.allseeingeye.utils.ProcessType;
import net.fabricmc.allseeingeye.utils.ScanType;
import net.fabricmc.allseeingeye.utils.SearchType;
import net.fabricmc.allseeingeye.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;


public class SearchCommand {

    public SearchCommand() {};


    public static int runCommand(CommandContext context, ScanType type, int diameter) {
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();
        BlockPos foundBlock = null;
        if (type == ScanType.CHUNK) {
            foundBlock = ChunkScanner.chunkScan(context.getSource());
        } else if (type == ScanType.CIRCLE) {
            foundBlock = ChunkScanner.circleScan(context.getSource(),diameter);
        }
        if (foundBlock != null) {
            PlayerManipulator.setPlayer(player);
            PlayerManipulator.setDestination(foundBlock); //add one to 1 because the player will end up on top of the block
            PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);




        }
        return 1;
    }

}
