package com.wurmcraft.common.blocks;

import com.wurmcraft.common.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class WurmTweaksBlocks {

    public static Block transparentAluminum;
    public static Block stoneMagic;
    public static Block logMagic;

    public static void register () {
        register (transparentAluminum = new BlockTransparentAluminum (Material.GLASS),"transparentAluminum");
        register (stoneMagic = new BlockBasic (Material.ROCK),"stoneMagic");
        register (logMagic = new BlockBasic (Material.WOOD), "logMagic");
    }

    private static Block register (Block block,String name) {
        Registry.registerBlock (block,name);
        return block;
    }
}
