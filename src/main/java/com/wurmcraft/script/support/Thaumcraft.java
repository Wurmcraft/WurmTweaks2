package com.wurmcraft.script.support;


import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.StackSettings;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.internal.WeightedRandomLoot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Thaumcraft extends SupportHelper {

 private List<Object[]> lootBag = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> crucible = Collections.synchronizedList(new ArrayList<>());
 private List<InfusionRecipe> infusion = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> shapedArcane = Collections.synchronizedList(new ArrayList<>());
 private List<Object[]> shapelessArcane = Collections.synchronizedList(new ArrayList<>());

 public Thaumcraft() {
  super("thaumcraft");
 }

 private static AspectList getAspectList(String[] aspect) {
  AspectList list = new AspectList();
  for (String spect : aspect)
   if (spect.startsWith(StackSettings.FRONT.getFormatting() + StackSettings.ASPECT.getFormatting())) {
    Aspect aspe = getAspect(spect.substring(spect.indexOf(StackSettings.FRONT.getFormatting().charAt(0)) + 1, spect.indexOf(StackSettings.BACK.getFormatting().charAt(0))));
    int amount = Integer.parseInt(spect.substring(spect.indexOf(StackSettings.FRONT.getFormatting() + StackSettings.ASPECT.getFormatting()) + 2, spect.indexOf(StackSettings.STACK_SIZE.getFormatting())));
    list.add(aspe, amount);
   }
  return list;
 }

 private static Aspect getAspect(String aspect) {
  return Aspect.getAspect(aspect);
 }

 @Override
 public void init() {
  lootBag.clear();
  crucible.clear();
  infusion.clear();
  shapedArcane.clear();
  shapelessArcane.clear();
  if (ConfigHandler.Script.removeAllMachineRecipes) {
   WeightedRandomLoot.lootBagCommon.clear();
   WeightedRandomLoot.lootBagUncommon.clear();
   WeightedRandomLoot.lootBagRare.clear();
  }
 }

 @Override
 public void finishSupport() {
  for (Object[] recipe : lootBag)
   ThaumcraftApi.addLootBagItem((ItemStack) recipe[0], (int) recipe[1]);
  for (Object[] recipe : crucible)
   ThaumcraftApi.addCrucibleRecipe((ResourceLocation) recipe[0], (CrucibleRecipe) recipe[1]);
  for (InfusionRecipe recipe : infusion)
   ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Global.MODID, recipe.getRecipeOutput().hashCode() + "_" + recipe.research), recipe);
 }

 @ScriptFunction
 public void addLootBagItem(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addLootBagItem('<output> <bag>')");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1]);
  ThaumcraftApi.addLootBagItem(convertStack(helper, input[0]), convertInteger(input[1]));
 }

 @ScriptFunction
 public void addCrucible(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 4, "addCrucible('<output> <researchKey> <input> <aspects>...')");
  isValid(helper, input[0], input[2]);
  AspectList list = getAspectList(Arrays.copyOfRange(input, 3, input.length));
  ItemStack output = helper.convert(input[0]);
  ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Global.MODID, "thaumcraft_" + output.getUnlocalizedName() + output.getItemDamage()), new CrucibleRecipe(input[1], output, convertStack(helper, input[2]), list));
 }

 @ScriptFunction
 public void addInfusion(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 7, "addInfusion(<output> <research> <centerStack> <instability> <items>... <aspects...'");
  isValid(helper, input[0], input[2]);
  isValid(Types.INTEGER, helper, input[3]);
  List<ItemStack> items = new ArrayList<>();
  for (int index = 4; index < input.length; index++)
   if (!input[index].startsWith(StackSettings.FRONT.getFormatting() + StackSettings.ASPECT.getFormatting())) {
    isValid(helper, input[index]);
    items.add(convertStack(helper, input[index]));

   } else
    break;
  List<String> aspectList = new ArrayList<>();
  for (int index = 5; index < input.length - 1; index++)
   if (input[index].startsWith(StackSettings.FRONT.getFormatting() + StackSettings.ASPECT.getFormatting()))
    aspectList.add(input[index]);
  InfusionRecipe recipe = new InfusionRecipe(input[1], convertStack(helper, input[0]), convertInteger(input[3]), getAspectList(aspectList.toArray(new String[0])), convertStack(helper, input[2]), items.toArray(new ItemStack[0]));
  infusion.add(recipe);
 }
}
