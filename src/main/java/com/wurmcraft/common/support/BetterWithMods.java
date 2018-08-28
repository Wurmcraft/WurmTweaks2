package com.wurmcraft.common.support;

import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.HopperInteractions.HopperRecipe;
import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.anvil.ShapelessAnvilRecipe;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.script.utils.IngredientWrapper;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "betterwithmods")
public class BetterWithMods {

  private static NonBlockingHashSet<HopperRecipe> scriptHopper;
  private static NonBlockingHashSet<ShapedAnvilRecipe> scriptShapedAnvil;
  private static NonBlockingHashSet<ShapelessAnvilRecipe> scriptShapelessAnvil;
  private static NonBlockingHashSet<Heat> scriptHeat;

  @InitSupport
  public void init() {
    scriptHopper = new NonBlockingHashSet<>();
    scriptShapedAnvil = new NonBlockingHashSet<>();
    scriptShapelessAnvil = new NonBlockingHashSet<>();
    scriptHeat = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllRecipes) {
      HopperInteractions.RECIPES.clear();
//      for (HopperRecipe recipe : HopperInteractions.RECIPES) {
//        for (ItemStack itemStack : recipe.getInputs().getMatchingStacks()) {
//          HopperInteractions.removeByInput(itemStack);
//        }
//      }
      AnvilCraftingManager.ANVIL_CRAFTING.clear();


    } else if (ScriptExecutor.reload) {
      scriptHopper.forEach(
          recipe -> HopperInteractions.remove(recipe.getOutputs(), recipe.getSecondaryOutputs()));
      for (ShapedAnvilRecipe recipe : scriptShapedAnvil) {
        AnvilCraftingManager.ANVIL_CRAFTING.remove(recipe);
      }
      scriptShapedAnvil.clear();
      for (ShapelessAnvilRecipe recipe : scriptShapelessAnvil) {
        AnvilCraftingManager.ANVIL_CRAFTING.remove(recipe);
      }
      scriptShapelessAnvil.clear();
      // TODO Remove Block Heat on Reload
    }
  }

  @FinalizeSupport
  public void finalize() {
    scriptHopper.forEach(HopperInteractions::addHopperRecipe);
    AnvilCraftingManager.ANVIL_CRAFTING.addAll(scriptShapedAnvil);
    AnvilCraftingManager.ANVIL_CRAFTING.addAll(scriptShapelessAnvil);
    scriptHeat.forEach(heat -> BWMHeatRegistry.addHeatSource(heat.block, heat.heat));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "String ItemStack ItemStack ItemStack ...")
  public void addHopperFilter(Converter converter, String[] line) {
    scriptHopper.add(
        new HopperRecipe(line[0], new IngredientWrapper((ItemStack) converter.convert(line[1], 1)),
            (ItemStack) converter.convert(line[2]),
            converter.getBulkItemsAsList(Arrays.copyOfRange(line, 3, line.length))
                .toArray(new ItemStack[0])));
  }


  @ScriptFunction(modid = "betterwithmods")
  public void addShapedAnvil(Converter converter, String[] line) {
    scriptShapedAnvil.add(new ShapedAnvilRecipe(
        new ResourceLocation(Global.MODID, converter.convert(line[0]).toString()),
        (ItemStack) converter.convert(line[0]),
        RecipeUtils.getShapedRecipe(line).toArray(new Object[0])));
  }


  @ScriptFunction(modid = "betterwithmods")
  public void addShapelessAnvil(Converter converter, String[] line) {
    scriptShapelessAnvil.add(
        new ShapelessAnvilRecipe(new ResourceLocation(converter.convert(line[0]).toString()),
            (ItemStack) converter.convert(line[0]),
            RecipeUtils.getShapelessItems(Arrays.copyOfRange(line, 1, line.length), converter)));
  }


  @ScriptFunction(modid = "betterwithmods")
  public void addBWMHeat(Converter converter, String[] line) {
    scriptHeat.add(new Heat(new BlockIngredient(
        converter.getBulkItemsAsList(Arrays.copyOfRange(line, 1, line.length))
            .toArray(new ItemStack[0])), Integer.parseInt(line[0])));
  }

  public class Heat {

    public BlockIngredient block;
    private int heat;

    public Heat(BlockIngredient block, int heat) {
      this.block = block;
      this.heat = heat;
    }
  }
}
