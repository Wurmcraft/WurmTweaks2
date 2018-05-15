package com.wurmcraft.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.exception.InvalidStackException;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import com.wurmcraft.script.utils.recipe.RecipeUtils;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.*;

public class GalacticCraft extends SupportHelper {

 private List<Object[]> shapedCompressor = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> shapelessCompressor = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> circuitFab = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> station = Collections.synchronizedList(new ArrayList<>());

 public GalacticCraft() {
  super("galacticraftcore");
 }

 @Override
 public void init() {
  shapedCompressor.clear();
  shapelessCompressor.clear();
  circuitFab.clear();
  station.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   CompressorRecipes.getRecipeList().clear();
   CircuitFabricatorRecipes.getRecipes().clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : shapedCompressor)
   CompressorRecipes.addRecipe((ItemStack) recipe[0], (Object[]) recipe[1]);
  for (Object[] recipe : shapelessCompressor)
   CompressorRecipes.addRecipe((ItemStack) recipe[0], (Object[]) recipe[1]);
  for (Object[] recipe : circuitFab)
   CircuitFabricatorRecipes.addRecipe((ItemStack) recipe[0], (List<Object>) recipe[1]);
  for (Object[] recipe : station)
   GalacticraftRegistry.replaceSpaceStationRecipe((int) recipe[0], (HashMap<Object, Integer>) recipe[1]);
 }

 @ScriptFunction
 public void addShapedCompressor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length > 4, "addShapedCompressor(<output> <style> <format>')");
  isValid(helper, input[0]);
  List<Object> recipe = RecipeUtils.getShapedRecipe(helper, input);
  Preconditions.checkNotNull(recipe);
  shapedCompressor.add(new Object[]{convertStack(helper, input[0]), recipe.toArray(new Object[0])});
 }

 @ScriptFunction
 public void addShapelessCompressor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2 && line.split(" ").length <= 10, "addShapelessCompressor('<output> <input>...')");
  isValid(helper, input[0]);
  List<Ingredient> validItems = new ArrayList<>();
  boolean validRecipe = true;
  for (String item : Arrays.copyOfRange(input, 1, input.length))
   if (helper.convert(item) != null)
    validItems.add(convertIngredient(helper, item));
   else {
    validRecipe = false;
    throw new InvalidStackException("Invalid Item '" + item + "'");
   }
  if (validRecipe)
   shapelessCompressor.add(new Object[]{convertStack(helper, input[0]), validItems.toArray(new Ingredient[0])});
 }

 @ScriptFunction
 public void addCircuitFabricator(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.length() == 5, "addCircuitFabricator('<output> <input> <input2> <input3> <input4>')");
  isValid(helper, input[0], input[1], input[2], input[3], input[4]);
  List<Object> validItems = new ArrayList<>();
  boolean validRecipe = true;
  for (String item : Arrays.copyOfRange(input, 1, input.length))
   if (helper.convert(item) != null)
    validItems.add(convertStack(helper, item));
   else {
    validRecipe = false;
    throw new InvalidStackException("Invalid Item '" + item + "'");
   }
  if (validRecipe) {
   Preconditions.checkArgument(!validItems.isEmpty(), "Invalid Inputs!");
   circuitFab.add(new Object[]{convertStack(helper, input[0]), validItems});
  }
 }

 @ScriptFunction
 public void addSpaceStationRecipe(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.length() >= 2, "addSpaceStationRecipe('<stationID> <items>...')");
  isValid(Types.INTEGER, helper, input[0]);
  List<ItemStack> validItems = new ArrayList<>();
  boolean validRecipe = true;
  for (String item : Arrays.copyOfRange(input, 1, input.length))
   if (helper.convert(item) != null)
    validItems.add(convertStack(helper, item));
   else {
    validRecipe = false;
    throw new InvalidStackException("Invalid Item '" + item + "'");
   }
  if (validRecipe) {
   HashMap<Object, Integer> stationRecipe = new HashMap<>();
   for (Object item : validItems)
    if (item instanceof ItemStack)
     stationRecipe.put(item, ((ItemStack) item).getCount());
    else
     stationRecipe.put(item, 16);
   station.add(new Object[]{convertInteger(input[0]), stationRecipe});
  }
 }
}
