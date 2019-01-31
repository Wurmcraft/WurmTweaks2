package com.wurmcraft.common.support;

import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

// https://github.com/BlakeBr0/ExtendedCrafting
@Support(modid = "extendedcrafting")
public class ExtendedCrafting {

  private static NonBlockingHashSet<Object[]> scriptTableRecipes;
  private static NonBlockingHashSet<Object[]> scriptEnderShaped;
  private static NonBlockingHashSet<Object[]> scriptEnderShapeless;
  private static NonBlockingHashSet<CompressorRecipe> scriptCompression;

  @InitSupport(modid = "extendedcrafting")
  public void init() {
    if (scriptTableRecipes == null
        || scriptEnderShaped == null
        || scriptCompression == null
        || scriptEnderShapeless == null) {
      scriptTableRecipes = new NonBlockingHashSet<>();
      scriptEnderShaped = new NonBlockingHashSet<>();
      scriptCompression = new NonBlockingHashSet<>();
      scriptEnderShapeless = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllRecipes) {
      scriptTableRecipes.forEach(
          output -> TableRecipeManager.getInstance().removeRecipes((ItemStack) output[0]));
      scriptEnderShaped.forEach(
          output -> EnderCrafterRecipeManager.getInstance().removeRecipes((ItemStack) output[0]));
      scriptEnderShapeless.forEach(
          output -> EnderCrafterRecipeManager.getInstance().removeRecipes((ItemStack) output[0]));
      scriptCompression.forEach(
          recipe -> CompressorRecipeManager.getInstance().removeRecipes(recipe.getOutput()));
    }
  }

  @FinalizeSupport(modid = "extendedcrafting")
  public void finalize() {
    scriptTableRecipes.forEach(
        recipe ->
            TableRecipeManager.getInstance()
                .addShaped(
                    (int) recipe[1],
                    (ItemStack) recipe[0],
                    Arrays.copyOfRange(recipe, 1, recipe.length)));
    scriptEnderShaped.forEach(
        recipe ->
            EnderCrafterRecipeManager.getInstance().addShaped((ItemStack) recipe[0], recipe[1]));
    scriptEnderShapeless.forEach(
        recipe ->
            EnderCrafterRecipeManager.getInstance().addShapeless((ItemStack) recipe[0], recipe[1]));
    for (CompressorRecipe recipe : scriptCompression) {
      CompressorRecipeManager.getInstance()
          .addRecipe(
              recipe.getOutput(),
              recipe.getInput(),
              recipe.getInputCount(),
              recipe.getCatalyst(),
              recipe.consumeCatalyst(),
              recipe.getPowerCost(),
              recipe.getPowerRate());
    }
  }

  @ScriptFunction(modid = "extendedcrafting", inputFormat = "ItemStack Integer Object ...")
  public void addShapedTable(Converter converter, String[] line) {
    scriptTableRecipes.add(
        new Object[] {
          converter.convert(line[0]),
          Integer.parseInt(line[1]),
          RecipeUtils.getShapedRecipe(Arrays.copyOfRange(line, 2, line.length))
        });
  }

  @ScriptFunction(modid = "extendedcrafting", inputFormat = "ItemStack Object ...")
  public void addShapelessTable(Converter converter, String[] line) {
    scriptTableRecipes.add(
        new Object[] {
          converter.convert(line[0]),
          RecipeUtils.getShapelessItems(Arrays.copyOfRange(line, 1, line.length), converter)
        });
  }
}
