package com.wurmcraft.common.support.utils;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.common.script.FunctionBuilder;
import com.wurmcraft.common.script.utils.DynamicShapedOreRecipe;
import com.wurmcraft.common.script.utils.DynamicShapelessOreRecipe;
import com.wurmcraft.common.script.utils.IngredientWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class RecipeUtils {

  public static final ResourceLocation RECIPE_GROUP = new ResourceLocation(Global.MODID, "Recipes");

  public static Object[] getShapelessItems(String[] list, Converter converter) {
    List<Object> items = new ArrayList<>();
    for (String line : list) {
      Object convert = converter.convert(line);
      if (convert != null) {
        if (convert instanceof ItemStack) {
          items.add(new IngredientWrapper((ItemStack) convert));
        } else {
          items.add(convert);
        }
      } else {
        invalidItemStack(line);
        return new Object[0];
      }
    }
    return items.toArray();
  }

  private static void invalidItemStack(String line) {
    WurmTweaks.logger.error(
        "Unable to convert '" + line + "' into an ItemStack or OreDictionary Entry!");
  }

  public static IRecipe createShapelessRecipe(ItemStack output, Object[] input) {
    if (!verifyInput(input) && ConfigHandler.recipeErrorDebug) {
      FunctionBuilder.print.println(
          Thread.currentThread().getName()
              + " "
              + output.toString()
              + " Invalid Input: "
              + Arrays.toString(input));
    } else {
      DynamicShapelessOreRecipe recipe = new DynamicShapelessOreRecipe(RECIPE_GROUP, output, input);
      recipe.setRegistryName(generateRecipeName(output, Arrays.hashCode(input)));
      return recipe;
    }
    return null;
  }

  public static IRecipe createShapedRecipe(ItemStack output, Object[] input) {
    if (!verifyInput(input) && ConfigHandler.recipeErrorDebug) {
      FunctionBuilder.print.println(
          Thread.currentThread().getName()
              + " "
              + output.toString()
              + " Invalid Input: "
              + Arrays.toString(input));
    } else {
      DynamicShapedOreRecipe recipe = new DynamicShapedOreRecipe(RECIPE_GROUP, output, input);
      recipe.setRegistryName(generateRecipeName(output, Arrays.hashCode(input)));
      return recipe;
    }
    return null;
  }

  private static boolean validIngredient(Ingredient ingredient) {
    for (ItemStack stack : ingredient.getMatchingStacks()) {
      if (stack == ItemStack.EMPTY || stack == null) {
        ingredient = new IngredientWrapper(new ItemStack(Blocks.BEDROCK));
        return false;
      }
    }
    return true;
  }

  public static Object[] getShapelessRecipeInput(String[] input) {
    List<Object> recipeInput = new ArrayList<>();
    for (String in : input) {
      recipeInput.add(getIngredient(in));
    }
    return recipeInput.toArray(new Object[0]);
  }

  public static NonNullList<Ingredient> getShapelessIngredient(String[] input) {
    NonNullList<Ingredient> recipeInput = NonNullList.create();
    for (String in : input) {
      recipeInput.add(getIngredient(in));
    }
    return recipeInput;
  }

  public static ResourceLocation generateRecipeName(ItemStack output, int inputHash) {
    return new ResourceLocation(
        Global.MODID, output.toString() + inputHash + "_" + output.getTagCompound());
  }

  private static int findLargest(int[] num) {
    int highest = 0;
    for (int i : num) {
      if (i > highest) {
        highest = i;
      }
    }
    return highest;
  }

  private static String replaceLastTillDiff(String line, char ch) {
    StringBuilder build = new StringBuilder(line);
    for (int index = line.length() - 1; index == 0; index--) {
      if (line.charAt(index) == ch) {
        build.deleteCharAt(index);
      } else {
        break;
      }
    }
    return build.toString();
  }

  private static int[] getRecipeSize(String[] possibleStyle) {
    int[] temp = new int[possibleStyle.length];
    for (int index = 0; index < possibleStyle.length; index++) {
      if (possibleStyle[index] != null && possibleStyle[index].length() > 0) {
        temp[index] = replaceLastTillDiff(possibleStyle[index], '_').length();
      }
    }
    int[] size = new int[2];
    size[0] = findLargest(temp);
    int height = 0;
    for (int t : temp) {
      if (t > 0) {
        height++;
      }
    }
    size[1] = height;
    return size;
  }

  public static List<Object> getShapedRecipe(String[] input) {
    int indexFirstVar = 1;
    for (; indexFirstVar < input.length; indexFirstVar++) {
      if (input[indexFirstVar - 1].length() == 1 && input[indexFirstVar].contains("<")) {
        indexFirstVar -= 1;
        break;
      }
    }
    int[] recipeSize = RecipeUtils.getRecipeSize(Arrays.copyOfRange(input, 1, indexFirstVar));
    String[] recipeStyle = new String[recipeSize[1]];
    for (int index = 1; index < (recipeSize[1] + 1); index++) {
      StringBuilder temp = new StringBuilder(RecipeUtils.replaceLastTillDiff(input[index], '_'));
      if (temp.length() < recipeSize[0]) {
        while (temp.length() < recipeSize[0]) {
          temp.append(" ");
        }
      }
      recipeStyle[index - 1] = temp.toString().replaceAll("_", " ");
    }
    HashMap<Character, Ingredient> recipeFormat = new HashMap<>();
    HashMap<Character, String> invalidFormat = new HashMap<>();
    for (int index = (recipeSize[1] + 1); index < input.length; index++) {
      if (!input[index].startsWith("<") && input[index].length() == 1) {
        if ((index + 1) < input.length) {
          Ingredient stack = getIngredient(input[index + 1]);
          recipeFormat.put(input[index].charAt(0), stack);
          if (stack == Ingredient.EMPTY) {
            invalidFormat.put(input[index].charAt(0), input[index + 1]);
          }
          index++;
        } else {
          recipeFormat.put(input[index].charAt(0), Ingredient.EMPTY);
        }
      }
    }
    boolean valid = true;
    for (Character ch : recipeFormat.keySet()) {
      if (recipeFormat.get(ch) == Ingredient.EMPTY) {
        WurmTweaks.logger.warn("Invalid Stack '" + ch + "' " + invalidFormat.getOrDefault(ch, ""));
        valid = false;
      }
    }
    if (valid) {
      List<Object> temp = new ArrayList<>();
      for (Character ch : recipeFormat.keySet()) {
        temp.add(ch);
        temp.add(recipeFormat.get(ch));
      }
      List<Object> finalRecipe = new ArrayList<>();
      finalRecipe.addAll(Arrays.asList(recipeStyle));
      finalRecipe.addAll(temp);
      return finalRecipe;
    }
    return null;
  }

  public static Ingredient getIngredient(String in) {
    if (Converter.getFromName("ItemStack").isValid(in)) {
      return new IngredientWrapper((ItemStack) Converter.getFromName("ItemStack").getData(in));
    } else if (Converter.getFromName("Oredictionary").isValid(in)) {
      return (Ingredient) Converter.getFromName("Oredictionary").getData(in);
    }
    return Ingredient.EMPTY;
  }

  public static boolean verifyInput(Object[] input) {
    for (Object obj : input) {
      if (obj instanceof ItemStack) {
        if (((ItemStack) obj).isEmpty()) {
          return false;
        }
      } else if (obj instanceof String) {
        if (((String) obj).isEmpty()) {
          return false;
        }
      } else if (obj instanceof Ingredient) {
        if (((Ingredient) obj).getMatchingStacks().length == 0) {
          return false;
        }
      }
    }
    return true;
  }
}
