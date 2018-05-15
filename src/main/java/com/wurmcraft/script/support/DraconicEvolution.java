package com.wurmcraft.script.support;

import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.brandon3055.draconicevolution.lib.RecipeManager;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DraconicEvolution extends SupportHelper {

 private List<SimpleFusionRecipe> fusion = Collections.synchronizedList(new ArrayList<>());

 public DraconicEvolution() {
  super("draconicevolution");
 }

 @Override
 public void init() {
  fusion.clear();
  if (ConfigHandler.removeAllMachineRecipes)
   RecipeManager.FUSION_REGISTRY.getRecipes().clear();
 }

 @ScriptFunction
 public void addFusion(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 5, "addFusion('<output> <catalyst> <tier> <energy> <input>...')");
  isValid(helper, input[0], input[1], input[4]);
  isValid(Types.INTEGER, helper, input[2], input[3]);
  List<ItemStack> inputs = new ArrayList<>();
  for (int index = 4; index < input.length; index++) {
   isValid(helper, input[index]);
   inputs.add(convertStack(helper, input[index]));
  }
  fusion.add(new SimpleFusionRecipe(convertStack(helper, input[0]), convertStack(helper, input[1]), convertInteger(input[3]), convertInteger(input[2]), inputs.toArray(new ItemStack[0])));
 }

 @Override
 public void finishSupport() {
  for (SimpleFusionRecipe recipe : fusion)
   FusionRecipeAPI.addRecipe(recipe);
 }
}
