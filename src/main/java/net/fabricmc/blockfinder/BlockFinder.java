package net.fabricmc.blockfinder;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.blockfinder.commands.*;
import net.fabricmc.blockfinder.holding.Holding;
import net.fabricmc.blockfinder.items.DiamondEye;
import net.fabricmc.blockfinder.items.DiamondEye;
import net.fabricmc.blockfinder.items.ItemRegistrationHelper;
import net.fabricmc.blockfinder.movement.CardinalDirection;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.utils.ClickType;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.fabricmc.blockfinder.utils.ScanType;
import net.fabricmc.blockfinder.utils.SearchType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.callback.Callback;
import java.util.HashSet;
public class BlockFinder implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static Holding leftHolding;
	public static Holding rightHolding;
	public static Holding jumpHolding;
	public  static  Holding inventoryHolding;

	private static BlockFinder INSTANCE;
	private static HashSet<Holding> holdings = new HashSet<>();

	public static final String MODID = "blockfinder";

	public BlockFinder() {
		INSTANCE = this;
	}

	public static BlockFinder getInstance() {
		return INSTANCE;
	}




	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//search registration
		LOGGER.info("BlockFinder Initalised");

		ClientLifecycleEvents.CLIENT_STARTED.register(this::clientReady);

		ClientTickEvents.END_CLIENT_TICK.register(this::clientTickEvent);

		ItemRegistrationHelper.register();



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
		}
		}

		public static void clearHoldings() {
		for (Holding holding: holdings) {
			holding.getKey().setPressed(false);
		}
		LOGGER.info("Holdings cleared");
	}


}

