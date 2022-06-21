package com.example.examplemod.commands;

import com.example.examplemod.scanning.ChunkScanner;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;


public class MyCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mycommand").executes((command) -> {
            return execute(command);
        }));
    }
    private static int execute(CommandContext<CommandSourceStack> command){
        System.out.println("Command executed");
        if(command.getSource().getEntity() instanceof Player){

            Player player = (Player) command.getSource().getEntity();

            ChunkScanner chunkScanner = new ChunkScanner(player);
            //String blockString = player.getItemInHand(player.swingingArm).getItem().toString();
            chunkScanner.analyzeChunk();

        }
        return Command.SINGLE_SUCCESS;
    }

}

