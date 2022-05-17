package com.wurmcraft.wurmtweaks2.common.script.jython.recipes;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import static com.wurmcraft.wurmtweaks2.common.script.data.RecipeUtils.recipeLock;

public class BrewingRecipe {

  public static HashMap<ItemStack, BrewingRecipe> currentRecipes = new HashMap<>();

  public String output;
  public String input;
  public String catalyst;

  public BrewingRecipe(String output, String input, String catalyst) {
    this.output = output;
    this.input = input;
    this.catalyst = catalyst;
    try {
      register();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void register() {
    recipeLock(false);
    // TODO Handle OreDict
    ItemStack outputStack = (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack")
        .getData(output);
    BrewingRecipeRegistry.addRecipe(
        (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(input),
        (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(catalyst),
        outputStack);
    currentRecipes.put(outputStack, this);
    recipeLock(true);
  }
}
