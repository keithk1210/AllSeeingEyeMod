package net.fabricmc.allseeingeye.blocks;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class PurpleCloud extends BlockWithEntity {

    private long placedTime;

    public PurpleCloud(Settings settings) {
        super(settings);
    };

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PurpleCloudEntity(pos,state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        AllSeeingEye.LOGGER.info("scheduled tick called");
        world.removeBlock(pos,false);
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public <T extends BlockEntity>BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {

        return checkType(type, BlockRegistrationHelper.PURPLE_CLOUD_ENTITY, (world1, pos, state1, be ) -> {
            world.updateListeners(pos,state, state, Block.NOTIFY_LISTENERS);
            PurpleCloudEntity.tick(world1,pos,state1,be);
        });
    }
}
