package com.wurmcraft.common.support.utils;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.common.script.utils.DynamicShapelessOreRecipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;


public class RecipeUtils {

  public static final ResourceLocation RECIPE_GROUP = new ResourceLocation(Global.MODID, "Recipes");

  public static Object[] getShapelessItems(String[] list, Converter converter) {
    List<Object> items = new ArrayList<>();
    for (String line : list) {
      Object convert = converter.convert(line);
      if (convert != null) {
        items.add(convert);
      } else {
        invalidItemStack(line);
        return new Object[0];
      }
    }
    return items.toArray();
  }

  private static void invalidItemStack(String line) {
    WurmTweaks.logger
        .error("Unable to convert '" + line + "' into an ItemStack or OreDictionary Entry!");
  }

  public static IRecipe createShapelessRecipe(ItemStack output, Object[] input) {
    DynamicShapelessOreRecipe recipe = new DynamicShapelessOreRecipe(RECIPE_GROUP, output, input);
    recipe.setRegistryName(Global.MODID, output.toString() + Arrays.hashCode(input));
    return recipe;
  }

}
