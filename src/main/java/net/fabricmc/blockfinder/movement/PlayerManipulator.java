package net.fabricmc.blockfinder.movement;

import net.fabricmc.blockfinder.BlockFinder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class PlayerManipulator {


    private static PlayerEntity player;
    private static HashSet<MovementDirection> toggled = new HashSet();
    private static double directionToFace = -181; //defaults to -181 which is impossible in minecraft terms
    private static int yawIncrement;

    private static int yawIncrementMultiplier = 10;

    public static int allowedYawDiscrepancy = 5;

    public static boolean lookDirectionInControl = false;
    private static boolean pitchInControl = false;

    private static BlockPos destination;

    public PlayerManipulator(PlayerEntity player) {


    }

    public static void movePlayerHead(PlayerEntity player) {
        BlockFinder.LOGGER.info("Inside movePlayerHead method");
        BlockFinder.LOGGER.info("Player.getHorizontalFacing " + player.getHorizontalFacing());
        BlockFinder.LOGGER.info("Player.getActiveEyeHeight " + player.getActiveEyeHeight(player.getPose(),player.getDimensions(player.getPose())));
        BlockFinder.LOGGER.info("Player.getpitch() " + player.getPitch());
        BlockFinder.LOGGER.info("Player.getYaw() "+ player.getYaw());
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
        PlayerManipulator.destination = pos;
    }

    public static BlockPos getDestination() {
        return PlayerManipulator.destination;
    }

    public static void terminateHorizontalMovement() {

        PlayerManipulator.toggled.clear();
        PlayerManipulator.destination = null;
        PlayerManipulator.lookDirectionInControl = false;
        PlayerManipulator.directionToFace = -181;
        PlayerManipulator.yawIncrementMultiplier = 10;
    }

    private static void log(String str) {
        BlockFinder.LOGGER.info("[PlayerManipulator]: " + str);
    }

    public static PlayerEntity getPlayer() {
        return  PlayerManipulator.player;
    }

    public static void setPlayer(PlayerEntity player) {PlayerManipulator.player = player; };

    public static void checkIfHorizontalPositionReached() {
        if (player != null && destination != null) {
            if (player.getX() > destination.getX() && player.getZ() > destination.getZ()) {
                double xDiff = Math.abs(player.getX() - destination.getX());
                double zDiff = Math.abs(player.getZ() - destination.getZ());
                if (xDiff <= .5 && zDiff <= .5) {
                    BlockFinder.LOGGER.info("Player reached horizontal position of: " + player.getBlockPos().getX() + ", " + player.getBlockPos().getZ());
                    PlayerManipulator.terminateHorizontalMovement();
                    setPitchInControl(true);
                    BlockFinder.LOGGER.info("PlayerManipulator.pitchInControl set to: " + PlayerManipulator.pitchInControl);
                }
            }

        }
    }

    public static int getYawIncrementMultiplier() {
        return PlayerManipulator.yawIncrementMultiplier;
    }

    public static void setYawIncrementMultiplier(int yawIncrementMultiplier) {
        PlayerManipulator.yawIncrementMultiplier = yawIncrementMultiplier;
    }

    public static void setPitchInControl(boolean pitchInControl) {
        PlayerManipulator.pitchInControl = pitchInControl;
        BlockFinder.LOGGER.info("PitchInControl set to: " + PlayerManipulator.pitchInControl);
    }

    public static boolean getPitchInControl() {
        return pitchInControl;
    }



}
