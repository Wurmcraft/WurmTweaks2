package com.wurmcraft.script.utils.recipe;

import com.wurmcraft.script.utils.StackHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = input.get (width - subX - 1 + subY * width);
					} else {
						target = input.get (subX + subY * width);
					}
				}

				if (!apply (target,inv.getStackInRowAndColumn (x,y))) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean apply (Ingredient matchingStacks,ItemStack recipeStack) {
		boolean check = matchingStacks.apply (recipeStack);
		boolean hasNBT = false;
		for (ItemStack stack : matchingStacks.getMatchingStacks ())
			if (stack.hasTagCompound ())
				hasNBT = true;
		if (hasNBT)
			for (ItemStack stack : matchingStacks.getMatchingStacks ())
				if (isSameIgnoreSize (stack,recipeStack))
					return true;
		return check;
	}

	private boolean isSameIgnoreSize (ItemStack a,ItemStack b) {
		return a.getItem ().equals (b.getItem ()) && a.getTagCompound () == b.getTagCompound () && ((a.getItemDamage () == b.getItemDamage ()) || a.getItemDamage () == Short.MAX_VALUE);
	}
}
