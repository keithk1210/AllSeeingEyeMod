package com.example.examplemod.scanning;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.movement.PlayerManipulator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class ChunkScanner {

    Player player;

    public ChunkScanner(Player player) {
        this.player = player;
    }

    public void analyzeChunk() {

        String playerBlock = player.getItemInHand(player.swingingArm).getItem().toString();

        int y = player.getBlockY() - 1; //gets the y level below the player's feet

        int startingX = (player.getBlockX() >= 0) ? (player.getBlockX() / 16) * 16 : ((player.getBlockX() / 16) * 16) - 15;
        int startingZ = (player.getBlockZ() >= 0) ? (player.getBlockZ() / 16) * 16 : ((player.getBlockZ() / 16) * 16) - 15;

        int chunkX = (player.getBlockX() >= 0) ? (player.getBlockX() / 16): ((player.getBlockX() / 16)) - 1;
        int chunkZ = (player.getBlockZ() >= 0) ? (player.getBlockZ() / 16) : ((player.getBlockZ() / 16)) - 1;

        player.sendMessage(new TextComponent("You are in chunk " + chunkX + ", " + chunkZ + ". Corner coords are: " + startingX + ", " + startingZ), Util.NIL_UUID);

        ExampleMod.LOGGER.info("Searching for " + playerBlock + "...");

        ExampleMod.LOGGER.info("In chunk: " + chunkX + ", " + chunkZ);

        int[] foundBlockCoordinates = new int[3];

        boolean found = false;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = (x + startingX);
                int worldZ = (z + startingZ);
                String block = player.level.getChunk(chunkX,chunkZ).getBlockState(new BlockPos(worldX,y,worldZ)).getBlock().asItem().toString();
                ExampleMod.LOGGER.info("Block at World: " + worldX + ", " +  worldZ + " Chunk: " + x + ", " + z + ": " + block);
                if (block.equals(playerBlock)) {
                    ExampleMod.LOGGER.info("Block found at World: " + worldX + ", " +  worldZ + " Chunk: " + x + ", " + z);
                    foundBlockCoordinates[0] = worldX;
                    foundBlockCoordinates[1] = y;
                    foundBlockCoordinates[2] = worldZ;
                    found = true;
                    break;
                }
                if (found) {
                    break;
                }

            }

        }

        if (found) {
            PlayerManipulator playerManipulator = new PlayerManipulator(player);
            playerManipulator.movePlayerTo(foundBlockCoordinates[0],foundBlockCoordinates[1],foundBlockCoordinates[2]);
        }

    }
}
