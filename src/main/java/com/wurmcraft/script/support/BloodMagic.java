package com.wurmcraft.script.support;


import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BloodMagic extends SupportHelper {

 private List<Object[]> altar = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> array = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> forge = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> table = Collections.synchronizedList(new ArrayList<>());

 public BloodMagic() {
  super("bloodmagic");
 }

 @Override
 public void init() {
  altar.clear();
  array.clear();
  forge.clear();
  table.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   for (RecipeAlchemyArray array : BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArrayRecipes())
    for (ItemStack input : array.getInput().getMatchingStacks())
     for (ItemStack cat : array.getCatalyst().getMatchingStacks())
      BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyArray(input, cat);
   for (RecipeBloodAltar recipe : BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes())
    for (ItemStack input : recipe.getInput().getMatchingStacks())
     BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeBloodAltar(input);
   for (RecipeTartaricForge recipe : BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForgeRecipes())
    for (Ingredient input : recipe.getInput())
     for (ItemStack stack : input.getMatchingStacks())
      BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeTartaricForge(stack);
   for (RecipeAlchemyTable recipe : BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyRecipes())
    for (Ingredient input : recipe.getInput())
     for (ItemStack stack : input.getMatchingStacks())
      BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyTable(stack);
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : altar)
   BloodMagicAPI.INSTANCE.getRecipeRegistrar().addBloodAltar((Ingredient) recipe[0], (ItemStack) recipe[1], (int) recipe[2], (int) recipe[3], (int) recipe[4], (int) recipe[5]);
  for (Object[] recipe : array)
   BloodMagicAPI.INSTANCE.getRecipeRegistrar().addAlchemyArray((Ingredient) recipe[0], (Ingredient) recipe[1], (ItemStack) recipe[2], null);
  for (Object[] recipe : forge)
   BloodMagicAPI.INSTANCE.getRecipeRegistrar().addTartaricForge((ItemStack) recipe[0], (float) recipe[1], (float) recipe[2], (ItemStack[]) recipe[3]);
  for (Object[] recipe : table)
   BloodMagicAPI.INSTANCE.getRecipeRegistrar().addAlchemyTable((ItemStack) recipe[0], (int) recipe[1], (int) recipe[2], (int) recipe[3], (Ingredient[]) recipe[4]);
 }

 @ScriptFunction
 public void addAltar(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 6, "addAltar('<output> <input> <tier> <syphon> <consume> <drain>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2], input[3], input[4], input[5]);
  altar.add(new Object[]{convertIngredient(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2]), convertInteger(input[3]), convertInteger(input[4]), convertInteger(input[5])});
 }

 @ScriptFunction
 public void addAlchemyArray(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addAlchemyArray('<output> <catalyst> <input')");
  array.add(new Object[]{convertIngredient(helper, input[2]), convertIngredient(helper, input[1]), convertStack(helper, input[0]), null});
 }

 @ScriptFunction
 public void addSoulForge(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 4, "addSoulForge('<output> <souls> <drain> <input>...')");
  isValid(helper, input[0]);
  isValid(Types.FLOAT, helper, input[2]);
  List<ItemStack> inputs = new ArrayList<>();
  for (int index = 3; index < input.length; index++) {
   isValid(helper, input[index]);
   inputs.add(convertStack(helper, input[index]));
  }
  forge.add(new Object[]{convertStack(helper, input[0]), convertFloat(input[0]), convertFloat(input[1]), inputs.toArray(new ItemStack[0])});
 }

 @ScriptFunction
 public void addTable(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 5, "addTable('<output> <syphon> <ticks> <tier> <input>...')");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1], input[2], input[3]);
  List<Ingredient> inputs = new ArrayList<>();
  for (int index = 4; index < input.length; index++) {
   isValid(helper, input[index]);
   inputs.add(convertIngredient(helper, input[index]));
  }
  table.add(new Object[]{convertStack(helper, input[0]), convertInteger(input[1]), convertInteger(input[2]), convertInteger(input[3]), inputs.toArray()});
 }
}
