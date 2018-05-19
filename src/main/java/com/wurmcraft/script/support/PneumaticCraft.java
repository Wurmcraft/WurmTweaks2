package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.common.recipes.AmadronOfferManager;
import me.desht.pneumaticcraft.common.recipes.AssemblyRecipe;
import me.desht.pneumaticcraft.common.recipes.PressureChamberRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PneumaticCraft extends SupportBase {

 private List<Object[]> drill = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> laser = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> pressure = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> amadron = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> defaultAmadron = Collections.synchronizedList(new ArrayList<>());

 public PneumaticCraft() {
  super("pneumaticcraft");
 }

 @Override
 public void init() {
  drill.clear();
  laser.clear();
  pressure.clear();
  amadron.clear();
  defaultAmadron.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   AssemblyRecipe.drillRecipes.clear();
   AssemblyRecipe.laserRecipes.clear();
   PressureChamberRecipe.chamberRecipes.clear();
   AmadronOfferManager.getInstance().getStaticOffers().clear();
   AmadronOfferManager.getInstance().getPeriodicOffers().clear();
   AmadronOfferManager.getInstance().getAllOffers().clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : drill)
   PneumaticRegistry.getInstance().getRecipeRegistry().addAssemblyDrillRecipe(recipe[0], recipe[1]);
  for (Object[] recipe : laser)
   PneumaticRegistry.getInstance().getRecipeRegistry().addAssemblyLaserRecipe(recipe[0], recipe[1]);
  for (Object[] recipe : pressure)
   PneumaticRegistry.getInstance().getRecipeRegistry().registerPressureChamberRecipe((ItemStack[]) recipe[0], (float) recipe[1], (ItemStack[]) recipe[2]);
  for (Object[] recipe : defaultAmadron)
   PneumaticRegistry.getInstance().getRecipeRegistry().registerDefaultStaticAmadronOffer(recipe[0], recipe[1]);
  for (Object[] recipe : amadron)
   PneumaticRegistry.getInstance().getRecipeRegistry().registerDefaultPeriodicAmadronOffer(recipe[0], recipe[1]);
 }

 @ScriptFunction
 public void addAssemblyDrill(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addAssemblyDrill('<output> <input')");
  isValid(helper, input[0], input[1]);
  drill.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addAssemblyLaser(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addAssemblyLaser('<output> <input')");
  isValid(helper, input[0], input[1]);
  laser.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addPressureChamber(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 3, "addPressureChamber('<output> <pressure> <input>...')");
  isValid(helper, input[0]);
  isValid(EnumInputType.FLOAT, helper, input[1]);
  List<ItemStack> inputs = new ArrayList<>();
  for (int index = 2; index < input.length; index++) {
   isValid(helper, input[index]);
   inputs.add(convertStack(helper, input[index]));
  }
  pressure.add(new Object[]{inputs.toArray(new ItemStack[0]), convertFloat(input[1]), new ItemStack[]{convertStack(helper, input[0])}});
 }

 @ScriptFunction
 public void addDefaultAmadron(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addDefaultAmadron('<output> <input>')");
  isValid(helper, input[0], input[1]);
  defaultAmadron.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1])});
 }

 @ScriptFunction
 public void addAmadron(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addDefaultAmadron('<output> <input>')");
  isValid(helper, input[0], input[1]);
  amadron.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1])});
 }
}
