package net.fabricmc.blockfinder.items;

import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.scanning.ChunkScanner;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.StructureTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashSet;

public class BaseStructureEye extends Item {

    TagKey<Structure> target;

    public BaseStructureEye(Settings settings,TagKey<Structure> target) {
        super(settings);
        this.target = target;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
        playerEntity.clearActiveItem();
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos blockPos = serverWorld.locateStructure(target,playerEntity.getBlockPos(),100,false);
            PlayerManipulator.beginProcess(playerEntity,blockPos);

        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
