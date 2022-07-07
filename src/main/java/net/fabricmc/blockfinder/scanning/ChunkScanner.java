package net.fabricmc.blockfinder.scanning;

import net.fabricmc.blockfinder.BlockFinder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class ChunkScanner {

    public ChunkScanner() {

    };

    public static BlockPos chunkScan(Object source) {
        ServerCommandSource player = (ServerCommandSource) source;

        String playerBlock = player.getPlayer().getStackInHand(player.getPlayer().getActiveHand()).getItem().toString();

        System.out.println("Player is holding: " + playerBlock);


        int playerX = player.getEntity().getBlockX();
        int playerZ = player.getEntity().getBlockZ();
        int y = player.getEntity().getBlockY() - 1; //y below player's feet

        int chunkX = (playerX >= 0) ? (playerX / 16): ((playerX / 16)) - 1;
        int chunkZ = (playerZ >= 0) ? (playerZ / 16) : ((playerZ / 16)) - 1;

        int cornerX = (playerX >= 0) ? (playerX / 16) * 16 : ((playerX / 16) * 16) - 15;
        int cornerZ = (playerZ >= 0) ? (playerZ / 16) * 16 : ((playerZ / 16) * 16) - 15;

        System.out.println("Player is in chunk: x = "  + chunkX + ", z = " + chunkZ + ". Corner coords are: x = " + cornerX + ", z = " + cornerZ);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = cornerX + x;
                int worldZ = cornerZ + z;
                String block = player.getWorld().getBlockState(new BlockPos(worldX,y,worldZ)).getBlock().asItem().toString();
                System.out.println("Block found at " + worldX + ", " + y + ", " + worldZ + " is " + block);
                if (block.equals(playerBlock)) {
                    return new BlockPos(worldX,y,worldZ);
                }
            }
        }
        return null;
    }

    public static BlockPos circleScan(Object source,int diameter) {

        ServerCommandSource serverCommandSource = (ServerCommandSource) source;

        PlayerEntity player = serverCommandSource.getPlayer();

        int playerX = player.getBlockX();
        int playerZ = player.getBlockZ();
        int playerY = player.getBlockY() - 1; //y below player's feet

        String playerBlock = player.getStackInHand(player.getActiveHand()).getItem().toString();

        System.out.println("Player is holding: " + playerBlock);

        player.sendMessage(Text.literal("Searching for " + playerBlock + " in a " + (diameter / 2) + " radius"));
        for (int y = playerY; y > -64; y--) {
            for (int d = 1; d <= diameter; d += 2) {
                for (double degrees = 0; degrees < 360; degrees += .5) {
                    double radians = degrees * (Math.PI / 180);
                    int x = (int) Math.round((Math.sin(radians) * (d / 2.0)) + playerX);
                    int z = (int) Math.round((Math.cos(radians) * (d / 2.0)) + playerZ);
                    String block = player.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock().asItem().toString();
                    //log("Block at " + x + ", " + y + ", " + z + " is: " + block);
                    if (block.equals(playerBlock)) {
                        log("Block was found at " + x + ", " + y + ", " + z + "!");
                        player.sendMessage(Text.literal(playerBlock + " found at " + x + ", " + y + ", " + z));
                        return new BlockPos(x, y, z);
                    }

                }
            }
        }
        return null;
    }
    public static BlockPos circleScan(PlayerEntity player, HashSet<Block> targets, int diameter) {

        int playerX = player.getBlockX();
        int playerZ = player.getBlockZ();
        int playerY = player.getBlockY() - 1; //y below player's feet


        for (int y = playerY; y > -64; y--) {
            for (int d = 1; d <= diameter; d += 2) {
                for (double degrees = 0; degrees < 360; degrees += .5) {
                    double radians = degrees * (Math.PI / 180);
                    int x = (int) Math.round((Math.sin(radians) * (d / 2.0)) + playerX);
                    int z = (int) Math.round((Math.cos(radians) * (d / 2.0)) + playerZ);
                    Block block = player.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
                    //log("Block at " + x + ", " + y + ", " + z + " is: " + block);
                    if (targets.contains(block)) {
                        log("Block was found at " + x + ", " + y + ", " + z + "!");
                        return new BlockPos(x, y, z);
                    }

                }
            }
        }
        return null;
    }



    private static void log(String string) {
        BlockFinder.LOGGER.info("[ChunkScanner]: " + string);
    }
}
