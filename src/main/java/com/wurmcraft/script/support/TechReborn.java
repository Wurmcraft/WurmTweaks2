package com.wurmcraft.script.support;


import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.exception.InvalidStackException;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import com.wurmcraft.script.utils.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.RollingMachineRecipe;
import techreborn.api.ScrapboxList;
import techreborn.api.TechRebornAPI;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.BaseRecipe;
import techreborn.api.recipe.machines.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TechReborn extends SupportHelper {

 private List<Object[]> shapeless = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> shaped = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> scrap = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> fluidGenerator = Collections.synchronizedList(new ArrayList<>());
 private List<FusionReactorRecipe> fusion = Collections.synchronizedList(new ArrayList<>());
 private List<BaseRecipe> machine = Collections.synchronizedList(new ArrayList<>());

 public TechReborn() {
  super("techreborn");
 }

 @Override
 public void init() {
  shapeless.clear();
  shaped.clear();
  scrap.clear();
  fluidGenerator.clear();
  fusion.clear();
  machine.clear();
  if (ConfigHandler.removeAllMachineRecipes) {
   RollingMachineRecipe.instance.getRecipeList().clear();
   ScrapboxList.stacks.clear();
   FusionReactorRecipeHelper.reactorRecipes.clear();
   RecipeHandler.recipeList.clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] r : shapeless)
   TechRebornAPI.addShapelessOreRollingMachinceRecipe((ResourceLocation) r[0], (ItemStack) r[1], (Ingredient[]) r[2]);
  for (Object[] r : shaped)
   TechRebornAPI.addRollingOreMachinceRecipe((ResourceLocation) r[0], (ItemStack) r[1], (Object[]) r[2]);
  for (Object[] r : scrap)
   ScrapboxList.addItemStackToList((ItemStack) r[0]);
  for (Object[] r : fluidGenerator)
   GeneratorRecipeHelper.registerFluidRecipe((EFluidGenerator) r[0], (Fluid) r[1], (int) r[2]);
  for (FusionReactorRecipe r : fusion)
   FusionReactorRecipeHelper.registerRecipe(r);
  for (BaseRecipe recipe : machine)
   RecipeHandler.addRecipe(recipe);
 }

 @ScriptFunction
 public void addShapelessRolling(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2 && line.split(" ").length <= 10, "addShapelessRolling('<output> <input>...')");
  isValid(helper, input[0]);
  List<Object> validItems = new ArrayList<>();
  boolean validRecipe = true;
  for (String item : Arrays.copyOfRange(input, 1, input.length))
   if (helper.convert(item) != null)
    validItems.add(convertIngredient(helper, item));
   else {
    validRecipe = false;
    throw new InvalidStackException("Invalid Item '" + item + "'");
   }
  if (validRecipe) {
   ItemStack output = convertStack(helper, input[0]);
   Preconditions.checkArgument(!validItems.isEmpty(), "Invalid Inputs!");
   shapeless.add(new Object[]{new ResourceLocation(Global.MODID, output.getUnlocalizedName().substring(5) + validItems.hashCode()), output, validItems.toArray(new Ingredient[0])});
  }
 }

 @ScriptFunction
 public void addShapedRolling(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length > 4, "addShapedRolling(<output> <style> <format>')");
  isValid(helper, input[0]);
  ItemStack output = convertStack(helper, input[0]);
  List<Object> recipe = RecipeUtils.getShapedRecipe(helper, input);
  Preconditions.checkNotNull(recipe);
  shaped.add(new Object[]{new ResourceLocation(Global.MODID, output.getUnlocalizedName().substring(5) + recipe.hashCode()), output, recipe.toArray(new Object[0])});
 }

 @ScriptFunction
 public void addScrapbox(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 1, "addScrapbox('<stack>')");
  isValid(helper, input[0]);
  scrap.add(new Object[]{convertStack(helper, input[0])});
 }

 @ScriptFunction
 public void addGeneratorFluid(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addGeneratorFluid('<type> <*fluid> <energy>')");
  Preconditions.checkNotNull(getGeneratorType(input[0]));
  isValid(Types.FLUIDSTACK, helper, input[1]);
  isValid(Types.INTEGER, helper, input[2]);
  fluidGenerator.add(new Object[]{getGeneratorType(input[0]), convertFluidStack(helper, input[1]).getFluid(), convertInteger(input[2])});
 }

 private EFluidGenerator getGeneratorType(String name) {
  return EFluidGenerator.valueOf(name.toUpperCase());
 }

 @ScriptFunction
 public void addTechFusion(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 6, "addTechFusion('<output> <topInput> <bottomInput> <startEU> <euTick> <time>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3], input[4], input[5]);
  fusion.add(new FusionReactorRecipe(convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[0]), convertInteger(input[3]), convertInteger(input[4]), convertInteger(input[5])));
 }

 @ScriptFunction
 public void addAlloySmelter(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 5, "addAlloySmelter('<output> <input> <input2> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3], input[4]);
  machine.add(new AlloySmelterRecipe(convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[0]), convertInteger(input[3]), convertInteger(input[4])));
 }

 @ScriptFunction
 public void addAssemblingMachine(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 5, "addAssemblingMachine('<output> <input> <input2> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3], input[4]);
  machine.add(new AssemblingMachineRecipe(convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[0]), convertInteger(input[3]), convertInteger(input[4])));
 }

 @ScriptFunction
 public void addIndustrialBlastFurnace(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addIndustrialBlastFurnace('<output> <output2> <input> <input2> <time> <euTick> <heat>')");
  isValid(helper, input[0], input[1], input[2], input[3]);
  isValid(Types.INTEGER, helper, input[4], input[5], input[6]);
  machine.add(new BlastFurnaceRecipe(convertStack(helper, input[2]), convertStack(helper, input[3]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[4]), convertInteger(input[5]), convertInteger(input[6])));
 }

 @ScriptFunction
 public void addTRCenterfuge(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 8, "addCenterfuge('<output> <output2> <output3> <input> <input2> <time> <euTick> <output4>')");
  isValid(helper, input[0], input[1], input[2], input[3], input[4], input[7]);
  isValid(Types.INTEGER, helper, input[5], input[6]);
  machine.add(new CentrifugeRecipe(convertStack(helper, input[4]), convertStack(helper, input[7]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[3]), convertInteger(input[5]), convertInteger(input[6])));
 }

 @ScriptFunction
 public void addChemicalReactor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 5, "addChemicalReactor('<output> <input> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2]);
  isValid(Types.INTEGER, helper, input[3], input[4]);
  machine.add(new ChemicalReactorRecipe(convertStack(helper, input[2]), convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[3]), convertInteger(input[4])));
 }

 @ScriptFunction
 public void addCompressor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addGrinder('<output> <input> <time> <euTick>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2], input[3]);
  machine.add(new CompressorRecipe(convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2]), convertInteger(input[3])));
 }
 //TODO out of bounds ln187
 @ScriptFunction
 public void addDistillationTower(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addDistillationTower('<output> <output2> <output3> <input> <input2> <time> <euTick>'");
  isValid(helper, input[0], input[1], input[2], input[3], input[4], input[5]);
  isValid(Types.INTEGER, helper, input[6], input[7]);
  machine.add(new DistillationTowerRecipe(convertStack(helper, input[4]), convertStack(helper, input[5]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[3]), convertInteger(input[6]), convertInteger(input[7])));
 }

 @ScriptFunction
 public void addExtractor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addExtractor('<output> <input> <time> <euTick>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2], input[3]);
  machine.add(new ExtractorRecipe(convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2]), convertInteger(input[3])));
 }

 @ScriptFunction
 public void addGrinder(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addGrinder('<output> <input> <time> <euTick>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2], input[3]);
  machine.add(new GrinderRecipe(convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2]), convertInteger(input[3]) * 10));
 }

 @ScriptFunction
 public void addImplosionCompressor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 6, "addImplosionCompressor('<output> <output2> <input> <input2> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2], input[3]);
  isValid(Types.INTEGER, helper, input[4], input[5]);
  machine.add(new ImplosionCompressorRecipe(convertStack(helper, input[2]), convertStack(helper, input[3]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[4]), convertInteger(input[5])));
 }

 @ScriptFunction
 public void addIndustrialElectrolyzer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addIndustrialElectrolyzer('<output> <output2> <output3> <input> <input2> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2], input[3], input[4], input[5]);
  isValid(Types.INTEGER, helper, input[6], input[7]);
  machine.add(new IndustrialElectrolyzerRecipe(convertStack(helper, input[4]), convertStack(helper, input[5]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[3]), convertInteger(input[6]), convertInteger(input[7])));
 }

 @ScriptFunction
 public void addIndustrialGrinder(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addIndustrialGrinder('<output> <output2> <output3> <input> <input2> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2], input[3], input[4]);
  isValid(Types.INTEGER, helper, input[6], input[7]);
  isValid(Types.FLUIDSTACK, helper, input[5]);
  machine.add(new IndustrialGrinderRecipe(convertStack(helper, input[4]), convertFluidStack(helper, input[5]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertStack(helper, input[3]), convertInteger(input[6]), convertInteger(input[7])));
 }

 @ScriptFunction
 public void addIndustrialSawmill(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 7, "addIndustrialSawmill('<output> <output2> <output3> <input> <input2> <time> <euTick>')");
  isValid(helper, input[0], input[1], input[2], input[3]);
  isValid(Types.INTEGER, helper, input[5], input[6]);
  isValid(Types.FLUIDSTACK, helper, input[4]);
  machine.add(new IndustrialSawmillRecipe(convertStack(helper, input[3]), convertFluidStack(helper, input[4]), convertStack(helper, input[0]), convertStack(helper, input[1]), convertStack(helper, input[2]), convertInteger(input[5]), convertInteger(input[6])));
 }

 @ScriptFunction
 public void addVacuumFreezer(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 4, "addVacuumFreezer('<output> <input> <time> <euTick')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2], input[3]);
  machine.add(new VacuumFreezerRecipe(convertStack(helper, input[1]), convertStack(helper, input[0]), convertInteger(input[2]), convertInteger(input[3]) * 10));
 }
}
