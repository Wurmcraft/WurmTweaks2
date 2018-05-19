package com.wurmcraft.script.support;


import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Botania extends SupportBase {

 private List<Object[]> conjuration = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> alchemy = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> infusion = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> elven = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> rune = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> pureDaisy = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> apohecary = Collections.synchronizedList(new ArrayList<>());

 public Botania() {
  super("botania");
 }

 @Override
 public void init() {
  conjuration.clear();
  infusion.clear();
  elven.clear();
  rune.clear();
  pureDaisy.clear();
  apohecary.clear();
  alchemy.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   BotaniaAPI.manaInfusionRecipes.clear();
   BotaniaAPI.elvenTradeRecipes.clear();
   BotaniaAPI.pureDaisyRecipes.clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : conjuration)
   BotaniaAPI.registerManaConjurationRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
  for (Object[] recipe : infusion)
   BotaniaAPI.registerManaInfusionRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
  for (Object[] recipe : elven)
   BotaniaAPI.registerElvenTradeRecipe((ItemStack[]) recipe[0], (ItemStack) recipe[1]);
  for (Object[] recipe : rune)
   BotaniaAPI.registerRuneAltarRecipe((ItemStack) recipe[0], (int) recipe[1], (Object[]) recipe[2]);
  for (Object[] recipe : pureDaisy)
   BotaniaAPI.registerPureDaisyRecipe(recipe[0], (IBlockState) recipe[1]);
  for (Object[] recipe : apohecary)
   BotaniaAPI.registerPetalRecipe((ItemStack) recipe[0], (Object[]) recipe[1]);
  for (Object[] recipe : alchemy)
   BotaniaAPI.registerManaAlchemyRecipe((ItemStack) recipe[0], recipe[1], (int) recipe[2]);
 }

 @ScriptFunction
 public void addApothecary(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addApothecary('<output> <input>...')");
  List<Object> inputItems = new ArrayList<>();
  for (String l : Arrays.copyOfRange(input, 1, input.length)) {
   ItemStack inputStack = convertStack(helper, l);
   if (inputStack != ItemStack.EMPTY)
    inputItems.add(inputItems);
   else if (l.startsWith("<") && l.endsWith(">") && OreDictionary.doesOreNameExist(l.substring(1, l.length() - 1)))
    inputItems.add(l.substring(1, l.length() - 1));
  }
  apohecary.add(new Object[]{convertStack(helper, input[0]), inputItems.toArray(new Object[0])});
 }

 @ScriptFunction
 public void addPureDaisy(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addPureDaisy('<output> <input>')");
  isValid(helper, input[0], input[1]);
  pureDaisy.add(new Object[]{Block.getBlockFromItem(convertStack(helper, input[1]).getItem()), Block.getBlockFromItem(convertStack(helper, input[0]).getItem()).getDefaultState()});
 }

 @ScriptFunction
 public void addRune(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 3, "addRune('<output> <mana> <input>...')");
  isValid(helper, input[0]);
  isValid(EnumInputType.INTEGER, helper, input[1]);
  List<Object> inputItems = new ArrayList<>();
  for (String l : Arrays.copyOfRange(input, 2, input.length)) {
   ItemStack inputStack = convertStack(helper, l);
   if (inputStack != ItemStack.EMPTY)
    inputItems.add(inputStack);
   else if (l.startsWith("<") && l.endsWith(">") && OreDictionary.doesOreNameExist(l.substring(1, l.length() - 1)))
    inputItems.add(l.substring(1, l.length() - 1));
   rune.add(new Object[]{convertStack(helper, input[0]), convertInteger(input[1]), inputItems.toArray(new Object[0])});
  }
 }

 @ScriptFunction
 public void addElven(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addElven('<output> <input>')");
  isValid(helper, input[0], input[1]);
  elven.add(new Object[]{new ItemStack[]{convertStack(helper, input[0])}, convertStack(helper, input[1])});
 }

 @ScriptFunction
 public void addInfusion(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addInfusion('<output> <input> <mana>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  infusion.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addAlchemy(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addAlchemy('<output> <input> <mana>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  alchemy.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addConjuration(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addConjuration('<output> <input> <mana>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  conjuration.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[2])});
 }
}
