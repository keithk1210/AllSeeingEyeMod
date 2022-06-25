package net.fabricmc.example.scanning;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class ChunkScanner {

    public ChunkScanner() {

    };

    public static BlockPos scan(Object source) {
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
                    return new BlockPos(x,y,z);
                }
            }
        }
        return null;
    }
}
