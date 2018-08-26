package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "galacticraftcore")
public class Galacticraft {

  private NonBlockingHashSet<Object[]> shapedCompressor;
  private NonBlockingHashSet<Object[]> shapelessCompressor;
  private NonBlockingHashSet<Object[]> circuitFab;
  private NonBlockingHashSet<Object[]> station;

  @InitSupport
  public void init() {
    if (shapelessCompressor == null) {
      shapedCompressor = new NonBlockingHashSet<>();
      shapelessCompressor = new NonBlockingHashSet<>();
      circuitFab = new NonBlockingHashSet<>();
      station = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      CompressorRecipes.getRecipeList().clear();
      CircuitFabricatorRecipes.getRecipes().clear();
    } else if (ScriptExecutor.reload) {
      shapedCompressor.stream().map(recipe -> (ItemStack) recipe[0])
          .forEach(CompressorRecipes::removeRecipe);
      shapedCompressor.clear();
      shapelessCompressor.stream().map(recipe -> (ItemStack) recipe[0])
          .forEach(CompressorRecipes::removeRecipe);
      shapelessCompressor.clear();
      circuitFab.stream().map(fab -> (ItemStack) fab[0])
          .forEach(CircuitFabricatorRecipes::removeRecipe);
      circuitFab.clear();
      station.clear();
    }
  }

  @FinalizeSupport
  public void finishSupport() {
    shapedCompressor.forEach(
        recipe -> CompressorRecipes.addRecipe((ItemStack) recipe[0], (Object[]) recipe[1]));
    shapelessCompressor.forEach(
        recipe -> CompressorRecipes.addRecipe((ItemStack) recipe[0], (Object[]) recipe[1]));
    circuitFab.forEach(recipe -> CircuitFabricatorRecipes
        .addRecipe((ItemStack) recipe[0], (List<Object>) recipe[1]));
    station.forEach(recipe -> GalacticraftRegistry
        .replaceSpaceStationRecipe((int) recipe[0], (HashMap<Object, Integer>) recipe[1]));
  }

  @ScriptFunction(modid = "galacticraftcore")
  public void addShapedCompressor(Converter converter, String[] line) {
    shapedCompressor.add(new Object[]{converter.convert(line[0]),
        RecipeUtils.getShapedRecipe(line).toArray(new Object[0])});
  }

  @ScriptFunction(modid = "galacticraftcore", inputFormat = "ItemStack ItemStack ...")
  public void addShapelessCompressor(Converter converter, String[] line) {
    shapelessCompressor
        .add(new Object[]{converter.convert(line[0], 1),
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)).toArray(
                new Ingredient[0])});
  }

  @ScriptFunction(modid = "galacticraftcore", inputFormat = "ItemStack ItemStack ...")
  public void addCircuitFabricator(Converter converter, String[] line) {
    circuitFab.add(new Object[]{converter.convert(line[0], 1),
        Arrays.asList(converter.getBulkItems(Arrays.copyOfRange(line, 1, line.length)))});
  }

  @ScriptFunction(modid = "galacticraftcore", inputFormat = "Integer ItemStack/OreDictionary")
  public void addSpaceStationRecipe(Converter converter, String[] line) {
    HashMap<Object, Integer> stationRecipe = new HashMap<>();
    for (Object item : RecipeUtils
        .getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length))) {
      if (item instanceof ItemStack) {
        stationRecipe.put(item, ((ItemStack) item).getCount());
      } else {
        stationRecipe.put(item, 16);
      }
    }
    station.add(new Object[]{Integer.parseInt(line[0]), stationRecipe});
  }
}
