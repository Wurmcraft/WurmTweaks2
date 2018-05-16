package com.wurmcraft.script.support;


import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbyssalCraft extends SupportHelper {

 private List<Object[]> crystallizerRecipes = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> transmutatorRecipes = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> materializerRecipes = Collections.synchronizedList(new ArrayList<>());

 public AbyssalCraft() {
  super("abyssalcraft");
 }

 @Override
 public void init() {
  crystallizerRecipes.clear();
  transmutatorRecipes.clear();
  materializerRecipes.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   CrystallizerRecipes.instance().getCrystallizationList().clear();
   TransmutatorRecipes.instance().getTransmutationList().clear();
   MaterializerRecipes.instance().getMaterializationList().clear();
  }
 }

 @ScriptFunction
 public void addCrystallizer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addCrystallizer('<output> <output2> <input> <exp>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(EnumInputType.INTEGER, helper, input[3]);
  crystallizerRecipes.add(new Object[]{convertStack(helper, input[2]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertFloat(input[3])});
 }

 @ScriptFunction
 public void addTransmutator(StackHelper helper, String line) {
  String input[] = validateFormat(line, line.split(" ").length == 3, "addTransmutator('<output> <input> <energy>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  transmutatorRecipes.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertFloat(input[2])});
 }

 @ScriptFunction
 public void addMaterializer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addMaterializer('<output> <input>...')");
  isValid(helper, input);
  List<ItemStack> inputStacks = new ArrayList<>();
  for (int index = 1; index < input.length; index++)
   inputStacks.add(convertStack(helper, input[index]));
  materializerRecipes.add(new Object[]{inputStacks.toArray(new ItemStack[0]), convertStack(helper, input[0])});
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : crystallizerRecipes)
   CrystallizerRecipes.instance().crystallize((ItemStack) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2], (float) recipe[3]);
  for (Object[] recipe : transmutatorRecipes)
   TransmutatorRecipes.instance().transmute((ItemStack) recipe[0], (ItemStack) recipe[1], (float) recipe[2]);
  for (Object[] recipe : materializerRecipes)
   MaterializerRecipes.instance().materialize((ItemStack[]) recipe[0], (ItemStack) recipe[1]);
 }
}
