package com.wurmcraft.script.support;

import charcoalPit.crafting.OreSmeltingRecipes;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharcoalPit extends SupportBase {

 private List<OreSmeltingRecipes.AlloyRecipe> bloomery = Collections.synchronizedList(new ArrayList<>());

 public CharcoalPit() {
  super("charcoal_pit");
 }

 @Override
 public void init() {
  bloomery.clear();
  if (ConfigHandler.removeAllMachineRecipes)
   OreSmeltingRecipes.alloyRecipes.clear();
 }

 @ScriptFunction
 public void addBloomery(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addBloomery('<output> <input')");
  isValid(helper, input[0], input[1]);
  bloomery.add(new OreSmeltingRecipes.AlloyRecipe(convertStack(helper, input[0]), convertStack(helper, input[0]).getCount(), true, true, convertStack(helper, input[1])));
 }

 @Override
 public void finishSupport() {
  for (OreSmeltingRecipes.AlloyRecipe recipe : bloomery)
   OreSmeltingRecipes.addAlloyRecipe(recipe);
 }
}
