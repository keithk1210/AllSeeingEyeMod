package net.fabricmc.allseeingeye;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.allseeingeye.blocks.BlockRegistrationHelper;
import net.fabricmc.allseeingeye.keybindings.KeybindingRegistrationHelper;
import net.fabricmc.allseeingeye.utils.SearchType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.allseeingeye.commands.*;
import net.fabricmc.allseeingeye.holding.Holding;
import net.fabricmc.allseeingeye.items.ItemRegistrationHelper;
import net.fabricmc.allseeingeye.movement.CardinalDirection;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.fabricmc.allseeingeye.utils.ClickType;
import net.fabricmc.allseeingeye.utils.ProcessType;
import net.fabricmc.allseeingeye.utils.ScanType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
public class AllSeeingEye implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static Holding leftHolding;
	public static Holding rightHolding;
	public static Holding jumpHolding;
	public  static  Holding inventoryHolding;

	private static AllSeeingEye INSTANCE;
	private static HashSet<Holding> holdings = new HashSet<>();

	public static final String MODID = "allseeingeye";

	public AllSeeingEye() {
		INSTANCE = this;
	}

	public static AllSeeingEye getInstance() {
		return INSTANCE;
	}




	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//search registration
		LOGGER.info("AllSeeingEye Initalised");

		ClientLifecycleEvents.CLIENT_STARTED.register(this::clientReady);

		ClientTickEvents.END_CLIENT_TICK.register(this::clientTickEvent);

		ItemRegistrationHelper.register();
		BlockRegistrationHelper.register();
		KeybindingRegistrationHelper.register();

		HashSet<Identifier> lootTables = new HashSet<> (Arrays.asList(LootTables.SIMPLE_DUNGEON_CHEST,LootTables.SHIPWRECK_MAP_CHEST,LootTables.ABANDONED_MINESHAFT_CHEST,LootTables.NETHER_BRIDGE_CHEST,LootTables.RUINED_PORTAL_CHEST,LootTables.BASTION_TREASURE_CHEST));

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && lootTables.contains(id)) {
				LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(ItemRegistrationHelper.ALL_SEEING_BOOTS));
				tableBuilder.pool(poolBuilder);
			}
		});



		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("search")
					.then(CommandManager.literal("chunk").executes(context -> {
						return SearchCommand.runCommand(context, ScanType.CHUNK,0);
					})));
			dispatcher.register(CommandManager.literal("search")
					.then(CommandManager.literal("circle")
							.then(CommandManager.argument("diameter",IntegerArgumentType.integer(1,101)).executes(context -> {
								return SearchCommand.runCommand(context, ScanType.CIRCLE,IntegerArgumentType.getInteger(context,"diameter"));
							}))));
			});
		//lookindirection registration
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->  {
			dispatcher.register(CommandManager.literal("lookindirection")
					.then(CommandManager.literal("north").executes(context -> {
						return LookCommand.runCommand(context, CardinalDirection.NORTH);
					})));
			dispatcher.register(CommandManager.literal("lookindirection")
					.then(CommandManager.literal("east").executes(context -> {
						return LookCommand.runCommand(context, CardinalDirection.EAST);
					})));
			dispatcher.register(CommandManager.literal("lookindirection")
					.then(CommandManager.literal("south").executes(context -> {
						return LookCommand.runCommand(context, CardinalDirection.SOUTH);
					})));
			dispatcher.register(CommandManager.literal("lookindirection")
					.then(CommandManager.literal("west").executes(context -> {
						return LookCommand.runCommand(context, CardinalDirection.WEST);
					})));
		});
		//drawcircle registration
		CommandRegistrationCallback.EVENT.register((((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("drawcircle")
					.then(CommandManager.argument("diameter",IntegerArgumentType.integer(0,51)).executes(context -> {
						return DrawCircleCommand.runCommand(context,IntegerArgumentType.getInteger(context,"diameter"));
			})));
		})));
		//abort command
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("abort").executes(context -> {
				return AbortCommand.runCommand(context);
			}));
		}));
		//click command
		CommandRegistrationCallback.EVENT.register((((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("click")
					.then(CommandManager.literal("left").executes(context -> {
						return ClickCommand.runCommand(ClickType.LEFT);
					})));
			dispatcher.register(CommandManager.literal("click")
					.then(CommandManager.literal("right").executes(context -> {
						return ClickCommand.runCommand(ClickType.RIGHT);
					})));
			dispatcher.register(CommandManager.literal("click")
					.then(CommandManager.literal("stop").executes(context -> {
						return ClickCommand.runCommand(ClickType.STOP);
					})));



		})));
	}

	private void clientReady(MinecraftClient client) {
		leftHolding = new Holding(client.options.attackKey);
		rightHolding = new Holding(client.options.useKey);
		jumpHolding = new Holding(client.options.jumpKey);
		inventoryHolding = new Holding(client.options.inventoryKey);
		holdings.add(leftHolding);
		holdings.add(rightHolding);
		holdings.add(jumpHolding);
	}

	private void clientTickEvent(MinecraftClient mc) {
		if (PlayerManipulator.getCurrentProcess() != null) {
			PlayerManipulator.checkIfDead();
			if (KeybindingRegistrationHelper.stopKey.wasPressed()) {
				PlayerManipulator.endAllProcesses();
				PlayerManipulator.getPlayer().sendMessage(Text.literal("You escaped the grasp of the All-Seeing eye."));
			}
			if (PlayerManipulator.getCurrentProcess() == ProcessType.VERTICAL_DOWN) { //assuming all downards motion requires breaking
				PlayerManipulator.checkIfVerticalPositionReached();
				leftHolding.getKey().setPressed(true);

			}
			if (PlayerManipulator.getCurrentProcess() == ProcessType.VERTICAL_UP) {//assuming all upwards motion requires placing
				PlayerManipulator.checkIfVerticalPositionReached();
				rightHolding.getKey().setPressed(true);
				jumpHolding.getKey().setPressed(true);
			}
			if (PlayerManipulator.getCurrentProcess() == ProcessType.HORIZONTAL) {
				PlayerManipulator.checkIfHorizontalPositionReached();
			}
			if (PlayerManipulator.getCurrentProcess() != ProcessType.VERTICAL_FLOAT && PlayerManipulator.getCurrentSearchType() == SearchType.STRUCTURE) {
				PlayerEntity player = mc.player;
				if (player.getWorld().getBlockState(player.getBlockPos().down()) != null && player.getWorld().getBlockState(player.getBlockPos().down()).equals(Blocks.AIR.getDefaultState())) {
					player.getWorld().setBlockState(player.getBlockPos().down(),BlockRegistrationHelper.PURPLE_CLOUD.getDefaultState());
					player.getWorld().setBlockState(player.getBlockPos().down().north(),BlockRegistrationHelper.PURPLE_CLOUD.getDefaultState());
					player.getWorld().setBlockState(player.getBlockPos().down().east(),BlockRegistrationHelper.PURPLE_CLOUD.getDefaultState());
					player.getWorld().setBlockState(player.getBlockPos().down().south(),BlockRegistrationHelper.PURPLE_CLOUD.getDefaultState());
					player.getWorld().setBlockState(player.getBlockPos().down().west(),BlockRegistrationHelper.PURPLE_CLOUD.getDefaultState());
				}
			}
			if (PlayerManipulator.getCurrentProcess() == ProcessType.VERTICAL_FLOAT) {
				if (mc.player.getBlockY() >= 257) {
					mc.player.clearStatusEffects();
					PlayerManipulator.setCurrentProcess(ProcessType.ANGULAR_YAW);
				}
			}
		}
		}




		public static void clearHoldings() {
		for (Holding holding: holdings) {
			holding.getKey().setPressed(false);
		}
		LOGGER.info("Holdings cleared");
	}


}

