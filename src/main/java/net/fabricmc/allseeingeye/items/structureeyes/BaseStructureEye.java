package net.fabricmc.allseeingeye.items.structureeyes;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.items.BaseEye;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.fabricmc.allseeingeye.scanning.ChunkScanner;
import net.fabricmc.allseeingeye.utils.ProcessType;
import net.fabricmc.allseeingeye.utils.SearchType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.StructureTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashSet;

public class BaseStructureEye extends BaseEye {

    TagKey<Structure> target;

    public BaseStructureEye(Item.Settings settings, TagKey<Structure> target,int levelsRequired) {
        super(settings,levelsRequired);
        this.target = target;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        AllSeeingEye.LOGGER.info("[structure]: world.isClient: " + world.isClient);
        if (!world.isClient && super.onUse(playerEntity,hand)) {

            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                BlockPos blockPos = null;
                int checks = 0;
                blockPos = serverWorld.locateStructure(target,playerEntity.getBlockPos(),100,false);
                AllSeeingEye.LOGGER.info("candidate block: " + blockPos + " inhabited time: " + world.getChunk(blockPos).getInhabitedTime());


                if (blockPos == null) {
                    playerEntity.sendMessage(Text.literal("Even the All-Seeing Eye has its limits."));
                    return TypedActionResult.fail(playerEntity.getStackInHand(hand));
                }

                AllSeeingEye.LOGGER.info("[structure]: blockPos != null: " +(blockPos != null));
                playerEntity.sendMessage(Text.literal("Destination: " + blockPos.getX() + ", " + blockPos.getZ()));

                PlayerManipulator.beginProcess(playerEntity,blockPos, SearchType.STRUCTURE);
            }
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.fail(playerEntity.getStackInHand(hand));
    }

}
