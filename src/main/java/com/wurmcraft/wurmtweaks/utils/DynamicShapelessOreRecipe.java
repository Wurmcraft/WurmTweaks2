package com.wurmcraft.wurmtweaks.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class DynamicShapelessOreRecipe extends ShapelessOreRecipe {

	public DynamicShapelessOreRecipe (ResourceLocation group,@Nonnull ItemStack result,Object... recipe) {
		super (group,result,recipe);
	}

	@Override
	public boolean isDynamic () {
		return true;
	}
}
