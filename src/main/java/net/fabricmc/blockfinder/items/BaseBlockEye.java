package net.fabricmc.blockfinder.items;

import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.scanning.ChunkScanner;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class BaseBlockEye extends Item {
    HashSet<Block> targets;

    public BaseBlockEye(Settings settings,HashSet<Block> targets) {
        super(settings);
        this.targets = targets;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
        playerEntity.clearActiveItem();
        BlockPos foundBlock = ChunkScanner.circleScan(playerEntity,targets,101);

        if (foundBlock != null) { //might want to make this more concise
            PlayerManipulator.beginProcess(playerEntity,foundBlock);
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
