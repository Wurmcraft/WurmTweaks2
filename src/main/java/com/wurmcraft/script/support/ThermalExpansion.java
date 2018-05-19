package com.wurmcraft.script.support;

import cofh.thermalexpansion.util.managers.machine.*;
import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThermalExpansion extends SupportBase {

 private List<Object[]>
  furnace = Collections.synchronizedList(new ArrayList<>()),
  pulverizer = Collections.synchronizedList(new ArrayList<>()),
  sawmill = Collections.synchronizedList(new ArrayList<>()),
  smelter = Collections.synchronizedList(new ArrayList<>()),
  compactor = Collections.synchronizedList(new ArrayList<>()),
  crucible = Collections.synchronizedList(new ArrayList<>()),
  centerfuge = Collections.synchronizedList(new ArrayList<>());
 private List<TransposerManager.TransposerRecipe>
  extractTransposer = Collections.synchronizedList(new ArrayList<>()),
  fillTransposer = Collections.synchronizedList(new ArrayList<>());


 public ThermalExpansion() {
  super("thermalexpansion");
 }

 @Override
 public void init() {
  furnace.clear();
  pulverizer.clear();
  sawmill.clear();
  smelter.clear();
  compactor.clear();
  crucible.clear();
  centerfuge.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   for (FurnaceManager.FurnaceRecipe recipe : FurnaceManager.getRecipeList(false))
    FurnaceManager.removeRecipe(recipe.getInput());
   for (PulverizerManager.PulverizerRecipe recipe : PulverizerManager.getRecipeList())
    PulverizerManager.removeRecipe(recipe.getInput());
   for (SmelterManager.SmelterRecipe recipe : SmelterManager.getRecipeList())
    SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
   for (CompactorManager.Mode mode : CompactorManager.Mode.values())
    for (CompactorManager.CompactorRecipe recipe : CompactorManager.getRecipeList(mode))
     CompactorManager.removeRecipe(recipe.getInput(), mode);
   for (CentrifugeManager.CentrifugeRecipe recipe : CentrifugeManager.getRecipeList())
    CentrifugeManager.removeRecipe(recipe.getInput());
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : furnace)
   FurnaceManager.addRecipe((int)recipe[0], (ItemStack)recipe[1], (ItemStack)recipe[2]);
  for (Object[] r : pulverizer)
   if (r.length == 5) {
    PulverizerManager.addRecipe((int)r[0], (ItemStack)r[1], (ItemStack)r[2], (ItemStack)r[3], (int)r[4]);
   } else {
    PulverizerManager.addRecipe((int)r[0], (ItemStack)r[1], (ItemStack)r[2]);
   }
  for (Object[] r : sawmill) {
   if (r.length == 2) {
    SmelterManager.addRecipe((int)r[0], (ItemStack)r[1], (ItemStack)r[2], (ItemStack)r[3]);
   } else {
    SawmillManager.addRecipe((int)r[0], (ItemStack)r[1], (ItemStack)r[2]);
   }
  }
//  for (Object[] r : sawmill)
  for (Object[] r : compactor)
   CompactorManager.addRecipe((int)r[0], (ItemStack)r[1], (ItemStack)r[2], (CompactorManager.Mode)r[3]);
  for (Object[] r : crucible)
   CrucibleManager.addRecipe((int)r[0], (ItemStack)r[1], (FluidStack)r[2]);
  for (Object[] r : centerfuge)
   CentrifugeManager.addRecipe((int)r[0], (ItemStack)r[1], (List<ItemStack>)r[2], (FluidStack)r[3]);
  for (TransposerManager.TransposerRecipe r : fillTransposer)
   TransposerManager.addFillRecipe(r.getEnergy(), r.getInput(), r.getOutput(), r.getFluid(), true);
  for (TransposerManager.TransposerRecipe r : extractTransposer)
   TransposerManager.addExtractRecipe(r.getEnergy(), r.getInput(), r.getOutput(), r.getFluid(), r.getChance(), true);
 }

 @ScriptFunction
 public void addRedstoneFurnace(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addRedstoneFurnace('<output> <input> <energy>')");
  isValid(helper, input[0], input[1]);
  if (line.split(" ").length == 3) {
   isValid(EnumInputType.INTEGER, helper, input[2]);
   furnace.add(new Object[]{convertInteger(input[2]), convertStack(helper, input[1]), convertStack(helper, input[0])});
  } else
   furnace.add(new Object[]{8000, convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addPulverizer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 3, "addPulverizer('<output> <input> <energy> | <secOutput> <secOutput%>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  if (input.length == 3)
   pulverizer.add(new Object[]{convertInteger(input[2]), convertStack(helper, input[1]), convertStack(helper, input[0])});
  else {
   isValid(helper, input[3]);
   isValid(EnumInputType.INTEGER, helper, input[4]);
   pulverizer.add(new Object[]{convertInteger(input[2]), convertStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, input[3]), convertInteger(input[4])});
  }
 }

 @ScriptFunction
 public void addTESawmill(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addTESawmill('<output> <input> <energy>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  sawmill.add(new Object[]{convertInteger(input[2]), convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addSmelter(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addSmelter('<output> <inputA> <inputB> <energy')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(EnumInputType.INTEGER, helper, input[3]);
  smelter.add(new Object[]{convertInteger(input[3]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addCompactor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addCompactor('<output> <input> <energy> <mode>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  CompactorManager.Mode mode = getMode(input[3]);
  Preconditions.checkArgument(mode != null, "Invalid Mode %s", input[3]);
  compactor.add(new Object[]{convertInteger(input[2]), convertStack(helper, input[1]), convertStack(helper, input[0]), mode});
 }

 @ScriptFunction
 public void addMagmaCrucible(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addMagmaCrucible('<*output> <input> <energy>')");
  isValid(EnumInputType.FLUIDSTACK, helper, input[0]);
  isValid(helper, input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  crucible.add(new Object[]{convertInteger(input[2]), convertStack(helper, input[1]), convertFluidStack(helper, input[0])});
 }

 @ScriptFunction
 public void addTECenterfuge(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addCenterfuge('<output> <output2> <output3> <output4> <input> <energy> <*output5>')");
  isValid(helper, input[0], input[1], input[2], input[3], input[4]);
  isValid(EnumInputType.INTEGER, helper, input[5]);
  centerfuge.add(new Object[]{convertInteger(input[5]), convertStack(helper, input[4]), Arrays.asList(convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[3])), convertFluidStack(helper, input[6])});
 }

 @ScriptFunction
 public void addFluidTransposer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 5, "addFluidTransposer('<output> <input> <fluid> <energy> " + "<mode> | <chance>");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.FLUIDSTACK, helper, input[2]);
  if (line.split(" ").length == 6)
   isValid(EnumInputType.INTEGER, helper, input[5]);
  TransposerManager.TransposerRecipe recipe = new TransposerManager.TransposerRecipe(convertStack(helper, input[1]),
   convertStack(helper, input[0]), convertFluidStack(helper, input[2]), convertInteger(input[3]),
   line.split(" ").length == 6 ? convertInteger(input[5]) : 0);
  if (input[4].matches("[eE]xtract"))
   extractTransposer.add(recipe);
  else if (input[4].matches("[fF]ill"))
   fillTransposer.add(recipe);
 }

 private CompactorManager.Mode getMode(String mode) {
  if (mode.matches("[gG]ear"))
   return CompactorManager.Mode.GEAR;
  else if (mode.matches("[aA]ll"))
   return CompactorManager.Mode.ALL;
  else if (mode.matches("[cC]oin"))
   return CompactorManager.Mode.COIN;
  else if (mode.matches("[pP]late"))
   return CompactorManager.Mode.PLATE;
  return null;
 }
}
