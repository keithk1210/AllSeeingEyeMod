package net.fabricmc.blockfinder.items;

import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.scanning.ChunkScanner;
import net.fabricmc.blockfinder.utils.ProcessType;
import net.fabricmc.blockfinder.utils.SearchType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

public class BaseStructureEye extends BaseEye {

    TagKey<Structure> target;

    public BaseStructureEye(Settings settings,TagKey<Structure> target) {
        super(settings);
        this.target = target;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        super.onUse(playerEntity,hand);
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,Integer.MAX_VALUE,5,false,false,false));
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,Integer.MAX_VALUE,5,false,false,false));
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos blockPos = serverWorld.locateStructure(target,playerEntity.getBlockPos(),100,false);
            PlayerManipulator.beginProcess(playerEntity,blockPos, SearchType.STRUCTURE);
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
