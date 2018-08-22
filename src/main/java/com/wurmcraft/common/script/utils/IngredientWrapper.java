package com.wurmcraft.common.script.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class IngredientWrapper extends Ingredient {

  public IngredientWrapper(ItemStack... stacks) {
    super(stacks);
  }

  public IngredientWrapper(NonNullList<ItemStack> items) {
    super(items.toArray(new ItemStack[0]));
  }
}
