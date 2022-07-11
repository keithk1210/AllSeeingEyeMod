package net.fabricmc.allseeingeye.items;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.minecraft.enchantment.Enchantments;
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

import java.util.Iterator;

public class BaseEye extends Item {
    public BaseEye(Settings settings) {
        super(settings);
    }
    protected boolean onUse(PlayerEntity playerEntity, Hand hand) { //returns true if the player has the necessary materials
        Iterator<ItemStack> itemStackIterator = playerEntity.getArmorItems().iterator();
        boolean hasAllSeeingBoots = false;
        while (itemStackIterator.hasNext()) {
            ItemStack item = itemStackIterator.next();
            if (item.getItem().toString().equals(ItemRegistrationHelper.ALL_SEEING_BOOTS.toString())) {
                AllSeeingEye.LOGGER.info("Player was wearing all seeing boots");
                item.addEnchantment(Enchantments.FROST_WALKER,2);
                hasAllSeeingBoots = true;
            }
        }
        if (hasAllSeeingBoots) {
            playerEntity.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
            playerEntity.sendMessage(Text.literal("The All-Seeing Eye overtakes you..."));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS,Integer.MAX_VALUE,5,false,false,false));
            if (!playerEntity.getAbilities().creativeMode) {
                playerEntity.getStackInHand(hand).decrement(1);
            }
            return true;
        } else {
            playerEntity.sendMessage(Text.literal("Without a pair of all-seeing boots, the Eye is blind."));
            return false;
        }

    }
}
