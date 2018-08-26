package com.wurmcraft.common.support;

import charcoalPit.crafting.OreSmeltingRecipes;
import charcoalPit.crafting.OreSmeltingRecipes.AlloyRecipe;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "charcoal_pit")
public class CharcoalPit {

  private NonBlockingHashSet<OreSmeltingRecipes.AlloyRecipe> bloomery;


  @InitSupport
  public void init() {
    if (bloomery == null) {
      bloomery = new NonBlockingHashSet<AlloyRecipe>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      OreSmeltingRecipes.alloyRecipes.clear();
    } else if (ScriptExecutor.reload) {
      bloomery.forEach(alloy -> OreSmeltingRecipes.alloyRecipes.remove(alloy));
      bloomery.clear();
    }
  }

  @ScriptFunction(modid = "charcoal_pit", inputFormat = "ItemStack ItemStack")
  public void addBloomery(Converter converter, String[] line) {
    bloomery.add(new OreSmeltingRecipes.AlloyRecipe(converter.convert(line[0], 1),
        ((ItemStack) converter.convert(line[0], 1)).getCount(), true, true,
        (ItemStack) converter.convert((line[1]))));
  }

  @FinalizeSupport
  public void finishSupport() {
    bloomery.forEach(recipe -> OreSmeltingRecipes.addAlloyRecipe(recipe));
  }

}
