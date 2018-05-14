package com.wurmcraft.script.support;

import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_botanic.VMBotanic;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_ore.VMOre;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_res.VMRes;
import com.valkyrieofnight.vlib.lib.stack.WeightedItemStack;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnvironmentalTech extends SupportHelper {

 private List<Object[]> botanic = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> res = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> ore = Collections.synchronizedList(new ArrayList<>());

 public EnvironmentalTech() {
  super("environmentaltech");
 }

 @Override
 public void init() {
  if (ConfigHandler.Script.removeAllMachineRecipes) {
   VMBotanic.getInstance().T1.getList().clear();
   VMBotanic.getInstance().T1.getTargeters().clear();
   VMBotanic.getInstance().T2.getList().clear();
   VMBotanic.getInstance().T2.getTargeters().clear();
   VMBotanic.getInstance().T3.getList().clear();
   VMBotanic.getInstance().T3.getTargeters().clear();
   VMBotanic.getInstance().T4.getList().clear();
   VMBotanic.getInstance().T4.getTargeters().clear();
   VMBotanic.getInstance().T5.getList().clear();
   VMBotanic.getInstance().T5.getTargeters().clear();
   VMBotanic.getInstance().T6.getList().clear();
   VMBotanic.getInstance().T6.getTargeters().clear();
   VMRes.getInstance().T1.getList().clear();
   VMRes.getInstance().T1.getTargeters().clear();
   VMRes.getInstance().T2.getList().clear();
   VMRes.getInstance().T2.getTargeters().clear();
   VMRes.getInstance().T3.getList().clear();
   VMRes.getInstance().T3.getTargeters().clear();
   VMRes.getInstance().T4.getList().clear();
   VMRes.getInstance().T4.getTargeters().clear();
   VMRes.getInstance().T5.getList().clear();
   VMRes.getInstance().T5.getTargeters().clear();
   VMRes.getInstance().T6.getList().clear();
   VMRes.getInstance().T6.getTargeters().clear();
   VMOre.getInstance().VOM_T1.getList().clear();
   VMOre.getInstance().VOM_T1.getTargeters().clear();
   VMOre.getInstance().VOM_T2.getList().clear();
   VMOre.getInstance().VOM_T2.getTargeters().clear();
   VMOre.getInstance().VOM_T3.getList().clear();
   VMOre.getInstance().VOM_T3.getTargeters().clear();
   VMOre.getInstance().VOM_T4.getList().clear();
   VMOre.getInstance().VOM_T4.getTargeters().clear();
   VMOre.getInstance().VOM_T5.getList().clear();
   VMOre.getInstance().VOM_T5.getTargeters().clear();
   VMOre.getInstance().VOM_T6.getList().clear();
   VMOre.getInstance().VOM_T6.getTargeters().clear();
  }
 }

 //TODO CLEAN THIS UP
 @Override
 public void finishSupport() {
  for (Object[] recipe : botanic) {
   switch ((int)recipe[0]) {
    case (1):
     VMBotanic.getInstance().T1.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (2):
     VMBotanic.getInstance().T2.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (3):
     VMBotanic.getInstance().T3.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (4):
     VMBotanic.getInstance().T4.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (5):
     VMBotanic.getInstance().T5.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (6):
     VMBotanic.getInstance().T6.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
   }
  }
  for (Object[] recipe : res) {
   switch ((int)recipe[0]) {
    case (1):
     VMRes.getInstance().T1.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (2):
     VMRes.getInstance().T2.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (3):
     VMRes.getInstance().T3.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (4):
     VMRes.getInstance().T4.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (5):
     VMRes.getInstance().T5.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (6):
     VMRes.getInstance().T6.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
   }
  }
  for (Object[] recipe : ore) {
   switch ((int)recipe[0]) {
    case (1):
     VMOre.getInstance().VOM_T1.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (2):
     VMOre.getInstance().VOM_T2.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (3):
     VMOre.getInstance().VOM_T3.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (4):
     VMOre.getInstance().VOM_T4.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (5):
     VMOre.getInstance().VOM_T5.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
    case (6):
     VMOre.getInstance().VOM_T6.addResource((WeightedItemStack)recipe[1], (String)recipe[2]);
     break;
   }
  }
 }

 @ScriptFunction
 public void addBotanicMiner(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addBotanicMiner('<stack> <weight> <tier> <color>'");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1], input[2]);
  botanic.add(new Object[]{
   convertInteger(input[2]),
   new WeightedItemStack(convertStack(helper, input[0]), convertInteger(input[1])),
   (String)input[3]
  });
 }

 @ScriptFunction
 public void addResourceMiner(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addBotanicMiner('<stack> <weight> <tier> <color>'");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1], input[2]);
  res.add(new Object[]{
   convertInteger(input[2]),
   new WeightedItemStack(convertStack(helper, input[0]), convertInteger(input[1])),
   (String)input[3]
  });
 }

 @ScriptFunction
 public void addOreMiner(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addBotanicMiner('<stack> <weight> <tier> <color>'");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1], input[2]);
  ore.add(new Object[]{
   convertInteger(input[2]),
   new WeightedItemStack(convertStack(helper, input[0]), convertInteger(input[1])),
   (String)input[3]
  });
 }
}
