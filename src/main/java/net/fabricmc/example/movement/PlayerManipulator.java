package net.fabricmc.example.movement;

import net.fabricmc.example.ExampleMod;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class PlayerManipulator {


    private PlayerManipulator player;
    private HashSet toggled;
    private double directionToFace;
    private int yawIncrement;

    public static int allowedYawDiscrepancy = 5;

    public boolean mouseInControl = false;

    private BlockPos destination;

    public PlayerManipulator(PlayerEntity player) {

        this.toggled = new HashSet<>();
        this.player = player;

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

    public static void addDirection(MovementDirection direction) {
        PlayerManipulator.toggled.add(direction);
    }

    public static boolean hasDirection(MovementDirection direction) {
        return PlayerManipulator.toggled.contains(direction);
    }

    public static void setDestination(BlockPos pos) {
        log("Setting destination to: " + pos.getX() + ", " + + pos.getY() + ", " + pos.getZ());
    }

    public static BlockPos getDestination() {
        return PlayerManipulator.destination;
    }

    public static void finish() {
        PlayerManipulator.toggled.clear();
    }

    private static void log(String str) {
        ExampleMod.LOGGER.info("[PlayerManipulator]: " + str);
    }




}
