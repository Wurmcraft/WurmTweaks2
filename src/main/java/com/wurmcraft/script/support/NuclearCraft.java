package com.wurmcraft.script.support;


import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import nc.recipe.NCRecipes;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NuclearCraft extends SupportBase {

 private List<Object[]> manufactory = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> separator = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> pressurizer = Collections.synchronizedList(new ArrayList<>());


 public NuclearCraft() {
  super("nuclearcraft");
 }

 @Override
 public void init() {
  manufactory.clear();
  separator.clear();
  pressurizer.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   NCRecipes.Type.PRESSURIZER.getRecipeHandler().recipes.clear();
   NCRecipes.Type.ISOTOPE_SEPARATOR.getRecipeHandler().recipes.clear();
   NCRecipes.Type.MANUFACTORY.getRecipeHandler().recipes.clear();
   NCRecipes.Type.ALLOY_FURNACE.getRecipeHandler().recipes.clear();
   NCRecipes.Type.CHEMICAL_REACTOR.getRecipeHandler().recipes.clear();
   NCRecipes.Type.SUPERCOOLER.getRecipeHandler().recipes.clear();
   NCRecipes.Type.INFUSER.getRecipeHandler().recipes.clear();
   NCRecipes.Type.INGOT_FORMER.getRecipeHandler().recipes.clear();
   NCRecipes.Type.MELTER.getRecipeHandler().recipes.clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : manufactory)
   NCRecipes.Type.MANUFACTORY.getRecipeHandler().addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
  for (Object[] recipe : separator)
   NCRecipes.Type.ISOTOPE_SEPARATOR.getRecipeHandler().addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2], (int) recipe[3]);
  for (Object[] recipe : pressurizer)
   NCRecipes.Type.PRESSURIZER.getRecipeHandler().addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
 }

 @ScriptFunction
 public void addManufactory(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addManufactory('<output> <input> <time>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  manufactory.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addIsotopeSeparator(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addIsotopeSeparator('<output> <output2> <input> <time')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(EnumInputType.INTEGER, helper, input[3]);
  separator.add(new Object[]{convertStack(helper, input[2]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[3])});
 }


 @ScriptFunction
 public void addPressurizer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addPressurizer('<output> <input> <time>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  pressurizer.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2])});
 }
}
