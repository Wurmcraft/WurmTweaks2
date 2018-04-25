package com.wurmcraft.wurmtweaks.utils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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

	@Override
	protected boolean checkMatch (InventoryCrafting inv,int startX,int startY,boolean mirror) {
		for (int x = 0; x < inv.getWidth (); x++) {
			for (int y = 0; y < inv.getHeight (); y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;
				if (subX >= 0 && subY >= 0 && subX < width && subY < height)
					if (mirror) {
						target = input.get (width - subX - 1 + subY * width);
					} else {
						target = input.get (subX + subY * width);
					}
				if (!isSame (target,inv.getStackInRowAndColumn (x,y)))
					return false;
			}
		}
		return true;
	}

	private boolean isSame (Ingredient test,ItemStack stack) {
		if (stack == null)
			return false;
		else
			for (ItemStack itemstack : test.getMatchingStacks ())
				if (itemstack.getItem () == stack.getItem ())
					return StackHelper.convert (itemstack,0).equals (StackHelper.convert (stack,0));
		return false;
	}
}
