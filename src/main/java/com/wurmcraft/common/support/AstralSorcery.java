package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "astralsorcery")
public class AstralSorcery {

  private static NonBlockingHashSet<Object[]> basic;
  private static NonBlockingHashSet<Object[]> slow;

  @Method(modid = "appliedenergistics")
  @InitSupport
  public void init() {
    basic = new NonBlockingHashSet<>();
    slow = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllRecipes) {
      InfusionRecipeRegistry.recipes.clear();
      InfusionRecipeRegistry.mtRecipes.clear();
    } else if (ScriptExecutor.reload) {
      // TODO Reload Recipes
    }
  }

  @Method(modid = "appliedenergistics")
  @ScriptFunction(modid = "astralsorcery", inputFormat = "ItemStack ItemStack")
  public void addBasicInfusion(Converter converter, String[] line) {
    basic.add(new Object[]{converter.convert(line[0]), converter.convert(line[1])});
  }

  @Method(modid = "appliedenergistics")
  @ScriptFunction(modid = "astralsorcery", inputFormat = "ItemStack ItemStack")
  public void addSlowInfusion(Converter converter, String[] line) {
    slow.add(new Object[]{converter.convert(line[0]), converter.convert(line[1])});
  }

  @Method(modid = "appliedenergistics")
  @FinalizeSupport
  public void finishSupport() {
    basic.forEach(
        recipe ->
            InfusionRecipeRegistry.registerBasicInfusion(
                (ItemStack) recipe[0], (ItemStack) recipe[1]));
    slow.forEach(
        recipe ->
            InfusionRecipeRegistry.registerLowConsumptionInfusion(
                (ItemStack) recipe[0], (ItemStack) recipe[1]));
  }
}
