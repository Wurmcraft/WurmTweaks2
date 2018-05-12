package com.wurmcraft.script.utils.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class DynamicShapelessOreRecipe extends ShapelessOreRecipe {

	public DynamicShapelessOreRecipe (ResourceLocation group,Block result,Object... recipe) {
		super (group,result,recipe);
	}

	public DynamicShapelessOreRecipe (ResourceLocation group,Item result,Object... recipe) {
		super (group,result,recipe);
	}

	public DynamicShapelessOreRecipe (ResourceLocation group,NonNullList <Ingredient> input,@Nonnull ItemStack result) {
		super (group,input,result);
	}

	public DynamicShapelessOreRecipe (ResourceLocation group,@Nonnull ItemStack result,Object... recipe) {
		super (group,result,recipe);
	}

	@Override
	public boolean isDynamic () {
		return true;
	}

	@Override
	public boolean matches (@Nonnull InventoryCrafting inv,@Nonnull World world) {
		return super.matches (inv,world);
	}
}
