package net.fabricmc.allseeingeye.items;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class BaseEye extends Item {
    private final int levelsRequired;
    public BaseEye(Settings settings,int levelsRequired) {

        super(settings);
        this.levelsRequired = levelsRequired;
    }
    protected boolean onUse(PlayerEntity playerEntity, Hand hand) { //returns true if the player has the necessary materials
        Iterator<ItemStack> itemStackIterator = playerEntity.getArmorItems().iterator();
        boolean hasAllSeeingBoots = false;
        boolean hasEnoughXP = false;
        while (itemStackIterator.hasNext()) {
            ItemStack item = itemStackIterator.next();
            if (item.getItem().toString().equals(ItemRegistrationHelper.ALL_SEEING_BOOTS.toString())) {
                AllSeeingEye.LOGGER.info("Player was wearing all seeing boots");
                if (!item.getEnchantments().contains(Enchantments.FROST_WALKER)) {
                    item.addEnchantment(Enchantments.FROST_WALKER,2);
                }
                hasAllSeeingBoots = true;
            }
        }

        if (!playerEntity.getAbilities().creativeMode && playerEntity.experienceLevel >= levelsRequired) {
            hasEnoughXP = true;
            playerEntity.addExperienceLevels(-levelsRequired);
        }

        if (hasAllSeeingBoots && hasEnoughXP) {
            playerEntity.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
            playerEntity.sendMessage(Text.literal("The All-Seeing Eye overtakes you..."));
            playerEntity.sendMessage(Text.literal("Press \"R\" to stop"));
            //playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS,Integer.MAX_VALUE,5,false,false,false));
            //playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,Integer.MAX_VALUE,5,false,false,false));
            if (!playerEntity.getAbilities().creativeMode) {
                playerEntity.getStackInHand(hand).decrement(1);
            }
            return true;
        } else if (!hasAllSeeingBoots && hasEnoughXP) {
            playerEntity.sendMessage(Text.literal("Without a pair of all-seeing boots, the Eye is blind."));
            return false;
        } else if (hasAllSeeingBoots && !hasEnoughXP) {
            playerEntity.sendMessage(Text.literal("The Eye believes you are too weak. It requires " + levelsRequired + " XP levels"));
        } else {
            playerEntity.sendMessage(Text.literal("The Eye does not service those without a pair of all-seeing boots and those without enough XP."));
            return false;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        // formatted red text
        tooltip.add(Text.translatable("item.allseeingeye.base_eye.tooltip").formatted(Formatting.RED));
    }
}
