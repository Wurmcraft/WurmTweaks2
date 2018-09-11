package com.wurmcraft.common.support;

import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.brandon3055.draconicevolution.lib.RecipeManager;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "draconicevolution")
public class DraconicEvolution {

  private static NonBlockingHashSet<SimpleFusionRecipe> fusion;

  @InitSupport(modid = "draconicevolution")
  public void init() {
    fusion = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      RecipeManager.FUSION_REGISTRY.getRecipes().clear();
    } else if (ScriptExecutor.reload) {
      for (SimpleFusionRecipe recipe : fusion) {
        FusionRecipeAPI.removeRecipe(recipe);
      }
      fusion.clear();
    }
  }

  @FinalizeSupport(modid = "draconicevolution")
  public void finalize() {
    for (SimpleFusionRecipe recipe : fusion) {
      FusionRecipeAPI.addRecipe(recipe);
    }
  }

  @ScriptFunction(
    modid = "draconicevolution",
    inputFormat = "ItemStack ItemStack Integer Integer ItemStack ..."
  )
  public void addDEFusion(Converter converter, String[] line) {
    fusion.add(
        new SimpleFusionRecipe(
            (ItemStack) converter.convert(line[0]),
            (ItemStack) converter.convert(line[1]),
            Integer.parseInt(line[3]),
            Integer.parseInt(line[2]),
            converter.getBulkItemsAsList(Arrays.copyOfRange(line, 3, line.length))));
  }
}
