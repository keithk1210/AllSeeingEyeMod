package net.fabricmc.blockfinder;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.blockfinder.commands.AbortCommand;
import net.fabricmc.blockfinder.commands.DrawCircleCommand;
import net.fabricmc.blockfinder.commands.LookCommand;
import net.fabricmc.blockfinder.commands.SearchCommand;
import net.fabricmc.blockfinder.movement.CardinalDirection;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.utils.SearchType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class BlockFinder implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final MinecraftClient client = MinecraftClient.getInstance();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//search registration
		LOGGER.info("Hello Fabric world!");
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("search")
					.then(CommandManager.literal("chunk").executes(context -> {
						return SearchCommand.runCommand(context, SearchType.CHUNK,0);
					})));
			dispatcher.register(CommandManager.literal("search")
					.then(CommandManager.literal("circle")
							.then(CommandManager.argument("diameter",IntegerArgumentType.integer(1,51)).executes(context -> {
								return SearchCommand.runCommand(context, SearchType.CIRCLE,IntegerArgumentType.getInteger(context,"diameter"));
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

	}


}

