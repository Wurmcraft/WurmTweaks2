package com.wurmcraft.wurmtweaks2.common.script.data;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import java.util.*;
import java.util.Arrays;
import java.util.HashMap;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.python.antlr.ast.Str;
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

  public static void recipeLock(boolean lock) {
    ForgeRegistry<IRecipe> recipes = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
    if (lock) {
      recipes.freeze();
    } else {
      recipes.unfreeze();
    }
  }

  public static Object[] convertShapedRecipeInputs(String... recipeData) {
    if (recipeData.length == 1) {
      if (recipeData[0].contains(";")) { // Multi-Line Recipe
        String format = recipeData[0].substring(0, recipeData[0].indexOf("<"));
        format = format.trim();
        format = format.substring(0, format.length() - 1).trim();
        String after = format.substring(format.length() - 1) + " " + recipeData[0]
            .substring(recipeData[0].indexOf("<"));
        return convertShapedRecipeInputs(format, after);
      } else if (recipeData[0].contains("*")) {
        // TODO Generate Dynamic Format based on Item Placement
      } else {
        recipeData = recipeData[0].split(" ");
      }
    }
    String[] format = null;
    String[] otherData = null;
    for (int index = 0; index < recipeData.length; index++) {
      String selectedString = recipeData[index];
      if (selectedString.contains("<") || selectedString.contains(">")) {
        format = Arrays.copyOfRange(recipeData, 0, index - 1);
        otherData = Arrays.copyOfRange(recipeData, index - 1, recipeData.length);
        break;
      }
    }
    if (format == null || format.length == 0) {
      // TODO Generate Dynamic Format based on Item Placement
      format = new String[0];
      otherData = new String[0];
    }
    return convertShapedRecipeInputs(Strings.join(format, ";"),
        Strings.join(otherData, " "));
  }

  public static Object[] convertShapedRecipeInputs(String formatInput,
      String recipeData) {
    String[] format = formatInput.split(";");
    HashMap<Character, Object> unformattedRecipeData = collectRecipeData(
        recipeData.split(" "));
    if (isValidRecipeData(unformattedRecipeData)) {
      return formatRecipeData(format, unformattedRecipeData);
    }
    return new Object[0];
  }

  private static HashMap<Character, Object> collectRecipeData(String[] recipeData) {
    if (recipeData.length % 2 == 0) {
      HashMap<Character, Object> data = new HashMap<>();
      for (int index = 0; index < recipeData.length; index += 2) {
        if (recipeData[index].length() == 1) {
          data.put(recipeData[index].charAt(0), convertToStack(recipeData[index + 1]));
        }
      }
      return data;
    }
    return new HashMap<>();
  }

  private static Object[] formatRecipeData(String[] format,
      HashMap<Character, Object> unformattedData) {
    Object[] formattedData = new Object[format.length + (unformattedData.size() * 2)];
    Character[] keys = unformattedData.keySet().toArray(new Character[0]);
    for (int index = 0; index < format.length; index++) {
      formattedData[index] = format[index];
    }
    int currentChar = 0;
    for (int index = 0; index < (keys.length * 2); index += 2) {
      formattedData[index + format.length] = keys[currentChar];
      formattedData[index + 1 + format.length] = unformattedData.get(keys[currentChar]);
      currentChar++;
    }
    return formattedData;
  }

  private static boolean isValidRecipeData(HashMap<Character, Object> unformatedData) {
    for (Character ch : unformatedData.keySet()) {
      if (unformatedData.get(ch) == null) {
        return false;
      }
    }
    return true;
  }

  private static Object convertToStack(String data) {
    return WurmTweaks2API.dataConverters.get("ItemStack").getData(data);
  }
}
