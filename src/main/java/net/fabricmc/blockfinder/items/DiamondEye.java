package net.fabricmc.blockfinder.items;

import net.fabricmc.blockfinder.BlockFinder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;

public class DiamondEye extends BaseBlockEye{

    public DiamondEye(Settings settings) {
        super(settings, new HashSet<Block>(Arrays.asList(Blocks.DIAMOND_ORE,Blocks.DEEPSLATE_DIAMOND_ORE)));
    }

}
