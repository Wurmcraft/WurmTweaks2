package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.RollingMachineRecipe;
import techreborn.api.ScrapboxList;
import techreborn.api.TechRebornAPI;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.BaseRecipe;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.api.recipe.machines.CompressorRecipe;
import techreborn.api.recipe.machines.DistillationTowerRecipe;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;

@Support(modid = "techreborn")
public class TechReborn {

  private static NonBlockingHashSet<Object[]> shapeless;
  private static NonBlockingHashSet<Object[]> shaped;
  private static NonBlockingHashSet<Object[]> scrap;
  private static NonBlockingHashSet<Object[]> fluidGenerator;
  private static NonBlockingHashSet<FusionReactorRecipe> fusion;
  private static NonBlockingHashSet<BaseRecipe> machine;

  @InitSupport
  public void init() {
    if (shapeless == null) {
      shapeless = new NonBlockingHashSet<>();
      shaped = new NonBlockingHashSet<>();
      scrap = new NonBlockingHashSet<>();
      fluidGenerator = new NonBlockingHashSet<>();
      fusion = new NonBlockingHashSet<>();
      machine = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      RollingMachineRecipe.instance.getRecipeList().clear();
      ScrapboxList.stacks.clear();
      FusionReactorRecipeHelper.reactorRecipes.clear();
      RecipeHandler.recipeList.clear();
    } else if (ScriptExecutor.reload) {
      shapeless.clear();
      shaped.clear();
      scrap.clear();
      fluidGenerator.clear();
      fusion.clear();
      machine.clear();
    }
  }

