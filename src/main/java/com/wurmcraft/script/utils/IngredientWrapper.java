package com.wurmcraft.script.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
  A Simple Ingredient Wrapper
 */
public class IngredientWrapper extends Ingredient {

    public IngredientWrapper (ItemStack... stacks) {
        super (stacks);
    }
}
