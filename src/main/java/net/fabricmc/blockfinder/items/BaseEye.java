package net.fabricmc.blockfinder.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BaseEye extends Item {
    public BaseEye(Settings settings) {
        super(settings);
    }
    protected void onUse(PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
        playerEntity.sendMessage(Text.literal("The All-Seeing Eye overtakes you..."));
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS,Integer.MAX_VALUE,5,false,false,false));
        if (!playerEntity.getAbilities().creativeMode) {
            playerEntity.getStackInHand(hand).decrement(1);
        }
    }
}
