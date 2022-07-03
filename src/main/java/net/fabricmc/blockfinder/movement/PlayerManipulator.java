package net.fabricmc.blockfinder.movement;

import net.fabricmc.blockfinder.BlockFinder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class PlayerManipulator {


    private PlayerEntity player;
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
        BlockFinder.LOGGER.info("Inside movePlayerHead method");
        BlockFinder.LOGGER.info("Player.getHorizontalFacing " + player.getHorizontalFacing());
        BlockFinder.LOGGER.info("Player.getActiveEyeHeight " + player.getActiveEyeHeight(player.getPose(),player.getDimensions(player.getPose())));
        BlockFinder.LOGGER.info("Player.getpitch() " + player.getPitch());
        BlockFinder.LOGGER.info("Player.getYaw() "+ player.getYaw());
    }

    public void setYawIncrement(int yawIncrement) {
        this.yawIncrement = yawIncrement;
    }

    public int getYawIncrement() {
        return this.yawIncrement;
    }

    public void setDirectionToFace(double directionToFace) {
        this.directionToFace = directionToFace;
    }

    public double getDirectionToFace() {
        return this.directionToFace;
    }

    public void addDirection(MovementDirection direction) {
        this.toggled.add(direction);
    }

    public boolean hasDirection(MovementDirection direction) {
        return this.toggled.contains(direction);
    }

    public void setDestination(BlockPos pos) {
        log("Setting destination to: " + pos.getX() + ", " + + pos.getY() + ", " + pos.getZ());
    }

    public BlockPos getDestination() {
        return this.destination;
    }

    public void finish() {
        this.toggled.clear();
    }

    private void log(String str) {
        BlockFinder.LOGGER.info("[PlayerManipulator]: " + str);
    }

    public PlayerEntity getPlayer() {
        return  this.player;
    }




}
