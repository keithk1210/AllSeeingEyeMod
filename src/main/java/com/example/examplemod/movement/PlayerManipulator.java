package com.example.examplemod.movement;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerManipulator {

    Player player;
    public PlayerManipulator(Player player) {
        this.player = player;

    }

    public void movePlayerTo(int x, int y,int z) {

        ExampleMod.LOGGER.info("Moving player " + player.getName() + " to " + x + ", " + y + ", " + z);
        player.move(MoverType.PLAYER, new Vec3(x,y,z));
        /*
        while (player.getBlockX() < x) {
            ExampleMod.LOGGER.info("Player is moving in positive X direction. Currently at: " + player.getX() + ", " + player.getY() + ", " + player.getZ());
            player.moveTo(player.getX() + .1,player.getY(),player.getZ());


        }
        while (player.getBlockX() > x) {
            ExampleMod.LOGGER.info("Player is moving in negative X direction. Currently at: " + player.getX() + ", " + player.getY() + ", " + player.getZ());
            player.moveTo(player.getX() - .1,player.getY(),player.getZ());

        }

         */

    }


}
