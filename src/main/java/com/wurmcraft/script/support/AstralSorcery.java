package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO Missing Support
public class AstralSorcery extends SupportBase {

 private List<Object[]> basic = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> slow = Collections.synchronizedList(new ArrayList<>());

 public AstralSorcery() {
  super("astralsorcery");
 }

 @Override
 public void init() {
  basic.clear();
  slow.clear();
 }

 @ScriptFunction
 public void addBasicInfusion(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addBasicInfusion('<output> <input>')");
  isValid(helper, input[0], input[1]);
  basic.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1])});
 }

 @ScriptFunction
 public void addSlowInfusion(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addSlowInfusion('<output> <input>')");
  isValid(helper, input[0], input[1]);
  slow.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1])});
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : basic)
   InfusionRecipeRegistry.registerBasicInfusion((ItemStack) recipe[0], (ItemStack) recipe[1]);
  for (Object[] recipe : slow)
   InfusionRecipeRegistry.registerLowConsumptionInfusion((ItemStack) recipe[0], (ItemStack) recipe[1]);
 }
}