  @FinalizeSupport
  public void finishSupport() {
    for (Object[] r : shapeless) {
      TechRebornAPI
          .addShapelessOreRollingMachinceRecipe((ResourceLocation) r[0], (ItemStack) r[1],
              (Ingredient[]) r[2]);
    }
    for (Object[] r : shaped) {
      TechRebornAPI
          .addRollingOreMachinceRecipe((ResourceLocation) r[0], (ItemStack) r[1], (Object[]) r[2]);
    }
    for (Object[] r : scrap) {
      ScrapboxList.addItemStackToList((ItemStack) r[0]);
    }
    for (Object[] r : fluidGenerator) {
      GeneratorRecipeHelper.registerFluidRecipe((EFluidGenerator) r[0], (Fluid) r[1], (int) r[2]);
    }
    for (FusionReactorRecipe r : fusion) {
      FusionReactorRecipeHelper.registerRecipe(r);
    }
    for (BaseRecipe recipe : machine) {
      RecipeHandler.addRecipe(recipe);
    }
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ...")
  public void addShapelessRolling(Converter converter, String[] line) {
    shapeless.add(new Object[]{new ResourceLocation(
        Global.MODID, converter.convert(line[0], 1).toString()), converter.convert(line[0], 1),
        RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)).toArray(
            new Ingredient[0])});
  }

  @ScriptFunction(modid = "techreborn")
  public void addShapedRolling(Converter converter, String[] line) {
    shaped.add(new Object[]{new ResourceLocation(Global.MODID,
        converter.convert(line[0]).toString()), converter.convert(line[0], 1),
        RecipeUtils.getShapedRecipe(line).toArray(new Object[0])});
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack")
  public void addScrapbox(Converter converter, String[] line) {

    scrap.add(new Object[]{converter.convert(line[0])});
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "String FluidStack Integer")
  public void addGeneratorFluid(Converter converter, String[] line) {

    fluidGenerator.add(
        new Object[]{getGeneratorType(line[0]), ((FluidStack) converter.convert(line[1])).getFluid(),
            Integer.parseInt(line[2])});
  }

  private EFluidGenerator getGeneratorType(String name) {
    return EFluidGenerator.valueOf(name.toUpperCase());
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack Integer Integer Integer")
  public void addTechFusion(Converter converter, String[] line) {
    fusion.add(new FusionReactorRecipe((ItemStack) converter.convert(line[1]),
        (ItemStack) converter.convert(line[2]),
        (ItemStack) converter.convert(line[0]), Integer.parseInt(line[3]),
        Integer.parseInt(line[4]),
        Integer.parseInt(line[5])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack Integer Integer")
  public void addAlloySmelter(Converter converter, String[] line) {
    machine.add(new AlloySmelterRecipe(converter.convert(line[1]), converter.convert(line[2]),
        (ItemStack) converter.convert(line[0]), Integer.parseInt(line[3]),
        Integer.parseInt(line[4])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack Integer Integer")
  public void addAssemblingMachine(Converter converter, String[] line) {
    machine.add(new AssemblingMachineRecipe(converter.convert(line[1]), converter.convert(line[2]),
        (ItemStack) converter.convert(line[0]), Integer.parseInt(line[3]),
        Integer.parseInt(line[4])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack ItemStack Integer Integer Integer")
  public void addIndustrialBlastFurnace(Converter converter, String[] line) {
    machine.add(new BlastFurnaceRecipe(converter.convert(line[2]), converter.convert(line[3]),
        (ItemStack) converter.convert(line[0]), (ItemStack) converter.convert(line[1]),
        Integer.parseInt(line[4]),
        Integer.parseInt(line[5]), Integer.parseInt(line[6])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack ItemStack ItemStack Integer Integer ItemStack")
  public void addTRCentrifuge(Converter converter, String[] line) {
    machine.add(new CentrifugeRecipe(converter.convert(line[4]), converter.convert(line[7]),
        (ItemStack) converter.convert(line[0]), (ItemStack) converter.convert(line[1]),
        (ItemStack) converter.convert(line[2]),
        (ItemStack) converter.convert(line[3]), Integer.parseInt(line[5]),
        Integer.parseInt(line[6])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack Integer Integer")
  public void addChemicalReactor(Converter converter, String[] line) {
    machine.add(new ChemicalReactorRecipe(converter.convert(line[2]), converter.convert(line[1]),
        (ItemStack) converter.convert(line[0]), Integer.parseInt(line[3]),
        Integer.parseInt(line[4])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack Integer Integer")
  public void addCompressor(Converter converter, String[] line) {
    machine.add(
        new CompressorRecipe(converter.convert(line[1]), (ItemStack) converter.convert(line[0]),
            Integer.parseInt(line[2]), Integer.parseInt(line[3])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack ItemStack ItemStack Integer Integer")
  public void addDistillationTower(Converter converter, String[] line) {
    machine.add(new DistillationTowerRecipe(converter.convert(line[3]), converter.convert(line[4]),
        (ItemStack) converter.convert(line[0]), (ItemStack) converter.convert(line[1]),
        (ItemStack) converter.convert(line[2]),
        (ItemStack) converter.convert
            (line[3]), Integer.parseInt(line[5]), Integer.parseInt(line[6])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack Integer Integer")
  public void addExtractor(Converter converter, String[] line) {
    machine
        .add(new ExtractorRecipe(converter.convert(line[1]), (ItemStack) converter.convert(line[0]),
            Integer.parseInt(line[2]), Integer.parseInt(line[3])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack Integer Integer")
  public void addGrinder(Converter converter, String[] line) {
    machine
        .add(new GrinderRecipe(converter.convert(line[1]), (ItemStack) converter.convert(line[0]),
            Integer.parseInt(line[2]), Integer.parseInt(line[3]) * 10));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack ItemStack Integer Integer")
  public void addImplosionCompressor(Converter converter, String[] line) {
    machine.add(
        new ImplosionCompressorRecipe(converter.convert(line[0]), converter.convert(line[1]),
            (ItemStack) converter.convert(line[2]), (ItemStack) converter.convert(line[3]),
            Integer.parseInt(line[4]),
            Integer.parseInt(line[5])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack ItemStack ItemStack ItemStack ItemStack Integer Integer")
  public void addIndustrialElectrolyzer(Converter converter, String[] line) {
    machine.add(
        new IndustrialElectrolyzerRecipe(converter.convert(line[0]), converter.convert(line[1]),
            (ItemStack) converter.convert(line[2]), (ItemStack) converter.convert(line[3]),
            (ItemStack) converter.convert(line[4]),
            (ItemStack) converter.convert(line[5]), Integer.parseInt(line[6]),
            Integer.parseInt(line[7])));
  }

  @ScriptFunction(modid = "ItemStack FluidStack ItemStack ItemStack ItemStack ItemStack Integer Integer")
  public void addIndustrialGrinder(Converter converter, String[] line) {
    machine.add(new IndustrialGrinderRecipe(converter.convert(line[0]),
        (FluidStack) converter.convert(line[1]),
        (ItemStack) converter.convert(line[2]), (ItemStack) converter.convert(line[3]),
        (ItemStack) converter.convert(line[4]),
        (ItemStack) converter.convert(line[5]), Integer.parseInt(line[6]),
        Integer.parseInt(line[7])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack FluidStack ItemStack ItemStack ItemStack Integer Integer")
  public void addIndustrialSawmill(Converter converter, String[] line) {
    machine.add(new IndustrialSawmillRecipe((ItemStack) converter.convert(line[0]),
        (FluidStack) converter.convert(line[1]), (ItemStack) converter.convert(line[2]),
        (ItemStack) converter.convert(line[3]), (ItemStack) converter.convert(line[4]),
        Integer.parseInt(line[5]), Integer.parseInt(line[6])));
  }

  @ScriptFunction(modid = "techreborn", inputFormat = "ItemStack ItemStack Integer Integer")
  public void addVacuumFreezer(Converter converter, String[] line) {
    machine.add(
        new VacuumFreezerRecipe(converter.convert(line[1]), (ItemStack) converter.convert(line[0]),
            Integer.parseInt(line[2]), Integer.parseInt(line[3]) * 10));
  }
}
