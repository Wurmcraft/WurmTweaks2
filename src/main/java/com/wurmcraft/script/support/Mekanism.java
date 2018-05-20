package com.wurmcraft.script.support;


import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.StackSettings;
import com.wurmcraft.script.utils.SupportBase;
import mekanism.api.MekanismAPI;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.recipe.RecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mekanism extends SupportBase {

 private List<Object[]> enricher = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> osmiumCompressor = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> combiner = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> crusher = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> purification = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> metaInfuser = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> chemInfuser = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> oxidiser = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> chemInjection = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> electroSeperator = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> sawmill = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> chemicalDissolution = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> washer = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> crystallizer = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> pressure = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> thermal = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> solar = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> box = Collections.synchronizedList(new ArrayList<>());

 public Mekanism() {
  super("mekanism");
 }

 public static String getGases() {
  StringBuilder builder = new StringBuilder();
  for (Gas stack : GasRegistry.getRegisteredGasses())
   if (stack != null && stack.getFluid() != null)
    builder.append(stack.getFluid().getUnlocalizedName() + "\n");
  return builder.toString();
 }

 @Override
 public void finishSupport() {
  for (Object[] r : enricher)
   RecipeHandler.addEnrichmentChamberRecipe((ItemStack) r[0], (ItemStack) r[1]);
  for (Object[] r : osmiumCompressor)
   RecipeHandler.addOsmiumCompressorRecipe((ItemStack) r[0], (ItemStack) r[1]);
  for (Object[] r : combiner)
   RecipeHandler.addCombinerRecipe((ItemStack) r[0], (ItemStack) r[1]);
  for (Object[] r : crusher)
   RecipeHandler.addCrusherRecipe((ItemStack) r[0], (ItemStack) r[1]);
  for (Object[] r : metaInfuser)
   RecipeHandler.addMetallurgicInfuserRecipe((InfuseType) r[0], (int) r[1], (ItemStack) r[2], (ItemStack) r[3]);
  for (Object[] r : chemInfuser)
   RecipeHandler.addChemicalInfuserRecipe((GasStack) r[0], (GasStack) r[1], (GasStack) r[2]);
  for (Object[] r : oxidiser)
   RecipeHandler.addChemicalOxidizerRecipe((ItemStack) r[0], (GasStack) r[1]);
  for (Object[] r : chemInjection)
   RecipeHandler.addChemicalInjectionChamberRecipe((ItemStack) r[0], (Gas) r[1], (ItemStack) r[2]);
  for (Object[] r : electroSeperator)
   RecipeHandler.addElectrolyticSeparatorRecipe((FluidStack) r[0], (double) r[1], (GasStack) r[2], (GasStack) r[3]);
  for (Object[] r : sawmill)
   if (r.length == 4)
    RecipeHandler.addPrecisionSawmillRecipe((ItemStack) r[0], (ItemStack) r[1], (ItemStack) r[2], (float) r[3]);
   else
    RecipeHandler.addPrecisionSawmillRecipe((ItemStack) r[0], (ItemStack) r[1]);
  for (Object[] r : chemicalDissolution)
   RecipeHandler.addChemicalDissolutionChamberRecipe((ItemStack) r[0], (GasStack) r[1]);
  for (Object[] r : washer)
   RecipeHandler.addChemicalWasherRecipe((GasStack) r[0], (GasStack) r[1]);
  for (Object[] r : crystallizer)
   RecipeHandler.addChemicalCrystallizerRecipe((GasStack) r[0], (ItemStack) r[1]);
  for (Object[] r : pressure)
   RecipeHandler.addPRCRecipe((ItemStack) r[0], (FluidStack) r[1], (GasStack) r[2], (ItemStack) r[3], (GasStack) r[4], (double) r[5], (int) r[6]);
  for (Object[] r : thermal)
   RecipeHandler.addThermalEvaporationRecipe((FluidStack) r[0], (FluidStack) r[1]);
  for (Object[] r : solar)
   RecipeHandler.addSolarNeutronRecipe((GasStack) r[0], (GasStack) r[1]);
  for (Object[] r : box)
   MekanismAPI.addBoxBlacklist((Block) r[0], (int) r[1]);
 }

 @Override
 public void init() {
  enricher.clear();
  osmiumCompressor.clear();
  combiner.clear();
  crusher.clear();
  purification.clear();
  metaInfuser.clear();
  chemInfuser.clear();
  oxidiser.clear();
  chemInjection.clear();
  electroSeperator.clear();
  sawmill.clear();
  chemicalDissolution.clear();
  washer.clear();
  crystallizer.clear();
  pressure.clear();
  thermal.clear();
  solar.clear();
  box.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   RecipeHandler.Recipe.CHEMICAL_CRYSTALLIZER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_INFUSER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_INJECTION_CHAMBER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_WASHER.get().clear();
   RecipeHandler.Recipe.COMBINER.get().clear();
   RecipeHandler.Recipe.CRUSHER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_WASHER.get().clear();
   RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get().clear();
   RecipeHandler.Recipe.ENERGIZED_SMELTER.get().clear();
   RecipeHandler.Recipe.ENRICHMENT_CHAMBER.get().clear();
   RecipeHandler.Recipe.PURIFICATION_CHAMBER.get().clear();
   RecipeHandler.Recipe.PRECISION_SAWMILL.get().clear();
   RecipeHandler.Recipe.METALLURGIC_INFUSER.get().clear();
   RecipeHandler.Recipe.THERMAL_EVAPORATION_PLANT.get().clear();
   RecipeHandler.Recipe.SOLAR_NEUTRON_ACTIVATOR.get().clear();
   RecipeHandler.Recipe.OSMIUM_COMPRESSOR.get().clear();
  }
 }

 @ScriptFunction
 public void addEnrichmentChamber(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addEnrichmentChamber('<output> <input>')");
  isValid(helper, input[0], input[1]);
  enricher.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addOsmiumCompressor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addOsmiumCompressor('<output> <input>')");
  isValid(helper, input[0], input[1]);
  osmiumCompressor.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addCombiner(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addCombiner('<output> <input>')");
  isValid(helper, input[0], input[1]);
  combiner.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addMCrusher(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addMCrusher('<output> <input>')");
  isValid(helper, input[0], input[1]);
  crusher.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addPurification(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addPurification('<output> <input>')");
  isValid(helper, input[0], input[1]);
  purification.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addMetallurgicInfuser(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addMetallurgicInfuser('<output> <input> <infusionType> <amount>')");
  isValid(helper, input[0], input[1]);
  InfuseType type = InfuseRegistry.get(input[2]);
  Preconditions.checkNotNull(type);
  isValid(EnumInputType.INTEGER, helper, input[3]);
  metaInfuser.add(new Object[]{type, convertInteger(input[3]), convertStack(helper, input[1]), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addChemicalInfuser(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addChemicalInfuser('<%output> <%input> <%input2>')");
  GasStack output = getGasStack(input[0]);
  Preconditions.checkNotNull(output);
  GasStack inputStack = getGasStack(input[1]);
  Preconditions.checkNotNull(inputStack);
  GasStack inputStack2 = getGasStack(input[2]);
  Preconditions.checkNotNull(inputStack2);
  chemInfuser.add(new Object[]{inputStack, inputStack2, output});
 }

 @ScriptFunction
 public void addChemicalOxidizer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addChemicalOxidizer('<%output> <input>')");
  GasStack output = getGasStack(input[0]);
  Preconditions.checkNotNull(output);
  isValid(helper, input[1]);
  oxidiser.add(new Object[]{convertStack(helper, input[1]), output});
 }

 @ScriptFunction
 public void addChemicalInjection(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split("").length == 3, "addChemicalInjection('<output> <input> <%input>')");
  isValid(helper, input[0], input[1]);
  GasStack inputStack = getGasStack(input[2]);
  Preconditions.checkNotNull(inputStack);
  chemInjection.add(new Object[]{convertStack(helper, input[1]), inputStack.getGas(), convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addElectrolyticSeparator(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addElectrolyticSeparator('<leftOutput> <rightOutput> <*input> <energy>')");
  GasStack leftOutput = getGasStack(input[0]);
  Preconditions.checkNotNull(leftOutput);
  GasStack rightOutput = getGasStack(input[1]);
  Preconditions.checkNotNull(rightOutput);
  isValid(EnumInputType.FLUIDSTACK, helper, input[2]);
  isValid(EnumInputType.INTEGER, helper, input[3]);
  electroSeperator.add(new Object[]{convertFluidStack(helper, input[2]), convertFloat(input[3]), leftOutput, rightOutput});

 }

 @ScriptFunction
 public void addSawmill(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2 || line.split(" ").length == 4, "addSawmill('<output> <input> | <secOutput> <secOutput%>')");
  isValid(helper, input[0], input[1]);
  if (line.split(" ").length == 2) {
   sawmill.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0])});
  } else {
   isValid(helper, input[2]);
   isValid(EnumInputType.FLOAT, helper, input[3]);
   sawmill.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, input[0]), convertStack(helper, input[2]), convertFloat(input[3])});
  }
 }

 @ScriptFunction
 public void addChemicalDissolution(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addChemicalDissolution('<%output> <input>')");
  GasStack output = getGasStack(input[0]);
  Preconditions.checkNotNull(output);
  isValid(helper, input[1]);
  chemicalDissolution.add(new Object[]{convertStack(helper, input[1]), output});
 }

 @ScriptFunction
 public void addChemicalWasher(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addChemicalWasher('<%output> <%input>')");
  GasStack output = getGasStack(input[0]);
  Preconditions.checkNotNull(output);
  GasStack inputStack = getGasStack(input[1]);
  Preconditions.checkNotNull(inputStack);
  washer.add(new Object[]{inputStack, output});
 }

 @ScriptFunction
 public void addChemicalCrystallizer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addChemicalCrystallizer('<output> <%input>')");
  isValid(helper, input[0]);
  GasStack inputStack = getGasStack(input[1]);
  Preconditions.checkNotNull(inputStack);
  crystallizer.add(new Object[]{inputStack, convertStack(helper, input[0])});
 }

 @ScriptFunction()
 public void addMekPressureChamber(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addMekPressureChamber('<output> <%outputGas> <%inputGas> <*inputFluid> <input> <extraEnergy> <time>')");
  isValid(helper, input[0], input[4]);
  isValid(EnumInputType.FLOAT, helper, input[5]);
  isValid(EnumInputType.INTEGER, helper, input[6]);
  GasStack outputStack = getGasStack(input[1]);
  Preconditions.checkNotNull(outputStack);
  GasStack inputStack = getGasStack(input[2]);
  Preconditions.checkNotNull(inputStack);
  isValid(EnumInputType.FLUIDSTACK, helper, input[3]);
  pressure.add(new Object[]{convertStack(helper, input[4]), convertFluidStack(helper, input[3]), inputStack, convertStack(helper, input[0]), outputStack, (double) convertFloat(input[5]), convertInteger(input[6])});
 }

 @ScriptFunction
 public void addThermalEvaporation(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addThermalEvaporation('<*output> <*input>')");
  thermal.add(new Object[]{convertFluidStack(helper, input[0]), convertFluidStack(helper, input[1])});
 }

 @ScriptFunction
 public void addSolarNeutron(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addSolarNeutron('<%output> <%input>')");
  GasStack output = getGasStack(input[0]);
  Preconditions.checkNotNull(output);
  GasStack inputStack = getGasStack(input[1]);
  Preconditions.checkNotNull(inputStack);
  solar.add(new Object[]{inputStack, output});
 }

 @ScriptFunction
 public void addBoxBlacklist(StackHelper helper, String line) {
  String[] input = line.split(" ");
  isValid(helper, input[0]);
  Block block = Block.getBlockFromItem(convertStack(helper, input[0]).getItem());
  Preconditions.checkArgument(block != Blocks.AIR);
  box.add(new Object[]{block, convertStack(helper, input[0]).getItemDamage()});
 }

 private GasStack getGasStack(String stack) {
  if (stack.startsWith(StackSettings.FRONT + StackSettings.GAS.getFormatting())) {
   String name = stack.substring(stack.indexOf(StackSettings.FRONT.getFormatting().charAt(0)) + 1, stack.indexOf(StackSettings.BACK.getFormatting().charAt(0)));
   int amount = Integer.parseInt(stack.substring(stack.indexOf(StackSettings.FRONT.getFormatting() + StackSettings.GAS.getFormatting()) + 2, stack.indexOf(StackSettings.STACK_SIZE.getFormatting())));
   if (GasRegistry.containsGas(name.toUpperCase()))
    return new GasStack(GasRegistry.getGas(name.toUpperCase()), amount);
  }
  return null;
 }
}
