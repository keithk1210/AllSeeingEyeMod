package net.fabricmc.blockfinder.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistrationHelper {

    public static final ItemGroup EYE_GROUP = FabricItemGroupBuilder.create(
                    new Identifier("blockfinder", "other"))
            .icon(() -> new ItemStack(Items.ENDER_EYE))
            .build();

    //Block eyes
    public static final Item DIAMOND_EYE = new DiamondEye(new FabricItemSettings().group(EYE_GROUP));
    //Structure Eyes
    public static final Item VILLAGE_EYE = new VillageEye(new FabricItemSettings().group(EYE_GROUP));
    public static final Item MINESHAFT_EYE = new MineshaftEye(new FabricItemSettings().group(EYE_GROUP));

    public ItemRegistrationHelper() {}

    public static void register() {
        //Block eyes
        Registry.register(Registry.ITEM,new Identifier("blockfinder","diamond_eye"), DIAMOND_EYE);
        //Structure eyes
        Registry.register(Registry.ITEM,new Identifier("blockfinder","village_eye"),VILLAGE_EYE);
        Registry.register(Registry.ITEM,new Identifier("blockfinder","mineshaft_eye"),MINESHAFT_EYE);
    }
}
