package com.wurmcraft.script.support;


import com.rwtema.extrautils2.api.machine.XUMachineCrusher;
import com.rwtema.extrautils2.tile.TileResonator;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtraUtils2 extends SupportBase {

 private List<Object[]> resonator = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> crusher = Collections.synchronizedList(new ArrayList<>());

 public ExtraUtils2() {
  super("extrautils2");
 }

 @Override
 public void init() {
  resonator.clear();
  crusher.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   TileResonator.resonatorRecipes.clear();
   while (XUMachineCrusher.INSTANCE.recipes_registry.iterator().hasNext())
    XUMachineCrusher.INSTANCE.recipes_registry.removeRecipe(XUMachineCrusher.INSTANCE.recipes_registry.iterator().next());
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : resonator)
   TileResonator.register((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
  for (Object[] recipe : crusher)
   if (recipe.length == 4)
    XUMachineCrusher.addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2], (float) recipe[3]);
   else
    XUMachineCrusher.addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1]);
 }

 @ScriptFunction
 public void addResonator(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addResonator('<output> <input> <energy>'");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  resonator.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addXUCrusher(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2 || line.split(" ").length == 4, "addXUCrusher('<output> <input> | <secOutput> <secOutput%>')");
  isValid(helper, input[0], input[1]);
  if (line.split(" ").length == 2)
   crusher.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
  else {
   isValid(helper, input[2]);
   isValid(EnumInputType.FLOAT, helper, input[3]);
   crusher.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, (input[2])), convertFloat(input[3])});
  }
 }
}
