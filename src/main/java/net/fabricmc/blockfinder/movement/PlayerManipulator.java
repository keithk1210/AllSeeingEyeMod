package net.fabricmc.blockfinder.movement;

import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.objects.PlayerHeadMovement;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.fabricmc.blockfinder.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class PlayerManipulator {


    private static PlayerEntity player;
    private static HashSet<MovementDirection> toggled = new HashSet();
    private static Queue<PlayerHeadMovement> headMovements = new ArrayDeque<>();
    private static int yawIncrement;

    private static int yawIncrementMultiplier = 10;

    public static int allowedYawDiscrepancy = 5;

    private static boolean lookDirectionInControl = false;
    private static boolean pitchInControl = false;
    private static boolean isLeftClickDown = false;
    private static boolean isRightClickDown = false;
    private static ProcessType currentProcess;

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

    public static void addDirection(MovementDirection direction) {
        PlayerManipulator.toggled.add(direction);
    }

    public static boolean hasDirection(MovementDirection direction) {
        return PlayerManipulator.toggled.contains(direction);
    }

    public static void beginProcess(PlayerEntity playerEntity, BlockPos foundBlock) {
        PlayerManipulator.setPlayer(playerEntity);
        PlayerManipulator.setLookDirectionInControl(true);
        PlayerManipulator.setDestination(foundBlock); //add one to 1 because the player will end up on top of the block
        PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
    }

    public static void setDestination(BlockPos pos) {
        log("Setting destination to: " + pos.getX() + ", " + + pos.getY() + ", " + pos.getZ());
        PlayerManipulator.destination = pos;
        if (player.getBlockPos().getX() > pos.getX()) {
            PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.WEST, Utils.getIdealYawIncrement(player.getYaw(),CardinalDirection.WEST.getDegrees()));
            headMovements.add(headMovement);
        } else {
            PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.EAST, Utils.getIdealYawIncrement(player.getYaw(),CardinalDirection.EAST.getDegrees()));
            headMovements.add(headMovement);
        }

        int lastAngle = headMovements.peek().getDestination().getDegrees();

        if (player.getBlockPos().getZ() > pos.getZ()) {
            PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.NORTH, Utils.getIdealYawIncrement(lastAngle,CardinalDirection.NORTH.getDegrees()));
            headMovements.add(headMovement);
        } else {
            PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.SOUTH, Utils.getIdealYawIncrement(lastAngle,CardinalDirection.SOUTH.getDegrees()));
            headMovements.add(headMovement);
        }

        BlockFinder.LOGGER.info("Current head movements:");
        for (PlayerHeadMovement direction : headMovements) {
            BlockFinder.LOGGER.info(direction.toString());
        }
    }

    public static BlockPos getDestination() {
        return PlayerManipulator.destination;
    }

    public static void terminateHorizontalMovement() {

        PlayerManipulator.toggled.clear();
        PlayerManipulator.destination = null;
        PlayerManipulator.lookDirectionInControl = false;
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
        if (currentProcess == ProcessType.HORIZONTAL && player != null && destination != null && headMovements.size() > 0) {
            if (headMovements.peek().getDestination() == CardinalDirection.EAST || headMovements.peek().getDestination() == CardinalDirection.WEST) {

                if (player.getBlockX() == destination.getX()) {
                    toggled.clear();
                    PlayerManipulator.getHeadMovements().remove();
                    log("Toggled was cleared");
                    printHeadMovements();
                    if (headMovements.size() > 0) {
                        PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
                        PlayerManipulator.lookDirectionInControl = true;
                        log("lookDirectionInControl updated to: " + lookDirectionInControl);
                    } else {
                        PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_PITCH);
                        BlockFinder.LOGGER.info("Horizontal position reached. Settings process to: " + currentProcess);
                    }
                }
            } else if (headMovements.peek().getDestination() == CardinalDirection.NORTH || headMovements.peek().getDestination() == CardinalDirection.SOUTH) {
                double zDiff = Math.abs(player.getZ() - (destination.getZ() + .5));
                if (player.getBlockZ() == destination.getZ()) {
                    toggled.clear();
                    PlayerManipulator.getHeadMovements().remove();
                    log("Toggled was cleared");
                    printHeadMovements();
                    if (headMovements.size() > 0) {
                        PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
                        PlayerManipulator.lookDirectionInControl = true;
                        log("lookDirectionInControl updated to: " + lookDirectionInControl);
                    } else {
                        PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_PITCH);
                        BlockFinder.LOGGER.info("Horizontal position reached. Settings process to: " + currentProcess);
                    }
                }
            }



            }
    }

    public static void checkIfVerticalPositionReached() {
        if (currentProcess == ProcessType.VERTICAL_DOWN) {
            if (player.getBlockY() == destination.getY() + 1) { //arrives at the block above the destination
                player.sendMessage(Text.literal("Arrived at: " + player.getBlockX() + ", " + player.getBlockY() + ", " + player.getBlockZ()));
                endAllProcesses();
            }
        } else if (currentProcess == ProcessType.VERTICAL_UP) {
            if (player.getBlockY() == destination.getY() - 2) { //arrives at the block two below the destination so that the destination block is above the players head
                player.sendMessage(Text.literal("Arrived at: " + player.getBlockX() + ", " + player.getBlockY() + ", " + player.getBlockZ()));
                endAllProcesses();
            }
        }
    }

    public static void determineVerticalProcessType() {
        if (player.getBlockY() > destination.getY()) {
            setCurrentProcess(ProcessType.VERTICAL_DOWN);
        } else if (player.getBlockY() < destination.getY()) {
            setCurrentProcess(ProcessType.VERTICAL_UP);
        }
    }

    public static int getYawIncrementMultiplier() {
        return PlayerManipulator.yawIncrementMultiplier;
    }

    public static void setYawIncrementMultiplier(int yawIncrementMultiplier) {
        PlayerManipulator.yawIncrementMultiplier = yawIncrementMultiplier;
        //log("Yaw increment multiplier set to: " + PlayerManipulator.getYawIncrementMultiplier());
    }

    public static void setPitchInControl(boolean pitchInControl) {
        PlayerManipulator.pitchInControl = pitchInControl;
        BlockFinder.LOGGER.info("PitchInControl set to: " + PlayerManipulator.pitchInControl);
    }

    public static boolean getPitchInControl() {
        return pitchInControl;
    }

    public static Queue<PlayerHeadMovement> getHeadMovements() {
        return headMovements;
    }

    public static void printHeadMovements() {
        BlockFinder.LOGGER.info("Current head movements");
        for (PlayerHeadMovement hm: headMovements) {
            BlockFinder.LOGGER.info(hm.toString());
        }
    }

    public static boolean getLookDirectionInControl() {
        return lookDirectionInControl;
    }

    public static void setLookDirectionInControl(boolean lookDirectionInControl) {
        PlayerManipulator.lookDirectionInControl = lookDirectionInControl;
    }

    public static boolean isLeftClickDown() {
        return isLeftClickDown;
    }

    public static boolean isIsRightClickDown() {
        return isRightClickDown;
    }

    public static void setIsRightClickDown(boolean bool) {
        isRightClickDown = bool;
        BlockFinder.LOGGER.info("Is right click down? " + isRightClickDown);
    }

    public static void setIsLeftClickDown(boolean bool) {
        isLeftClickDown = bool;
        BlockFinder.LOGGER.info("Is left click down? " + isLeftClickDown);
    }

    public static void setCurrentProcess(ProcessType currentProcess) {
        PlayerManipulator.currentProcess = currentProcess;
        BlockFinder.LOGGER.info("Current process set to: " + PlayerManipulator.currentProcess);
    }

    public static ProcessType getCurrentProcess() {
        return currentProcess;
    }

    public static void endAllProcesses() {
        setCurrentProcess(null);
        BlockFinder.inventoryHolding.getKey().setPressed(true);
        BlockFinder.clearHoldings();
    }

}
