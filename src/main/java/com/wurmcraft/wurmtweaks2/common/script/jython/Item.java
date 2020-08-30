package com.wurmcraft.wurmtweaks2.common.script.jython;

import com.google.common.collect.Lists;
import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.common.script.data.InvalidRecipe;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.python.icu.impl.InvalidFormatException;

public class Item {

  public String item;
  private ItemStack stack;

  public Item(String item) throws InvalidFormatException {
    this.item = item;
    try {
      stack = (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(item);
    } catch (Exception e) {
      throw new InvalidFormatException(item + " is not a valid item!");
    }
  }

  public String getName() {
    return stack.getDisplayName();
  }

  public String toString() {
    return WurmTweaks2API.dataConverters.get("ItemStack").toString(stack);
  }

  // TODO Test Recipe Reload
  public void removeRecipe() {
    recipeLock(false);
    ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
    ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());
    for (IRecipe r : recipes) {
      if (r.getRecipeOutput().isItemEqual(stack)) {
        ((ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES).remove(r.getRegistryName());
        ((ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES)
            .register(new InvalidRecipe(stack).setRegistryName(r.getRegistryName()));
        break;
      }
    }
    recipeLock(true);
  }

  private void recipeLock(boolean lock) {
    ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
    if (lock) {
      recipeRegistry.freeze();
    } else {
      recipeRegistry.unfreeze();
    }
  }
}
