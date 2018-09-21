package com.wurmcraft.common.support;

import betterwithmods.common.BWRegistry;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.HopperInteractions.HopperRecipe;
import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.anvil.ShapelessAnvilRecipe;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import com.google.common.collect.Lists;
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
  private static NonBlockingHashSet<CookingPotRecipe> scriptCauldron;
  private static NonBlockingHashSet<CookingPotRecipe> scriptStokedCauldron;
  private static NonBlockingHashSet<MillRecipe> scriptMill;
  private static NonBlockingHashSet<CookingPotRecipe> scriptCrucible;
  private static NonBlockingHashSet<CookingPotRecipe> scriptStokedCrucible;

  @InitSupport(modid = "betterwithmods")
  public void init() {
    scriptHopper = new NonBlockingHashSet<>();
    scriptShapedAnvil = new NonBlockingHashSet<>();
    scriptShapelessAnvil = new NonBlockingHashSet<>();
    scriptHeat = new NonBlockingHashSet<>();
    scriptCauldron = new NonBlockingHashSet<>();
    scriptStokedCauldron = new NonBlockingHashSet<>();
    scriptMill = new NonBlockingHashSet<>();
    scriptCrucible = new NonBlockingHashSet<>();
    scriptStokedCrucible = new NonBlockingHashSet<>();
  }

  @FinalizeSupport(modid = "betterwithmods")
  public void finalize() {
    // Yea this is a thing thx BWM
    if (ConfigHandler.removeAllRecipes) {
      HopperInteractions.RECIPES.clear();
      AnvilCraftingManager.ANVIL_CRAFTING.clear();
      BWRegistry.CAULDRON.getRecipes().clear();
      BWRegistry.CRUCIBLE.getRecipes().clear();
      // TODO Remove All Recipes
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
      BWRegistry.CAULDRON.getRecipes().clear();
      BWRegistry.CAULDRON.getRecipes().clear();
      BWRegistry.MILLSTONE.getRecipes().clear();
      for (CookingPotRecipe recipe : scriptCrucible) {
        BWRegistry.CRUCIBLE.remove(recipe);
      }
      for (CookingPotRecipe recipe : scriptStokedCrucible) {
        BWRegistry.CRUCIBLE.remove(recipe);
      }
    }
    for (HopperRecipe hopperRecipe : scriptHopper) {
      HopperInteractions.addHopperRecipe(hopperRecipe);
    }
    AnvilCraftingManager.ANVIL_CRAFTING.addAll(scriptShapedAnvil);
    AnvilCraftingManager.ANVIL_CRAFTING.addAll(scriptShapelessAnvil);
    for (Heat heat : scriptHeat) {
      BWMHeatRegistry.addHeatSource(heat.block, heat.heat);
    }
    for (CookingPotRecipe cookingPotRecipe : scriptCauldron) {
      BWRegistry.CAULDRON.addUnstokedRecipe(
          cookingPotRecipe.getInputs(), cookingPotRecipe.getOutputs());
    }
    for (CookingPotRecipe cookingPotRecipe : scriptStokedCauldron) {
      BWRegistry.CAULDRON.addStokedRecipe(
          cookingPotRecipe.getInputs(), cookingPotRecipe.getOutputs());
    }
    for (MillRecipe millRecipe : scriptMill) {
      BWRegistry.MILLSTONE.addRecipe(millRecipe);
    }
    for (CookingPotRecipe cookingPotRecipe : scriptCrucible) {
      BWRegistry.CRUCIBLE.addUnstokedRecipe(
          cookingPotRecipe.getInputs(), cookingPotRecipe.getOutputs());
    }
    for (CookingPotRecipe recipe : scriptStokedCrucible) {
      BWRegistry.CRUCIBLE.addStokedRecipe(recipe.getInputs(), recipe.getOutputs());
    }
  }

  @ScriptFunction(
    modid = "betterwithmods",
    inputFormat = "String ItemStack ItemStack ItemStack ..."
  )
  public void addHopperFilter(Converter converter, String[] line) {
    scriptHopper.add(
        new HopperRecipe(
            line[0],
            new IngredientWrapper((ItemStack) converter.convert(line[1], 1)),
            (ItemStack) converter.convert(line[2]),
            converter
                .getBulkItemsAsList(Arrays.copyOfRange(line, 3, line.length))
                .toArray(new ItemStack[0])));
  }

  @ScriptFunction(modid = "betterwithmods")
  public void addShapedAnvil(Converter converter, String[] line) {
    scriptShapedAnvil.add(
        new ShapedAnvilRecipe(
            new ResourceLocation(Global.MODID, converter.convert(line[0]).toString()),
            (ItemStack) converter.convert(line[0]),
            RecipeUtils.getShapedRecipe(line).toArray(new Object[0])));
  }

  @ScriptFunction(modid = "betterwithmods")
  public void addShapelessAnvil(Converter converter, String[] line) {
    scriptShapelessAnvil.add(
        new ShapelessAnvilRecipe(
            new ResourceLocation(converter.convert(line[0]).toString()),
            (ItemStack) converter.convert(line[0]),
            RecipeUtils.getShapelessItems(Arrays.copyOfRange(line, 1, line.length), converter)));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "Integer Block")
  public void addBWMHeat(Converter converter, String[] line) {
    scriptHeat.add(
        new Heat(
            new BlockIngredient(
                converter
                    .getBulkItemsAsList(Arrays.copyOfRange(line, 1, line.length))
                    .toArray(new ItemStack[0])),
            Integer.parseInt(line[0])));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "ItemStack ItemStack ...")
  public void addCauldron(Converter converter, String[] line) {
    scriptCauldron.add(
        new CookingPotRecipe(
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)),
            Arrays.asList((ItemStack) converter.convert(line[0])),
            0));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "ItemStack ItemStack ...")
  public void addStokedCauldron(Converter converter, String[] line) {
    scriptStokedCauldron.add(
        new CookingPotRecipe(
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)),
            Arrays.asList((ItemStack) converter.convert(line[0])),
            0));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "ItemStack ItemStack ...")
  public void addCrucible(Converter converter, String[] line) {
    scriptCrucible.add(
        new CookingPotRecipe(
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)),
            Arrays.asList((ItemStack) converter.convert(line[0])),
            0));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "ItemStack ItemStack ...")
  public void addStokedCrucible(Converter converter, String[] line) {
    scriptStokedCrucible.add(
        new CookingPotRecipe(
            RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)),
            Arrays.asList((ItemStack) converter.convert(line[0])),
            0));
  }

  @ScriptFunction(modid = "betterwithmods", inputFormat = "ItemStack ItemStack")
  public void addMill(Converter converter, String[] line) {
    scriptMill.add(
        new MillRecipe(
            Lists.newArrayList(new IngredientWrapper((ItemStack) converter.convert(line[1]))),
            // Output
            Lists.newArrayList((ItemStack) converter.convert(line[0])))); // Input
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
