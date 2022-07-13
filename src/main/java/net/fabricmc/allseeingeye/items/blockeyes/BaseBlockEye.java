package net.fabricmc.allseeingeye.items.blockeyes;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.items.BaseEye;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.fabricmc.allseeingeye.scanning.ChunkScanner;
import net.fabricmc.allseeingeye.utils.ProcessType;
import net.fabricmc.allseeingeye.utils.SearchType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class BaseBlockEye extends BaseEye {
    HashSet<Block> targets;

    public BaseBlockEye(Settings settings,HashSet<Block> targets, int levelsRequired) {
        super(settings,levelsRequired);
        this.targets = targets;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        AllSeeingEye.LOGGER.info("[block]: world.isClient: " + world.isClient);
        if (!world.isClient && super.onUse(playerEntity, hand)) {
            BlockPos foundBlock = ChunkScanner.circleScan(playerEntity, targets, 101);
            AllSeeingEye.LOGGER.info("[block]: foundBlock != null: " +(foundBlock != null));
            if (foundBlock != null) {
                PlayerManipulator.beginProcess(playerEntity, foundBlock, SearchType.BLOCK);
            }
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.fail(playerEntity.getStackInHand(hand));
    }
}
