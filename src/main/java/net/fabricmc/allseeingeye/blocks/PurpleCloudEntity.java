package net.fabricmc.allseeingeye.blocks;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PurpleCloudEntity extends BlockEntity {

    private static final int TICKS_TO_DISAPPEAR = 20 * 5;
    private static int ticks = 0;
    public PurpleCloudEntity(BlockPos pos, BlockState state) {
        super(BlockRegistrationHelper.PURPLE_CLOUD_ENTITY,pos,state);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, PurpleCloudEntity be) {
    AllSeeingEye.LOGGER.info("tick method called");
    if (!world.isClient) {
        ticks += 1;
        AllSeeingEye.LOGGER.info("tick method called. ticks = " + ticks);
        if (ticks >= TICKS_TO_DISAPPEAR) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    }
}
