package com.wurmcraft.common.support;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import joptsimple.internal.Strings;
import mods.railcraft.api.crafting.IRockCrusherCrafter.IRockCrusherRecipeBuilder;
import mods.railcraft.api.crafting.IRollingMachineCrafter.IRollingMachineRecipeBuilder;
import mods.railcraft.common.util.crafting.RockCrusherCrafter;
import mods.railcraft.common.util.crafting.RollingMachineCrafter;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "railcraft")
public class Railcraft {

  public static NonBlockingHashSet<IRockCrusherRecipeBuilder> rockCrusher;
  public static NonBlockingHashSet<IRollingMachineRecipeBuilder> rollingMachine;

  @InitSupport(modid = "railcraft")
  public void init() {
    rockCrusher = new NonBlockingHashSet<>();
    rollingMachine = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      RockCrusherCrafter.INSTANCE.getRecipes().clear();
      RollingMachineCrafter.INSTANCE.getRecipes().clear();
    }
    rockCrusher.clear();
    rollingMachine.clear();
  }

  @InitSupport(modid = "railcraft")
  public void finalizeSupport() {
    if (rockCrusher != null) {
      for (IRockCrusherRecipeBuilder recipe : rockCrusher) {
        recipe.register();
      }
    }
  }

  @ScriptFunction(modid = "railcraft", inputFormat = "ItemStack ItemStack")
  public void addRCRockCrusher(Converter converter, String[] line) {
    rockCrusher.add(
        RockCrusherCrafter.INSTANCE
            .makeRecipe(converter.convert(line[1]))
            .addOutput((ItemStack) converter.convert(line[0])));
  }

  @ScriptFunction(modid = "minecraft", inputFormat = "ItemStack ***")
  public void addShapedRCRolling(Converter converter, String[] line) {
    if (line.length >= 2) {
      IRollingMachineRecipeBuilder recipe =
          RollingMachineCrafter.INSTANCE.newRecipe((ItemStack) converter.convert(line[0]));
      recipe.recipe(
          RecipeUtils.createShapedRecipe(
              (ItemStack) converter.convert(line[0], 1),
              RecipeUtils.getShapedRecipe(line).toArray()));
      rollingMachine.add(recipe);
    } else {
      WurmTweaks.logger.error("Invalid Shaped Format '" + Strings.join(line, " ") + "'");
    }
  }

  @ScriptFunction(modid = "minecraft", inputFormat = "ItemStack ItemStack/OreDictionary ...")
  public void addShapelessRCRolling(Converter converter, String[] line) {
    Object[] shapelessInputs =
        RecipeUtils.getShapelessItems(Arrays.copyOfRange(line, 1, line.length), converter);
    IRollingMachineRecipeBuilder recipe =
        RollingMachineCrafter.INSTANCE.newRecipe((ItemStack) converter.convert(line[0]));
    recipe.recipe(
        RecipeUtils.createShapelessRecipe(
            (ItemStack) converter.convert(line[0], 1), shapelessInputs));
    rollingMachine.add(recipe);
  }
}
