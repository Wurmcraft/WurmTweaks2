package com.wurmcraft.script.support;


import blusunrize.immersiveengineering.api.crafting.*;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImmersiveEngineering extends SupportHelper {

 private List<Object[]> alloy = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> arc = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> blast = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> fuel = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> coke = Collections.synchronizedList(new ArrayList<>());
 private List<CrusherRecipe> crusher = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> fermenter = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> press = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> refinery = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> squeezer = Collections.synchronizedList(new ArrayList<>());

 public ImmersiveEngineering() {
  super("immersiveengineering");
 }

 @Override
 public void init() {
  alloy.clear();
  arc.clear();
  blast.clear();
  fuel.clear();
  coke.clear();
  crusher.clear();
  fermenter.clear();
  press.clear();
  refinery.clear();
  squeezer.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   AlloyRecipe.recipeList.clear();
   ArcFurnaceRecipe.recipeList.clear();
   BlastFurnaceRecipe.recipeList.clear();
   CokeOvenRecipe.recipeList.clear();
   CrusherRecipe.recipeList.clear();
   FermenterRecipe.recipeList.clear();
   MetalPressRecipe.recipeList.clear();
   RefineryRecipe.recipeList.clear();
   SqueezerRecipe.recipeList.clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : alloy)
   AlloyRecipe.addRecipe((ItemStack)recipe[0], (ItemStack)recipe[1], (ItemStack)recipe[2], (int)recipe[3]);
  for (Object[] recipe : arc)
   if (recipe.length == 6)
    ArcFurnaceRecipe.addRecipe((ItemStack)recipe[0], (ItemStack)recipe[1], (ItemStack)recipe[2], (int)recipe[3], (int)recipe[4], (List<ItemStack>)recipe[5]);
   else
    ArcFurnaceRecipe.addRecipe((ItemStack)recipe[0], (ItemStack)recipe[1], (ItemStack)recipe[2], (int)recipe[3], (int)recipe[4]);
  for (Object[] recipe : blast)
   BlastFurnaceRecipe.addRecipe((ItemStack)recipe[0], (ItemStack)recipe[1], (int)recipe[2], (ItemStack)recipe[3]);
  for (Object[] recipe : fuel)
   BlastFurnaceRecipe.addBlastFuel(recipe[0], (int)recipe[1]);
  for (Object[] recipe : coke)
   CokeOvenRecipe.addRecipe((ItemStack)recipe[0], (ItemStack)recipe[1], (int)recipe[2], (int)recipe[3]);
  CrusherRecipe.recipeList.addAll(crusher);
  for (Object[] recipe : fermenter)
   FermenterRecipe.addRecipe((FluidStack)recipe[0], (ItemStack)recipe[1], (ItemStack)recipe[2], (int)recipe[3]);
  for (Object[] recipe : press)
   MetalPressRecipe.addRecipe((ItemStack)recipe[0], (ItemStack)recipe[1], (ItemStack)recipe[2], (int)recipe[3]);
  for (Object[] recipe : refinery)
   RefineryRecipe.addRecipe((FluidStack)recipe[0], (FluidStack)recipe[1], (FluidStack)recipe[2], (int)recipe[3]);
  for (Object[] recipe : squeezer)
   SqueezerRecipe.addRecipe((FluidStack)recipe[0], (ItemStack)recipe[1], recipe[2], (int)recipe[3]);
 }

 @ScriptFunction
 public void addAlloyRecipe(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addAlloyRecipe('<output> <input> <input2> <time>");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3]);
  alloy.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertInteger(input[3])});
 }

 @ScriptFunction
 public void addArcFurnace(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 4, "addArcFurnace('<output> <slag> <input> <time> <energy> | <additives'");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3], input[4]);
  List<ItemStack> additives = new ArrayList<>();
  if (input.length > 5) {
   for (int index = 5; index < input.length; index++) {
    isValid(helper, input[index]);
    additives.add(convertStack(helper, input[index]));
   }
   arc.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[2]), convertStack(helper, input[1]), convertInteger(input[3]), convertInteger(input[4]), additives});
  } else
   arc.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[2]), convertStack(helper, input[1]), convertInteger(input[3]), convertInteger(input[4])});
 }

 @ScriptFunction
 public void addBlastFurnace(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addBlastFurnace(<output> <slag> <input> <time>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3]);
  blast.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[2]), convertInteger(input[3]), convertStack(helper, input[1])});
 }

 @ScriptFunction
 public void addBlastFuel(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addBlastFuel('<stack> <time>')");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1]);
  fuel.add(new Object[]{convertStack(helper, input[0]), convertInteger(input[1])});
 }

 @ScriptFunction
 public void addCokeOven(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addCokeOven('<output> <input> <creosote> <time>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2], input[3]);
  coke.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[3]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addCrusher(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3 || line.split(" ").length == 5, "addCrusher('<output> <input> <energy> | <secOutput> <secOutput%>)");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2]);
  if (line.split(" ").length == 3)
   CrusherRecipe.addRecipe(convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[2]));
  else if (line.split(" ").length == 5) {
   isValid(helper, input[3]);
   isValid(Types.FLOAT, helper, input[4]);
   CrusherRecipe recipe = new CrusherRecipe(convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[2]));
   recipe.addToSecondaryOutput(convertStack(helper, input[3]), convertFloat(input[4]));
   crusher.add(recipe);
  }
 }

 @ScriptFunction
 public void addFermenter(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addFermenter('<output> <*output> <input> <energy')");
  isValid(helper, input[0], input[2]);
  isValid(Types.FLUIDSTACK, helper, input[1]);
  isValid(Types.INTEGER, helper, input[3]);
  fermenter.add(new Object[]{convertFluidStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, input[2]), convertInteger(input[3])});
 }

 @ScriptFunction
 public void addMetalPress(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addMetalPress(<output> <input> <mold> <time>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3]);
//  press.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[2]), convertStack(helper, input[3]), convertInteger(input[1])});
  press.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[2]), convertStack(helper, input[1]), convertInteger(input[3])});
 }

 @ScriptFunction
 public void addRefinery(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addRefinery('<*output> <*input> <*input> <energy>')");
  isValid(Types.FLUIDSTACK, helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3]);
  refinery.add(new Object[]{convertFluidStack(helper, input[0]), convertFluidStack(helper, input[1]), convertFluidStack(helper, input[2]), convertInteger(input[3])});
 }

 @ScriptFunction
 public void addSqueezer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addSqueezer('<output> <*output> <input> <energy>')");
  isValid(helper, input[0], input[2]);
  isValid(Types.FLUIDSTACK, helper, input[1]);
  isValid(Types.INTEGER, helper, input[2]);
  squeezer.add(new Object[]{convertFluidStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, input[2]), convertInteger(input[2])});
 }
}
