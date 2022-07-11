package net.fabricmc.allseeingeye.items;

import net.fabricmc.allseeingeye.items.armor.AllSeeingArmorMaterial;
import net.fabricmc.allseeingeye.items.blockeyes.DiamondEye;
import net.fabricmc.allseeingeye.items.structureeyes.MineshaftEye;
import net.fabricmc.allseeingeye.items.structureeyes.VillageEye;
import net.fabricmc.allseeingeye.items.structureeyes.WoodlandMansionEye;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistrationHelper {

    public static final ItemGroup EYE_GROUP = FabricItemGroupBuilder.create(
                    new Identifier("allseeingeye", "other"))
            .icon(() -> new ItemStack(Items.ENDER_EYE))
            .build();

    //Block eyes
    public static final Item DIAMOND_EYE = new DiamondEye(new FabricItemSettings().group(EYE_GROUP));
    //Structure Eyes
    public static final Item VILLAGE_EYE = new VillageEye(new FabricItemSettings().group(EYE_GROUP));
    public static final Item MINESHAFT_EYE = new MineshaftEye(new FabricItemSettings().group(EYE_GROUP));
    public static final Item WOODLAND_MANSION_EYE = new WoodlandMansionEye(new FabricItemSettings().group(EYE_GROUP));
    public static final ArmorMaterial ALL_SEEING_ARMOR_MATERIAL = new AllSeeingArmorMaterial();
    public static final Item ALL_SEEING_BOOTS = new ArmorItem(ALL_SEEING_ARMOR_MATERIAL, EquipmentSlot.FEET,new FabricItemSettings().group(EYE_GROUP));

    public ItemRegistrationHelper() {}

    public static void register() {
        //Block eyes
        Registry.register(Registry.ITEM,new Identifier("allseeingeye","diamond_eye"), DIAMOND_EYE);
        //Structure eyes
        Registry.register(Registry.ITEM,new Identifier("allseeingeye","village_eye"),VILLAGE_EYE);
        Registry.register(Registry.ITEM,new Identifier("allseeingeye","mineshaft_eye"),MINESHAFT_EYE);
        Registry.register(Registry.ITEM,new Identifier("allseeingeye","all_seeing_boots"),ALL_SEEING_BOOTS);
        Registry.register(Registry.ITEM,new Identifier("allseeingeye","woodland_mansion_eye"),WOODLAND_MANSION_EYE);
    }
}
