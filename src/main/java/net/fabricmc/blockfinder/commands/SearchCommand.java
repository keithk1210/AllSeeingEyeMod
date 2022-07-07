package net.fabricmc.blockfinder.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.scanning.ChunkScanner;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.fabricmc.blockfinder.utils.ScanType;
import net.fabricmc.blockfinder.utils.SearchType;
import net.fabricmc.blockfinder.utils.Utils;
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
