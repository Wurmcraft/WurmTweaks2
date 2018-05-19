package com.wurmcraft.script.support;

import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import com.wurmcraft.script.utils.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static betterwithmods.common.BWRegistry.*;
import static betterwithmods.common.registry.anvil.AnvilCraftingManager.ANVIL_CRAFTING;
import static betterwithmods.common.registry.heat.BWMHeatRegistry.addHeatSource;

public class BetterWithMods extends SupportBase {
 private List<Object[]> cauldron = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> crucible = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> sCauldron = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> sCrucible = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> mill = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> saw = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> anvil = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> heat = Collections.synchronizedList(new ArrayList<>());
 private List <HopperInteractions.HopperRecipe> hopper = Collections.synchronizedList (new ArrayList <> ());

 public BetterWithMods() {
  super("betterwithmods");
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : cauldron) CAULDRON.addUnstokedRecipe((List<Ingredient>) recipe[0], (List<ItemStack>) recipe[1]);
  for (Object[] recipe : crucible) CRUCIBLE.addUnstokedRecipe((List<Ingredient>) recipe[0], (List<ItemStack>) recipe[1]);
  for (Object[] recipe : sCauldron) CAULDRON.addStokedRecipe((List<Ingredient>) recipe[0], (List<ItemStack>) recipe[1]);
  for (Object[] recipe : sCrucible) CRUCIBLE.addStokedRecipe((List<Ingredient>) recipe[0], (List<ItemStack>) recipe[1]);
  for (Object[] recipe : mill) MILLSTONE.addMillRecipe((List<Ingredient>) recipe[0], (List<ItemStack>) recipe[1]);
  for (Object[] recipe : saw) WOOD_SAW.addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1]);
  for (Object[] recipe : anvil) ANVIL_CRAFTING.add((ShapedAnvilRecipe) recipe[0]);
  for (Object[] recipe : heat) addHeatSource((BlockIngredient) recipe[0], (int) recipe[1]);
  for (HopperInteractions.HopperRecipe recipe : hopper)
   HopperInteractions.addHopperRecipe (recipe);
 }

 @Override
 public void init() {
  cauldron.clear();
  crucible.clear();
  sCauldron.clear();
  sCrucible.clear();
  mill.clear();
  saw.clear();
  anvil.clear();
  heat.clear();
  hopper.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   //BWRegistry
   WOOD_SAW.getRecipes().clear();
   CAULDRON.getRecipes().clear();
   CRUCIBLE.getRecipes().clear();
   MILLSTONE.getRecipes().clear();
   HopperInteractions.RECIPES.clear();
   //AnvilCraftingManager
   ANVIL_CRAFTING.clear();
  }
 }

 @ScriptFunction
 public void addCauldron(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addCauldron('<output> <input>...')");
  isValid(helper, input[0]);
  List<Ingredient> inputStacks = new ArrayList<>();
  for (int index = 1; index < input.length; index++) {
   isValid(helper, input[index]);
   inputStacks.add(convertIngredient(helper, input[index]));
  }
  List<ItemStack> outputStacks = new ArrayList<>();
  outputStacks.add(convertStack(helper, (input[0])));
  cauldron.add(new Object[]{inputStacks, outputStacks});
 }

 @ScriptFunction
 public void addStokedCauldron(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addStokedCauldron('<output> <input>...')");
  isValid(helper, input[0]);
  List<Ingredient> inputStacks = new ArrayList<>();
  for (int index = 1; index < input.length; index++) {
   isValid(helper, input[index]);
   inputStacks.add(convertIngredient(helper, input[index]));
  }
  List<ItemStack> outputStacks = new ArrayList<>();
  outputStacks.add(convertStack(helper, (input[0])));
  sCauldron.add(new Object[]{inputStacks, outputStacks});
 }

 @ScriptFunction
 public void addCrucible(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addCrucible('<output> <input>...')");
  isValid(helper, input[0]);
  List<Ingredient> inputStacks = new ArrayList<>();
  for (int index = 1; index < input.length; index++) {
   isValid(helper, input[index]);
   inputStacks.add(convertIngredient(helper, input[index]));
  }
  List<ItemStack> outputStacks = new ArrayList<>();
  outputStacks.add(convertStack(helper, (input[0])));
  sCrucible.add(new Object[]{inputStacks, outputStacks});
 }

 @ScriptFunction
 public void addStokedCrucible(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addStokedCrucible('<output> <input>...')");
  isValid(helper, input[0]);
  List<Ingredient> inputStacks = new ArrayList<>();
  for (int index = 1; index < input.length; index++) {
   isValid(helper, input[index]);
   inputStacks.add(convertIngredient(helper, input[index]));
  }
  List<ItemStack> outputStacks = new ArrayList<>();
  outputStacks.add(convertStack(helper, (input[0])));
  sCrucible.add(new Object[]{inputStacks, outputStacks});
 }

 @ScriptFunction
 public void addMill(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addMill('<output> <input>...')");
  List<Ingredient> inputStacks = new ArrayList<>();
  for (int index = 1; index < input.length; index++) {
   isValid(helper, input[index]);
   inputStacks.add(convertIngredient(helper, input[index]));
  }
  List<ItemStack> outputStacks = new ArrayList<>();
  outputStacks.add(convertStack(helper, (input[0])));
  mill.add(new Object[]{inputStacks, outputStacks});
 }

 @ScriptFunction
 public void addSaw (StackHelper helper,String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addSaw('<output> <input>')");
  isValid(helper, input[0], input[1]);
  saw.add(new Object[]{convertStack(helper, (input[0])), convertStack(helper, (input[1]))});
 }

 @ScriptFunction
 public void addAnvil(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length > 4, "addAnvil(<output> <style> <format>')");
  isValid(helper, input[0]);
  ItemStack output = convertStack(helper, (input[0]));
  List<Object> recipe = RecipeUtils.getShapedRecipe(helper, input);
  Preconditions.checkNotNull(recipe);
  anvil.add(new Object[]{new ShapedAnvilRecipe(new ResourceLocation(Global.MODID, output.getUnlocalizedName().substring(5) + recipe.hashCode()), output, recipe.toArray(new Object[0]))});
 }

 @ScriptFunction
 public void addBlockHeat(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addBlockHeat(<block> <heat>')");
  isValid(helper, input[0]);
  isValid(EnumInputType.INTEGER, helper, input[1]);
  BlockIngredient heatBlock = new BlockIngredient(convertStack(helper, input[0]));
  heat.add(new Object[]{heatBlock, convertInteger(input[1])});
 }

 @ScriptFunction
 public void addFilteredHopper(StackHelper helper, String line) {
  String[] input = validateFormat (line,line.split (" ").length >= 3,"addFilteredHopper('<output> <input> <name> <secOutput>...')");
  isValid (helper,input[0],input[1]);
  isValid (EnumInputType.STRING,helper,input[1]);
  List <ItemStack> secOutput = new ArrayList <> ();
  for (int index = 3; index < input.length; index++) {
   isValid (helper,input[index]);
   secOutput.add (convertStack (helper,input[index]));
  }
  hopper.add (new HopperInteractions.HopperRecipe (input[2],convertIngredient (helper,input[1]),convertStack (helper,input[0]),secOutput.toArray (new ItemStack[0])));
 }
}
