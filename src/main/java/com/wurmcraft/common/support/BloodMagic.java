package com.wurmcraft.common.support;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.script.utils.IngredientWrapper;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "bloodmagic")
public class BloodMagic {

  private static NonBlockingHashSet<Object[]> altar;
  private static NonBlockingHashSet<Object[]> array;
  private static NonBlockingHashSet<Object[]> forge;
  private static NonBlockingHashSet<Object[]> table;

  @InitSupport(modid = "bloodmagic")
  public void init() {
    if (altar == null) {
      altar = new NonBlockingHashSet<>();
      array = new NonBlockingHashSet<>();
      forge = new NonBlockingHashSet<>();
      table = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      for (RecipeAlchemyArray array :
          BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArrayRecipes()) {
        for (ItemStack input : array.getInput().getMatchingStacks()) {
          for (ItemStack cat : array.getCatalyst().getMatchingStacks()) {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyArray(input, cat);
          }
        }
      }
      for (RecipeBloodAltar recipe :
          BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes()) {
        for (ItemStack input : recipe.getInput().getMatchingStacks()) {
          BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeBloodAltar(input);
        }
      }
      for (RecipeTartaricForge recipe :
          BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForgeRecipes()) {
        for (Ingredient input : recipe.getInput()) {
          for (ItemStack stack : input.getMatchingStacks()) {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeTartaricForge(stack);
          }
        }
      }
      for (RecipeAlchemyTable recipe :
          BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyRecipes()) {
        for (Ingredient input : recipe.getInput()) {
          for (ItemStack stack : input.getMatchingStacks()) {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyTable(stack);
          }
        }
      }
    } else if (ScriptExecutor.reload) {
      // TODO Remove Recipes on Reload
      altar.clear();
      array.clear();
      forge.clear();
      table.clear();
    }
  }

  @FinalizeSupport(modid = "bloodmagic")
  public void finishSupport() {
    for (Object[] recipe : altar) {
      BloodMagicAPI.INSTANCE
          .getRecipeRegistrar()
          .addBloodAltar(
              (Ingredient) recipe[0],
              (ItemStack) recipe[1],
              (int) recipe[2],
              (int) recipe[3],
              (int) recipe[4],
              (int) recipe[5]);
    }
    for (Object[] recipe : array) {
      BloodMagicAPI.INSTANCE
          .getRecipeRegistrar()
          .addAlchemyArray(
              (Ingredient) recipe[0], (Ingredient) recipe[1], (ItemStack) recipe[2], null);
    }
    for (Object[] recipe : forge) {
      BloodMagicAPI.INSTANCE
          .getRecipeRegistrar()
          .addTartaricForge(
              (ItemStack) recipe[0], (float) recipe[1], (float) recipe[2], (ItemStack[]) recipe[3]);
    }
    for (Object[] recipe : table) {
      BloodMagicAPI.INSTANCE
          .getRecipeRegistrar()
          .addAlchemyTable(
              (ItemStack) recipe[0],
              (int) recipe[1],
              (int) recipe[2],
              (int) recipe[3],
              (Ingredient[]) recipe[4]);
    }
  }

  @ScriptFunction(
    modid = "bloodmagic",
    inputFormat = "ItemStack ItemStack Integer Integer Integer Integer"
  )
  public void addAltar(Converter converter, String[] line) {
    altar.add(
        new Object[] {
          new IngredientWrapper((ItemStack) converter.convert(line[1], 1)),
          converter.convert(line[0]),
          Integer.parseInt(line[2]),
          Integer.parseInt(line[3]),
          Integer.parseInt(line[4]),
          Integer.parseInt(line[5])
        });
  }

  @ScriptFunction(modid = "bloodmagic", inputFormat = "ItemStack ItemStack ItemStack")
  public void addAlchemyArray(Converter converter, String[] line) {
    array.add(
        new Object[] {
          new IngredientWrapper((ItemStack) converter.convert(line[2], 1)),
          new IngredientWrapper((ItemStack) converter.convert(line[1], 1)),
          converter.convert(line[0]),
          null
        });
  }

  @ScriptFunction(
    modid = "bloodmagic",
    inputFormat = "ItemStack Float Float ItemStack/OreDictionary ..."
  )
  public void addSoulForge(Converter converter, String[] line) {
    forge.add(
        new Object[] {
          converter.convert(line[0]),
          Float.parseFloat(line[2]),
          Float.parseFloat(line[1]),
          converter.getBulkItemsAsList(Arrays.copyOfRange(line, 3, line.length))
        });
  }

  @ScriptFunction(
    modid = "bloodmagic",
    inputFormat = "ItemStack Integer Integer Integer ItemStack/OreDictionary ..."
  )
  public void addTable(Converter converter, String[] line) {
    table.add(
        new Object[] {
          converter.convert(line[0], 1),
          Integer.parseInt(line[1]),
          Integer.parseInt(line[2]),
          Integer.parseInt(line[3]),
          RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 4, line.length))
              .toArray(new Ingredient[0])
        });
  }
}
