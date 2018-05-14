package com.wurmcraft.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.Cast;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.tinkering.MaterialItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO Add Material Support
public class TConstruct extends SupportHelper {

 // TODO Config
 private static int INGOT = 1000;
 private static int BLOCK = INGOT * 32;

 private List<Object[]> casting = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> basin = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> alloy = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> drying = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> fuel = Collections.synchronizedList(new ArrayList<>());
 private List<MeltingRecipe> melting = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> entityMelting = Collections.synchronizedList(new ArrayList<>());

 public TConstruct() {
  super("tconstruct");
 }

 @Override
 public void init() {
  casting.clear();
  basin.clear();
  alloy.clear();
  drying.clear();
  fuel.clear();
  melting.clear();
  entityMelting.clear();
  if (ConfigHandler.Script.removeAllMachineRecipes) {
   try {
    Field tableCasting = TinkerRegistry.class.getDeclaredField("tableCastRegistry");
    tableCasting.setAccessible(true);
    ((List<ICastingRecipe>) tableCasting.get(TinkerRegistry.getAllTableCastingRecipes())).clear();
    Field melting = TinkerRegistry.class.getDeclaredField("meltingRegistry");
    melting.setAccessible(true);
    ((List<MeltingRecipe>) melting.get(TinkerRegistry.getAllMeltingRecipies())).clear();
    Field basinCast = TinkerRegistry.class.getDeclaredField("basinCastRegistry");
    basinCast.setAccessible(true);
    ((List<ICastingRecipe>) basinCast.get(TinkerRegistry.getAllBasinCastingRecipes())).clear();
    Field alloy = TinkerRegistry.class.getDeclaredField("alloyRegistry");
    alloy.setAccessible(true);
    ((List<AlloyRecipe>) alloy.get(TinkerRegistry.getAlloys())).clear();
    Field drying = TinkerRegistry.class.getDeclaredField("dryingRegistry");
    drying.setAccessible(true);
    ((List<DryingRecipe>) drying.get(TinkerRegistry.getAllDryingRecipes())).clear();
   } catch (IllegalAccessException | NoSuchFieldException e) {
    e.printStackTrace();
   }
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : casting)
   TinkerRegistry.registerTableCasting((ItemStack) recipe[0], (ItemStack) recipe[1], (Fluid) recipe[2], (int) recipe[3]);
  for (Object[] recipe : basin)
   TinkerRegistry.registerBasinCasting((ItemStack) recipe[0], (ItemStack) recipe[1], (Fluid) recipe[2], (int) recipe[3]);
  for (Object[] recipe : alloy)
   TinkerRegistry.registerAlloy((FluidStack) recipe[0], (FluidStack[]) recipe[1]);
  for (Object[] recipe : drying)
   TinkerRegistry.registerDryingRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
  for (Object[] recipe : fuel)
   TinkerRegistry.registerSmelteryFuel((FluidStack) recipe[0], (int) recipe[1]);

  melting.forEach(recipe -> TinkerRegistry.registerMelting(recipe));

  for (Object[] recipe : entityMelting)
   TinkerRegistry.registerEntityMelting((Class<? extends Entity>) recipe[0], (FluidStack) recipe[1]);
 }

