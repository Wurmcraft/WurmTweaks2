package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActuallyAdditions extends SupportBase {

 private List<Object[]> empowerer = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> reconstructor = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> crusher = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> miningLensStone = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> miningLensNether = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> composter = Collections.synchronizedList(new ArrayList<>());

 public ActuallyAdditions() {
  super("actuallyadditions");
 }

 @Override
 public void init() {
  empowerer.clear();
  reconstructor.clear();
  crusher.clear();
  miningLensStone.clear();
  miningLensNether.clear();
  composter.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   ActuallyAdditionsAPI.CRUSHER_RECIPES.clear();
   ActuallyAdditionsAPI.TREASURE_CHEST_LOOT.clear();
   ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES.clear();
   ActuallyAdditionsAPI.EMPOWERER_RECIPES.clear();
   ActuallyAdditionsAPI.COMPOST_RECIPES.clear();
   ActuallyAdditionsAPI.STONE_ORES.clear();
   ActuallyAdditionsAPI.NETHERRACK_ORES.clear();
  }
 }

 @ScriptFunction
 public void addEmpowerer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 8, "addEmpowerer('<output> <inputCenter> <input> <input2> <input3> <input4> <energyPerStand> <time>')");
  isValid(helper, input[0], input[1], input[2], input[3], input[4], input[5]);
  isValid(EnumInputType.INTEGER, helper, input[6], input[7]);
  empowerer.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, input[2]), convertStack(helper, input[3]), convertStack(helper, input[4]), convertStack(helper, input[5]), convertInteger(input[6]), convertInteger(input[7])});
 }

 @ScriptFunction
 public void addReconstructor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addReconstructor('<output> <input> <energy>')");
  isValid(helper, input[0], input[1]);
  isValid(EnumInputType.INTEGER, helper, input[2]);
  reconstructor.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addAACrusher(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addAACrusher('<output> <input> <secOutput> <secOutput%>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(EnumInputType.INTEGER, helper, input[3]);
  crusher.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, input[2]), convertInteger(input[3])});
 }


 @ScriptFunction
 public void addMiningLensStone(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addMiningLensStone('<oreDict> <weight>')");
  isValid(EnumInputType.STRING, helper, input[0]);
  isValid(EnumInputType.INTEGER, helper, input[1]);
  miningLensStone.add(new Object[]{input[0], convertInteger(input[1])});
 }

 @ScriptFunction
 public void addMiningLensNether(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addMiningLensNether('<oreDict> <weight>')");
  isValid(EnumInputType.STRING, helper, input[0]);
  isValid(EnumInputType.INTEGER, helper, input[1]);
  miningLensNether.add(new Object[]{input[0], convertInteger(input[1])});
 }

 @ScriptFunction
 public void addComposter(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2 || line.split(" ").length == 4, "addComposter('<output> <input> | <outputDisplay> <inputDisplay>')");
  isValid(helper, input[0], input[1]);
  if (line.split(" ").length == 2) {
   composter.add(new Object[]{convertStack(helper, input[1]), Block.getBlockFromItem(convertStack(helper, input[1]).getItem()), convertStack(helper, input[0]), Block.getBlockFromItem(convertStack(helper, input[0]).getItem())});
  } else {
   isValid(helper, input[2], input[3]);
   composter.add(new Object[]{convertStack(helper, input[1]), Block.getBlockFromItem(convertStack(helper, input[2]).getItem()), convertStack(helper, input[0]), Block.getBlockFromItem(convertStack(helper, input[3]).getItem())});
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : empowerer)
   ActuallyAdditionsAPI.addEmpowererRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2], (ItemStack) recipe[3], (ItemStack) recipe[4], (ItemStack) recipe[5], (int) recipe[6], (int) recipe[7], new float[]{1F, 91F / 255F, 76F / 255F});
  for (Object[] recipe : reconstructor)
   ActuallyAdditionsAPI.addReconstructorLensConversionRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
  for (Object[] recipe : crusher)
   ActuallyAdditionsAPI.addCrusherRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2], (int) recipe[3]);
  for (Object[] recipe : miningLensStone)
   ActuallyAdditionsAPI.addMiningLensStoneOre((String) recipe[0], (int) recipe[1]);
  for (Object[] recipe : miningLensNether)
   ActuallyAdditionsAPI.addMiningLensNetherOre((String) recipe[0], (int) recipe[1]);
  for (Object[] recipe : composter)
   ActuallyAdditionsAPI.addCompostRecipe((ItemStack) recipe[0], (Block) recipe[1], (ItemStack) recipe[2], (Block) recipe[3]);
 }
}
