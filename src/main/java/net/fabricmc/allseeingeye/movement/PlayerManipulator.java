package net.fabricmc.allseeingeye.movement;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.items.ItemRegistrationHelper;
import net.fabricmc.allseeingeye.objects.PlayerHeadMovement;
import net.fabricmc.allseeingeye.utils.ProcessType;
import net.fabricmc.allseeingeye.utils.SearchType;
import net.fabricmc.allseeingeye.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        AllSeeingEye.LOGGER.info("Inside movePlayerHead method");
        AllSeeingEye.LOGGER.info("Player.getHorizontalFacing " + player.getHorizontalFacing());
        AllSeeingEye.LOGGER.info("Player.getActiveEyeHeight " + player.getActiveEyeHeight(player.getPose(),player.getDimensions(player.getPose())));
        AllSeeingEye.LOGGER.info("Player.getpitch() " + player.getPitch());
        AllSeeingEye.LOGGER.info("Player.getYaw() "+ player.getYaw());
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
        setCurrentSearchType(searchType);
        PlayerManipulator.setDestination(foundBlock); //add one to 1 because the player will end up on top of the block
        if (searchType == SearchType.BLOCK) {
            PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
        } else if (searchType == SearchType.STRUCTURE) {
            PlayerManipulator.setCurrentProcess(ProcessType.VERTICAL_FLOAT);
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION,Integer.MAX_VALUE,10,false,false,false));
        }
    }

    public static void clearToggled() {
        toggled.clear();
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
        AllSeeingEye.LOGGER.info("Current head movements:");
        for (PlayerHeadMovement direction : headMovements) {
            AllSeeingEye.LOGGER.info(direction.toString());
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
        AllSeeingEye.LOGGER.info("[PlayerManipulator]: " + str);
    }

    public static PlayerEntity getPlayer() {
        return  PlayerManipulator.player;
    }

    public static void setPlayer(PlayerEntity player) {PlayerManipulator.player = player; }

    public static void checkIfHorizontalPositionReached() { //there is a lot of redundant code here
        if (currentProcess == ProcessType.HORIZONTAL && player != null && destination != null && headMovements.size() > 0) {
            double angle = headMovements.peek().getDestinationAngle();
            if (getCurrentSearchType() == SearchType.BLOCK) {
                checkIfBlocksInTheWay();
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
                            AllSeeingEye.LOGGER.info("Horizontal position reached. Settings process to: " + currentProcess);
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
                            AllSeeingEye.LOGGER.info("Horizontal position reached. Settings process to: " + currentProcess);
                        }
                    }
                }
            } else if (getCurrentSearchType() == SearchType.STRUCTURE) {
                double xDiff = Math.abs(player.getBlockX() - destination.getX());
                double zDiff = Math.abs(player.getBlockZ() - destination.getZ());

                if (xDiff <= 16 && zDiff <= 16) {
                    toggled.clear();

                    printHeadMovements();
                    AllSeeingEye.LOGGER.info("Player arrived at structure");
                    Iterator<ItemStack> iterator = player.getArmorItems().iterator();
                    while (iterator.hasNext()) {
                        ItemStack itemStack = iterator.next();
                        if (itemStack.getItem().toString().equals(ItemRegistrationHelper.ALL_SEEING_BOOTS.toString())) {
                            itemStack.getEnchantments().clear();
                            AllSeeingEye.LOGGER.info("frost walker cleared on player");
                        }
                    }
                    endAllProcesses();
                }

            }
        }
    }

    public static void checkIfBlocksInTheWay() {
        int direction = (int) getHeadMovements().peek().getDestinationAngle();
        boolean blocksInTheWay = false;
        if (direction == CardinalDirection.NORTH.getDegrees()) {
            if (!player.getWorld().getBlockState(player.getBlockPos().north()).equals(Blocks.AIR.getDefaultState()) && !player.getWorld().getBlockState(player.getBlockPos().north().up()).equals(Blocks.AIR.getDefaultState())) {
                blocksInTheWay = true;
            }
        } else if (direction == CardinalDirection.EAST.getDegrees()) {
            if (!player.getWorld().getBlockState(player.getBlockPos().east()).equals(Blocks.AIR.getDefaultState()) && !player.getWorld().getBlockState(player.getBlockPos().east().up()).equals(Blocks.AIR.getDefaultState())) {
                blocksInTheWay = true;
            }
        } else if (direction == CardinalDirection.SOUTH.getDegrees()) {
            if (!player.getWorld().getBlockState(player.getBlockPos().south()).equals(Blocks.AIR.getDefaultState()) && !player.getWorld().getBlockState(player.getBlockPos().south().up()).equals(Blocks.AIR.getDefaultState())) {
                blocksInTheWay = true;
            }
        } else if (direction == CardinalDirection.WEST.getDegrees()) {
            if (!player.getWorld().getBlockState(player.getBlockPos().south()).equals(Blocks.AIR.getDefaultState()) && !player.getWorld().getBlockState(player.getBlockPos().south().up()).equals(Blocks.AIR.getDefaultState())) {
                blocksInTheWay = true;
            }
        }
    }

    public static void checkIfDead() {
        if (player.isDead()) {
            endAllProcesses();
            AllSeeingEye.LOGGER.info("Processes ended. Reason: player death");
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
        AllSeeingEye.LOGGER.info("Current head movements");
        for (PlayerHeadMovement hm: headMovements) {
            AllSeeingEye.LOGGER.info(hm.toString());
        }
    }


    public static void setIsRightClickDown(boolean bool) {
        isRightClickDown = bool;
        AllSeeingEye.LOGGER.info("Is right click down? " + isRightClickDown);
    }

    public static void setIsLeftClickDown(boolean bool) {
        isLeftClickDown = bool;
        AllSeeingEye.LOGGER.info("Is left click down? " + isLeftClickDown);
    }

    public static void setCurrentProcess(ProcessType currentProcess) {
        if (PlayerManipulator.getCurrentProcess() == ProcessType.VERTICAL_FLOAT) {
            getPlayer().clearStatusEffects();
        }
        PlayerManipulator.currentProcess = currentProcess;
        AllSeeingEye.LOGGER.info("Current process set to: " + PlayerManipulator.currentProcess);
    }

    public static ProcessType getCurrentProcess() {
        return currentProcess;
    }

    public static void endAllProcesses() {
        setCurrentProcess(null);
        setCurrentSearchType(null);
        AllSeeingEye.clearHoldings();
        toggled.clear();
        getPlayer().clearStatusEffects();
        PlayerManipulator.getHeadMovements().clear();
    }

    public static void setCurrentSearchType(SearchType currentSearchType) {
        PlayerManipulator.currentSearchType = currentSearchType;
    }

    public static SearchType getCurrentSearchType() {
        return currentSearchType;
    }

    public static BlockPos getDestination() {
        return destination;
    }
}
