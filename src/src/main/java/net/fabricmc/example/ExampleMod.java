package net.fabricmc.example;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.commands.LookCommand;
import net.fabricmc.example.commands.SearchCommand;
import net.fabricmc.example.movement.CardinalDirection;
import net.fabricmc.example.movement.MovementDirection;
import net.fabricmc.example.scanning.ChunkScanner;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
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

		//mycommand registration
		LOGGER.info("Hello Fabric world!");
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("mycommand").executes(context -> {
				return SearchCommand.runCommand(context);
			}));
		}));
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

	}
}
