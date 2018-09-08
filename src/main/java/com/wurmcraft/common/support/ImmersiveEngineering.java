package com.wurmcraft.common.support;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "immersiveengineering")
public class ImmersiveEngineering {

  private static NonBlockingHashSet<AlloyRecipe> scriptAlloy;
  private static NonBlockingHashSet<ArcFurnaceRecipe> scriptArcFurnace;
  private static NonBlockingHashSet<BlastFurnaceRecipe> scriptBlastFurnace;
  private static NonBlockingHashSet<BlueprintCraftingRecipe> scriptBlueprint;
  private static NonBlockingHashSet<BottlingMachineRecipe> scriptBottling;
  private static NonBlockingHashSet<CokeOvenRecipe> scriptCoke;
  private static NonBlockingHashSet<CrusherRecipe> scriptCrusher;
  private static NonBlockingHashSet<FermenterRecipe> scriptFermenter;
  private static NonBlockingHashSet<MetalPressRecipe> scriptMetal;
  private static NonBlockingHashSet<MixerRecipe> scriptMixer;
  private static NonBlockingHashSet<RefineryRecipe> scriptRefinery;
  private static NonBlockingHashSet<SqueezerRecipe> scriptSqueezer;

  @Method(modid = "immersiveengineering")
  @InitSupport
  public void init() {
    scriptAlloy = new NonBlockingHashSet<>();
    scriptArcFurnace = new NonBlockingHashSet<>();
    scriptBlastFurnace = new NonBlockingHashSet<>();
    scriptBlueprint = new NonBlockingHashSet<>();
    scriptBottling = new NonBlockingHashSet<>();
    scriptCoke = new NonBlockingHashSet<>();
    scriptCrusher = new NonBlockingHashSet<>();
    scriptFermenter = new NonBlockingHashSet<>();
    scriptMetal = new NonBlockingHashSet<>();
    scriptMixer = new NonBlockingHashSet<>();
    scriptRefinery = new NonBlockingHashSet<>();
    scriptSqueezer = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllRecipes) {
      AlloyRecipe.recipeList.clear();
      ArcFurnaceRecipe.recipeList.clear();
      BlastFurnaceRecipe.recipeList.clear();
      BottlingMachineRecipe.recipeList.clear();
      FermenterRecipe.recipeList.clear();
      MixerRecipe.recipeList.clear();
      RefineryRecipe.recipeList.clear();
      SqueezerRecipe.recipeList.clear();
    } else if (ScriptExecutor.reload) {
      scriptAlloy.stream().map(alloy -> alloy.output).forEach(AlloyRecipe::removeRecipes);
      scriptAlloy.clear();
      scriptArcFurnace.stream().map(smelting -> smelting.output)
          .forEach(ArcFurnaceRecipe::removeRecipes);
      scriptArcFurnace.clear();
      scriptBlastFurnace.stream().map(smelting -> smelting.output)
          .forEach(BlastFurnaceRecipe::removeRecipes);
      scriptBlastFurnace.clear();
      scriptBlueprint.forEach(
          recipe -> BlueprintCraftingRecipe.recipeList.remove(recipe.blueprintCategory, recipe));
      scriptBlueprint.clear();
      scriptBottling.stream().map(bottle -> bottle.output)
          .forEach(BottlingMachineRecipe::removeRecipes);
      scriptCoke.stream().map(oven -> oven.output).forEach(CokeOvenRecipe::removeRecipes);
      scriptBottling.clear();
      scriptCrusher.stream().map(crusher -> crusher.output)
          .forEach(CrusherRecipe::removeRecipesForOutput);
      scriptCrusher.clear();
      scriptFermenter.forEach(fermenter -> FermenterRecipe.recipeList.remove(fermenter));
      scriptFermenter.clear();
      scriptMetal.stream().map(recipe -> recipe.output).forEach(MetalPressRecipe::removeRecipes);
      scriptMetal.clear();
      scriptMixer.forEach(mixer -> MixerRecipe.recipeList.remove(mixer));
      scriptMixer.clear();
      scriptRefinery.forEach(refinery -> RefineryRecipe.recipeList.remove(refinery));
      scriptRefinery.clear();
      scriptSqueezer.forEach(squeezer -> SqueezerRecipe.recipeList.remove(squeezer));
      scriptSqueezer.clear();
    }
  }

  @Method(modid = "immersiveengineering")
  @FinalizeSupport
  public void finalizeSupport() {
    scriptAlloy.forEach(
        alloy -> AlloyRecipe.addRecipe(alloy.output, alloy.input0, alloy.input1, alloy.time));
    scriptArcFurnace.forEach(arc -> ArcFurnaceRecipe
        .addRecipe(arc.output, arc.input, arc.slag, arc.getTotalProcessTime(),
            arc.getTotalProcessEnergy() / arc.getTotalProcessTime(), arc.additives));
    scriptBlastFurnace.forEach(
        blast -> BlastFurnaceRecipe.addRecipe(blast.output, blast.input, blast.time, blast.slag));
    scriptBlueprint.forEach(print -> BlueprintCraftingRecipe
        .addRecipe(print.blueprintCategory, print.output, print.inputs));
    for (BottlingMachineRecipe bottling : scriptBottling) {
      BottlingMachineRecipe.addRecipe(bottling.output, bottling.input, bottling.fluidInput);
    }
    for (CokeOvenRecipe oven : scriptCoke) {
      CokeOvenRecipe.addRecipe(oven.output, oven.input, oven.time, oven.creosoteOutput);
    }
    for (CrusherRecipe crusher : scriptCrusher) {
      CrusherRecipe.addRecipe(crusher.output, crusher.input,
          (crusher.getTotalProcessEnergy() / crusher.getTotalProcessTime()));
    }
    scriptFermenter.forEach(fermenter -> FermenterRecipe
        .addRecipe(fermenter.fluidOutput, fermenter.itemOutput, fermenter.input,
            fermenter.getTotalProcessEnergy() / fermenter.getTotalProcessTime()));
    for (MetalPressRecipe metal : scriptMetal) {
      MetalPressRecipe.addRecipe(metal.output, metal.input, metal.mold,
          metal.getTotalProcessEnergy() / metal.getTotalProcessEnergy());
    }
    for (MixerRecipe mixer : scriptMixer) {
      MixerRecipe.addRecipe(mixer.fluidOutput, mixer.fluidInput, mixer.itemInputs,
          mixer.getTotalProcessEnergy() / mixer.getTotalProcessEnergy());
    }
    for (RefineryRecipe refinery : scriptRefinery) {
      RefineryRecipe.addRecipe(refinery.output, refinery.input0, refinery.input1,
          refinery.getTotalProcessEnergy() / refinery.getTotalProcessTime());
    }
    for (SqueezerRecipe squeezer : scriptSqueezer) {
      SqueezerRecipe.addRecipe(squeezer.fluidOutput, squeezer.itemOutput, squeezer.input,
          squeezer.getTotalProcessEnergy() / squeezer.getTotalProcessEnergy());
    }
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat =
      "ItemStack ItemStack/OreDictionary"
          + " ItemStack/OreDictionary Integer", typeData = "Alloy", type = FunctionType.Linked)
  public void addIEAlloy(Converter converter, String[] line) {
    scriptAlloy.add(
        new AlloyRecipe((ItemStack) converter.convert(line[0]), converter.convert(line[1]),
            converter.convert(line[2]), Integer.parseInt(line[3])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat =
      "ItemStack ItemStack/OreDictionary"
          + " ItemStack Integer", typeData = "Blast", type = FunctionType.Linked)
  public void addIEBlastFurnace(Converter converter, String[] line) {
    scriptBlastFurnace.add(
        new BlastFurnaceRecipe((ItemStack) converter.convert(line[0]), converter.convert(line[1]),
            Integer.parseInt(line[3]), (ItemStack) converter.convert(line[2])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "String ItemStack ItemStack/OreDictionary ...")
  public void addBlueprint(Converter converter, String[] line) {
    scriptBlueprint.add(new BlueprintCraftingRecipe(line[0].replaceAll("_", ""),
        (ItemStack) converter.convert(line[0]), converter.getBulkItems(
        Arrays.copyOfRange(line, 1, line.length))));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "ItemStack ItemStack/OreDictionary FluidStack")
  public void addBottling(Converter converter, String[] line) {
    // TODO FluidStack DataConverter
    scriptBottling.add(new BottlingMachineRecipe((ItemStack) converter.convert(line[0]),
        converter.convert(line[1]), (FluidStack) converter.convert(line[2])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "ItemStack ItemStack/OreDictionary Integer Integer", typeData = "Coke", type = FunctionType.Linked)
  public void addCokeOven(Converter converter, String[] line) {
    scriptCoke.add(
        new CokeOvenRecipe((ItemStack) converter.convert(line[0]), converter.convert(line[1]),
            Integer.parseInt(line[2]), Integer.parseInt(line[3])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "ItemStack ItemStack/OreDictionary Integer", typeData = "Crusher", type = FunctionType.Linked)
  public void addIECrusher(Converter converter, String[] line) {
    scriptCrusher.add(
        new CrusherRecipe((ItemStack) converter.convert(line[0]), converter.convert(line[1]),
            Integer.parseInt(line[2])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "FluidStack ItemStack ItemStack/OreDictionary Integer")
  public void addFermenter(Converter converter, String[] line) {
    scriptFermenter.add(new FermenterRecipe((FluidStack) converter.convert(line[0]),
        (ItemStack) converter.convert(line[1]), converter.convert(line[2]),
        Integer.parseInt(line[3])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "ItemStack ItemStack/OreDictionary ItemStack Integer", typeData = "Press", type = FunctionType.Linked)
  public void addIEMetalPress(Converter converter, String[] line) {
    scriptMetal.add(
        new MetalPressRecipe((ItemStack) converter.convert(line[0]), converter.convert(line[1]),
            new ComparableItemStack((ItemStack) converter.convert(line[2], 1)),
            Integer.parseInt(line[3])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "FluidStack FluidStack Integer ItemStack/OreDictionary ...")
  public void addMixer(Converter converter, String[] line) {
    scriptMixer.add(new MixerRecipe((FluidStack) converter.convert(line[0]),
        (FluidStack) converter.convert(line[1]),
        converter.getBulkItems(Arrays.copyOfRange(line, 3, line.length)),
        Integer.parseInt(line[2])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "FluidStack FluidStack FluidStack Integer", typeData = "Refinery", type = FunctionType.Linked)
  public void addIERefinery(Converter converter, String[] line) {
    scriptRefinery.add(new RefineryRecipe((FluidStack) converter.convert(line[0]),
        (FluidStack) converter.convert(line[1]), (FluidStack) converter.convert(line[2]),
        Integer.parseInt(line[3])));
  }

  @Method(modid = "immersiveengineering")
  @ScriptFunction(modid = "immersiveengineering", inputFormat = "FluidStack ItemStack ItemStack/OreDictionary Integer")
  public void adSqueezer(Converter converter, String[] line) {
    scriptSqueezer.add(new SqueezerRecipe((FluidStack) converter.convert(line[0]),
        (ItemStack) converter.convert(line[1]), converter.convert(line[2]),
        Integer.parseInt(line[3])));
  }
}
