package net.fabricmc.example.movement;

import net.fabricmc.example.ExampleMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;

public class PlayerManipulator {


    HashSet toggled;
    private static double directionToFace;
    private static int yawIncrement;

    public static boolean mouseInControl = false;

    public PlayerManipulator(PlayerEntity player) {
        this.toggled = new HashSet<>();

    }

    public static void movePlayerHead(PlayerEntity player) {
        ExampleMod.LOGGER.info("Inside movePlayerHead method");
        ExampleMod.LOGGER.info("Player.getHorizontalFacing " + player.getHorizontalFacing());
        ExampleMod.LOGGER.info("Player.getActiveEyeHeight " + player.getActiveEyeHeight(player.getPose(),player.getDimensions(player.getPose())));
        ExampleMod.LOGGER.info("Player.getpitch() " + player.getPitch());
        ExampleMod.LOGGER.info("Player.getYaw() "+ player.getYaw());
    }

    public static void setYawIncrement(int yawIncrement) {
        PlayerManipulator.yawIncrement = yawIncrement;
    }

    public static int getYawIncrement() {
        return PlayerManipulator.yawIncrement;
    }

    public static void setDirectionToFace(double directionToFace) {
        PlayerManipulator.directionToFace = directionToFace;
    }

    public static double getDirectionToFace() {
        return PlayerManipulator.directionToFace;
    }


}
