package com.wurmcraft.wurmtweaks.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class DynamicShapedOreRecipe extends ShapedOreRecipe {

	public DynamicShapedOreRecipe (ResourceLocation group,@Nonnull ItemStack result,Object... recipe) {
		super (group,result,recipe);
	}

	@Override
	public boolean isDynamic () {
		return true;
	}
}
