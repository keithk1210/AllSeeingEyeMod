package net.fabricmc.blockfinder.movement;

import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.objects.PlayerHeadMovement;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.fabricmc.blockfinder.utils.SearchType;
import net.fabricmc.blockfinder.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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


    private static boolean isLeftClickDown = false;
    private static boolean isRightClickDown = false;

    private static SearchType currentSearchType;
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

    public static void beginProcess(PlayerEntity playerEntity, BlockPos foundBlock, SearchType searchType) {
        PlayerManipulator.setPlayer(playerEntity);
        PlayerManipulator.setDestination(foundBlock); //add one to 1 because the player will end up on top of the block
        setCurrentSearchType(searchType);
        PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
    }

    public static void setDestination(BlockPos pos) {
        log("Setting destination to: " + pos.getX() + ", " + + pos.getY() + ", " + pos.getZ());
        PlayerManipulator.destination = pos;
        if (getCurrentSearchType() == SearchType.BLOCK) {
            if (player.getBlockPos().getX() > pos.getX()) {
                PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.WEST.getDegrees(), Utils.getIdealYawIncrement(player.getYaw(), CardinalDirection.WEST.getDegrees()));
                headMovements.add(headMovement);
            } else {
                PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.EAST.getDegrees(), Utils.getIdealYawIncrement(player.getYaw(), CardinalDirection.EAST.getDegrees()));
                headMovements.add(headMovement);
            }

            double lastAngle = headMovements.peek().getDestinationAngle();

            if (player.getBlockPos().getZ() > pos.getZ()) {
                PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.NORTH.getDegrees(), Utils.getIdealYawIncrement(lastAngle, CardinalDirection.NORTH.getDegrees()));
                headMovements.add(headMovement);
            } else {
                PlayerHeadMovement headMovement = new PlayerHeadMovement(CardinalDirection.SOUTH.getDegrees(), Utils.getIdealYawIncrement(lastAngle, CardinalDirection.SOUTH.getDegrees()));
                headMovements.add(headMovement);
            }


        } else if (getCurrentSearchType() == SearchType.STRUCTURE) {
            double angle = Utils.getAngle(player.getYaw(),destination.getX()-player.getBlockX(),destination.getZ()-player.getBlockZ());
            PlayerHeadMovement playerHeadMovement = new PlayerHeadMovement(angle,Utils.getIdealYawIncrement(player.getYaw(),angle));
            headMovements.add(playerHeadMovement);
        }
        BlockFinder.LOGGER.info("Current head movements:");
        for (PlayerHeadMovement direction : headMovements) {
            BlockFinder.LOGGER.info(direction.toString());
        }
    }

    public static void terminateHorizontalMovement() {

        PlayerManipulator.toggled.clear();
        PlayerManipulator.destination = null;
        PlayerManipulator.yawIncrementMultiplier = 10;
    }

    public static double getAllowedYawDiscrepancy() {
        if (getCurrentSearchType() == SearchType.STRUCTURE) {
            return 0.5;
        } else if (getCurrentSearchType() == SearchType.BLOCK) {
            return 1.0;
        }
        return 0.0;
    }

    private static void log(String str) {
        BlockFinder.LOGGER.info("[PlayerManipulator]: " + str);
    }

    public static PlayerEntity getPlayer() {
        return  PlayerManipulator.player;
    }

    public static void setPlayer(PlayerEntity player) {PlayerManipulator.player = player; }

    public static void checkIfHorizontalPositionReached() { //there is a lot of redundant code here
        if (getCurrentSearchType() == SearchType.STRUCTURE) {
            if (player.getWorld().getBlockState(player.getBlockPos().down()).equals(Blocks.WATER.getDefaultState())) {
                player.getWorld().setBlockState(player.getBlockPos().down(),Blocks.ICE.getDefaultState());
                player.getWorld().setBlockState(player.getBlockPos().down().north(),Blocks.ICE.getDefaultState());
                player.getWorld().setBlockState(player.getBlockPos().down().east(),Blocks.ICE.getDefaultState());
                player.getWorld().setBlockState(player.getBlockPos().down().south(),Blocks.ICE.getDefaultState());
                player.getWorld().setBlockState(player.getBlockPos().down().west(),Blocks.ICE.getDefaultState());
            }
        }
        if (currentProcess == ProcessType.HORIZONTAL && player != null && destination != null && headMovements.size() > 0) {
            double angle = headMovements.peek().getDestinationAngle();
            if (getCurrentSearchType() == SearchType.BLOCK) {
                if (Double.compare(angle, CardinalDirection.EAST.getDegrees()) == 0 || Double.compare(angle, CardinalDirection.WEST.getDegrees()) == 0) {
                    //for block search
                    if (player.getBlockX() == destination.getX()) {
                        toggled.clear();
                        PlayerManipulator.getHeadMovements().remove();
                        log("Toggled was cleared");
                        printHeadMovements();
                        if (headMovements.size() > 0) {
                            PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
                        } else {
                            PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_PITCH);
                            BlockFinder.LOGGER.info("Horizontal position reached. Settings process to: " + currentProcess);
                        }
                        //for structure search
                    }
                } else if (Double.compare(angle, CardinalDirection.NORTH.getDegrees()) == 0 || Double.compare(angle, CardinalDirection.SOUTH.getDegrees()) == 0) {
                    if (player.getBlockZ() == destination.getZ()) {
                        toggled.clear();
                        PlayerManipulator.getHeadMovements().remove();
                        log("Toggled was cleared");
                        printHeadMovements();
                        if (headMovements.size() > 0) {
                            PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
                        } else {
                            PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_PITCH);
                            BlockFinder.LOGGER.info("Horizontal position reached. Settings process to: " + currentProcess);
                        }
                    }
                }
            } else if (getCurrentSearchType() == SearchType.STRUCTURE) {
                double xDiff = Math.abs(player.getBlockX() - destination.getX());
                double zDiff = Math.abs(player.getBlockZ() - destination.getZ());
                if (xDiff <= 16 && zDiff <= 16) {
                    toggled.clear();
                    PlayerManipulator.getHeadMovements().remove();
                    printHeadMovements();
                    BlockFinder.LOGGER.info("Player arrived at structure");
                    endAllProcesses();
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

    public static Queue<PlayerHeadMovement> getHeadMovements() {
        return headMovements;
    }

    public static void printHeadMovements() {
        BlockFinder.LOGGER.info("Current head movements");
        for (PlayerHeadMovement hm: headMovements) {
            BlockFinder.LOGGER.info(hm.toString());
        }
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
        setCurrentSearchType(null);
        BlockFinder.clearHoldings();
        getPlayer().clearStatusEffects();
    }

    public static void setCurrentSearchType(SearchType currentSearchType) {
        PlayerManipulator.currentSearchType = currentSearchType;
    }

    public static SearchType getCurrentSearchType() {
        return currentSearchType;
    }


}