 @ScriptFunction
 public void addCasting(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addCasting('<output> <cast> <*fluid>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.FLUIDSTACK, helper, input[2]);
  FluidStack fluidStack = convertFluidStack(helper, input[2]);
  casting.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), fluidStack.getFluid(), fluidStack.amount});
 }

 @ScriptFunction
 public void addBasin(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addBasin('<output> <cast> <*fluid>')");
  isValid(helper, input[0]);
  isValid(Types.FLUIDSTACK, helper, input[2]);
  FluidStack fluidStack = convertFluidStack(helper, input[2]);
  basin.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), fluidStack.getFluid(), fluidStack.amount});
 }

 @ScriptFunction
 public void addAlloy(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addAlloy('<*output> <*input>...')");
  isValid(Types.FLUIDSTACK, helper, input[0]);
  List<FluidStack> inputFluids = new ArrayList<>();
  for (int index = 1; index < input.length; index++) {
   isValid(Types.FLUIDSTACK, helper, input[index]);
   inputFluids.add(convertFluidStack(helper, input[index]));
  }
  alloy.add(new Object[]{convertFluidStack(helper, input[0]), inputFluids.toArray(new FluidStack[0])});
 }

 @ScriptFunction
 public void addDrying(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addDrying('<output> <input> <time>')");
  isValid(helper, input[0], input[1]);
  isValid(Types.INTEGER, helper, input[2]);
  drying.add(new Object[]{convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[2])});
 }

 @ScriptFunction
 public void addFuel(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addFuel(<*fluid> <time>')");
  isValid(Types.FLUIDSTACK, helper, input[0]);
  isValid(Types.INTEGER, helper, input[1]);
  fuel.add(new Object[]{convertFluidStack(helper, input[0]), convertInteger(input[1])});
 }

 @ScriptFunction
 public void addMelting(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addMelting('<*output> <input> <temp>')");
  isValid(Types.FLUIDSTACK, helper, input[0]);
  isValid(helper, input[1]);
  isValid(Types.INTEGER, helper, input[2]);
  FluidStack fluidStack = convertFluidStack(helper, input[0]);
  melting.add(new MeltingRecipe(RecipeMatch.of(convertStack(helper, input[1]), fluidStack.amount), fluidStack, convertInteger(input[2])));
 }

 @ScriptFunction
 public void addEntityMelting(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addEntityMelting('<*output> entityName')");
  EntityEntry entity = null;
  for (EntityEntry ent : ForgeRegistries.ENTITIES.getValues())
   if (ent.getName().equalsIgnoreCase(input[0]))
    entity = ent;
  Preconditions.checkNotNull(entity);
  entityMelting.add(new Object[]{entity.getEntityClass(), convertFluidStack(helper, input[0])});
 }
 // End Of Normal Mod Support

 @ScriptFunction
 public void handleMelting(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "handleMelting('<*fluid> <ingot> <block>')");
  isValid(Types.FLUIDSTACK, helper, input[0]);
  isValid(helper, input[1], input[2]);
  Fluid fluid = convertFluidStack(helper, input[0]).getFluid();
  if (convertStack(helper, input[2]) != ItemStack.EMPTY) {
   melting.add(new MeltingRecipe(RecipeMatch.of(convertStack(helper, input[2])), fluid, BLOCK));
   basin.add(new Object[]{convertStack(helper, input[2]), null, fluid, BLOCK});
  }
  if (convertStack(helper, input[1]) != ItemStack.EMPTY) {
   melting.add(new MeltingRecipe(RecipeMatch.of(convertStack(helper, input[1])), fluid, INGOT));
   casting.add(new Object[]{convertStack(helper, input[1]), convertStack(helper, "<1xtconstruct:cast_custom@0>"), fluid, INGOT});
  }
 }

 @ScriptFunction
 public void handleMaterialParts(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "handleMaterialParts('<materialName> <*fluid>')");
  isValid(Types.FLUIDSTACK, helper, input[1]);
  Fluid fluid = convertFluidStack(helper, input[1]).getFluid();
  Material material = null;
  for (Material mat : TinkerMaterials.materials)
   if (mat.getIdentifier().equalsIgnoreCase(input[0]))
    material = mat;
  for (IToolPart toolPart : TinkerRegistry.getToolParts())
   if (toolPart instanceof MaterialItem && toolPart.canBeCasted()) {
    ItemStack stack = toolPart.getItemstackWithMaterial(material);
    melting.add(new MeltingRecipe(RecipeMatch.of(stack), fluid, normalize(toolPart.getCost())));
    ItemStack cast = new ItemStack(TinkerSmeltery.cast);
    Cast.setTagForPart(cast, stack.getItem());
    casting.add(new Object[]{stack, cast, fluid, (normalize(toolPart.getCost()))});
   }
 }

 private int normalize(int amount) {
  if (amount == Material.VALUE_Ingot)
   return INGOT;
  else if (amount == Material.VALUE_Block)
   return BLOCK;
  else if (amount == Material.VALUE_Nugget)
   return INGOT / 9;
  else if (amount == Material.VALUE_Shard)
   return INGOT / 2;
  return INGOT * ((amount / Material.VALUE_Ingot));
 }
}
