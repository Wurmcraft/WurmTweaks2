package com.wurmcraft.wurmtweaks.utils;

import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientHelper extends Ingredient {

	public IngredientHelper (ItemStack... stacks) {
		super (stacks);
	}

	@Override
	public String toString () {
		List <String> names = new ArrayList <> ();
		for (ItemStack stack : getMatchingStacks ())
			if (!stack.isEmpty ())
				names.add (stack.getUnlocalizedName ());
		return Strings.join (names.toArray (new String[0]),", ");
	}
}
