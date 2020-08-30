package com.wurmcraft.wurmtweaks2.common.script.data;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import org.python.icu.impl.InvalidFormatException;

public class RecipeUtils {

  public static NonNullList<Ingredient> createIngredientList(String input)
      throws InvalidFormatException {
    NonNullList<Ingredient> list = NonNullList.create();
    for (String stack : input.split(" ")) {
      try {
        list.add(Ingredient.fromStacks(
            (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(stack)));
      } catch (Exception e) {
        throw new InvalidFormatException(stack + " is not valid");
      }
    }
    return list;
  }

}
