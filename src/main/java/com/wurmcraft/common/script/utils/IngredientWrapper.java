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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    ItemStack[] matchingStacks = getMatchingStacks();
    for (int index = 0; index < matchingStacks.length; index++) {
      ItemStack stack = matchingStacks[index];
      builder.append(stack.toString());
      if (index < matchingStacks.length - 1) {
        builder.append(", ");
      }
    }
    if (builder.toString().length() <= 0) return "INVALID";
    return builder.toString();
  }
}
