package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "avaritia")
public class Avaritia {

  private static NonBlockingHashSet<ExtremeShapedRecipe> shaped;
  private static NonBlockingHashSet<ExtremeShapelessRecipe> shapeless;
  private static NonBlockingHashSet<ICompressorRecipe> compressor;

  @InitSupport
  public void init() {
    shaped = new NonBlockingHashSet<>();
    shapeless = new NonBlockingHashSet<>();
    compressor = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      AvaritiaRecipeManager.EXTREME_RECIPES.clear();
      AvaritiaRecipeManager.COMPRESSOR_RECIPES.clear();
    } else if (ScriptExecutor.reload) {
      shaped
          .stream()
          .map(Impl::getRegistryName)
          .forEach(AvaritiaRecipeManager.EXTREME_RECIPES::remove);
      shaped.clear();
      shapeless
          .stream()
          .map(Impl::getRegistryName)
          .forEach(AvaritiaRecipeManager.EXTREME_RECIPES::remove);
      shapeless.clear();
      compressor.forEach(AvaritiaRecipeManager.COMPRESSOR_RECIPES::remove);
      compressor.clear();
    }
  }

  @ScriptFunction(modid = "avaritia")
  public void addShapedExtreme(Converter converter, String[] line) {
    shaped.add(
        new ExtremeShapedRecipe(
            (ItemStack) converter.convert(line[0]),
            CraftingHelper.parseShaped(RecipeUtils.getShapedRecipe(line).toArray(new Object[0]))));
  }

  @ScriptFunction(modid = "avaritia", inputFormat = "ItemStack ItemStack/OreDictionary ...")
  public void addShapelessExtreme(Converter converter, String[] line) {
    shapeless.add(
        new ExtremeShapelessRecipe(
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)),
            (ItemStack) converter.convert(line[0])));
  }

  @ScriptFunction(modid = "avaritia")
  public void addCompression(Converter converter, String[] line) {
    compressor.add(
        new CompressorRecipe(
            ((ItemStack) converter.convert(line[0])),
            ((ItemStack) converter.convert(line[1])).getCount(),
            false,
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, 1))));
  }

  @FinalizeSupport
  public void finishSupport() {
    shaped.forEach(
        recipe ->
            AvaritiaRecipeManager.EXTREME_RECIPES.put(
                new ResourceLocation(
                    Global.MODID,
                    recipe.getRecipeOutput().toString()
                        + (recipe.getRecipeOutput().hasTagCompound()
                        ? recipe.getRecipeOutput().getTagCompound()
                        : "")),
                recipe));
    shapeless.forEach(
        recipe ->
            AvaritiaRecipeManager.EXTREME_RECIPES.put(
                new ResourceLocation(
                    Global.MODID,
                    recipe.getRecipeOutput().toString()
                        + (recipe.getRecipeOutput().hasTagCompound()
                        ? recipe.getRecipeOutput().getTagCompound()
                        : "")),
                recipe));
    compressor.forEach(
        recipe ->
            AvaritiaRecipeManager.COMPRESSOR_RECIPES.put(
                new ResourceLocation(
                    Global.MODID,
                    recipe.getResult().toString()
                        + (recipe.getResult().hasTagCompound()
                        ? recipe.getResult().getTagCompound()
                        : "")),
                recipe));
  }
}
