package net.fabricmc.allseeingeye.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.allseeingeye.AllSeeingEye;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class DrawCircleCommand{

    public DrawCircleCommand() {

    }

    private static final BlockState[] concretes = new BlockState[] {
            Blocks.RED_CONCRETE.getDefaultState(), //1
            Blocks.ORANGE_CONCRETE.getDefaultState(), //3
            Blocks.YELLOW_CONCRETE.getDefaultState(), //5
            Blocks.GREEN_CONCRETE.getDefaultState(), //7
            Blocks.BLUE_CONCRETE.getDefaultState(), //9
            Blocks.PURPLE_CONCRETE.getDefaultState() //11
    };

    public static int runCommand(CommandContext context, int diameter) {
        AllSeeingEye.LOGGER.info("DrawCircleCommand.runCommand() successfully run");
        ServerCommandSource serverCommandSource = (ServerCommandSource) context.getSource();
        PlayerEntity player = serverCommandSource.getEntity().getCommandSource().getPlayer();

        int playerX = player.getBlockX();
        int playerZ = player.getBlockZ() ;
        int y = player.getBlockY() - 1; //the block below the player's feet
        DrawCircleCommand.log("Player was at: " + playerX + ", " + y + ", " + playerZ );

        for (int d = 1,index = 0; d <= diameter; d += 2) {
            for (double degrees = 0; degrees <  360; degrees += .5) {
                double radians = degrees * (Math.PI/ 180);
                int x = (int) Math.round((Math.sin(radians) * (d/2.0)) + playerX);
                int z = (int) Math.round((Math.cos(radians) * (d / 2.0)) + playerZ);
                log(x + ", " + y + ", " + z);
                player.getWorld().setBlockState(new BlockPos(x,y,z),concretes[index]);
            }
            index = (index + 1 > 5) ? 0: index + 1;
        }

        return 1;
    }

    public static void log(String string) {
        AllSeeingEye.LOGGER.info("[DrawCircleCommand]: " + string);
    }

}
