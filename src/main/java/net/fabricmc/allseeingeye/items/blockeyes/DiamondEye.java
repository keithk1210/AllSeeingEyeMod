package net.fabricmc.allseeingeye.items.blockeyes;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Arrays;
import java.util.HashSet;

public class DiamondEye extends BaseBlockEye {

    public DiamondEye(Settings settings) {
        super(settings, new HashSet<Block>(Arrays.asList(Blocks.DIAMOND_ORE,Blocks.DEEPSLATE_DIAMOND_ORE)),20);
    }

}
