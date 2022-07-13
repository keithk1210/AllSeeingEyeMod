package net.fabricmc.allseeingeye.blocks;

import net.fabricmc.allseeingeye.items.ItemRegistrationHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistrationHelper {

    public BlockRegistrationHelper() {}

    public static final Block PURPLE_CLOUD = new Block(FabricBlockSettings.of(Material.WOOL).strength(.1f));
    public static BlockEntityType<PurpleCloudEntity> PURPLE_CLOUD_ENTITY;

    public static void register() {

        String namespace = ItemRegistrationHelper.NAMESPACE;
        Registry.register(Registry.BLOCK,new Identifier(namespace,"purple_cloud"),PURPLE_CLOUD);
        Registry.register(Registry.ITEM, new Identifier(namespace,"purple_cloud"),new BlockItem(PURPLE_CLOUD,new FabricItemSettings().group(ItemRegistrationHelper.EYE_GROUP)));
        PURPLE_CLOUD_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "allseeingeye:purple_cloud_entity", FabricBlockEntityTypeBuilder.create(PurpleCloudEntity::new, PURPLE_CLOUD).build(null));
    }


}
