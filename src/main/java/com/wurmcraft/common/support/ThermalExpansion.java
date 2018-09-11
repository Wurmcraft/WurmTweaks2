package com.wurmcraft.common.support;

import cofh.thermalexpansion.util.managers.device.FactorizerManager;
import cofh.thermalexpansion.util.managers.machine.CentrifugeManager;
import cofh.thermalexpansion.util.managers.machine.CompactorManager;
import cofh.thermalexpansion.util.managers.machine.CrucibleManager;
import cofh.thermalexpansion.util.managers.machine.FurnaceManager;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalexpansion.util.managers.machine.TransposerManager;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "thermalexpansion")
public class ThermalExpansion {

  private static NonBlockingHashSet<Object[]> furnace;
  private static NonBlockingHashSet<Object[]> pulverizer;
  private static NonBlockingHashSet<Object[]> sawmill;
  private static NonBlockingHashSet<Object[]> smelter;
  private static NonBlockingHashSet<Object[]> compactor;
  private static NonBlockingHashSet<Object[]> crucible;
  private static NonBlockingHashSet<Object[]> centerfuge;
  private static NonBlockingHashSet<TransposerManager.TransposerRecipe> extractTransposer;
  private static NonBlockingHashSet<TransposerManager.TransposerRecipe> fillTransposer;

  @Method(modid = "thermalexpansion")
  @InitSupport
  public void init() {
    if (furnace == null) {
      furnace = new NonBlockingHashSet<>();
      pulverizer = new NonBlockingHashSet<>();
      sawmill = new NonBlockingHashSet<>();
      compactor = new NonBlockingHashSet<>();
      crucible = new NonBlockingHashSet<>();
      smelter = new NonBlockingHashSet<>();
      centerfuge = new NonBlockingHashSet<>();
      extractTransposer = new NonBlockingHashSet<>();
      fillTransposer = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      for (FurnaceManager.FurnaceRecipe recipe : FurnaceManager.getRecipeList(false)) {
        FurnaceManager.removeRecipe(recipe.getInput());
      }
      for (PulverizerManager.PulverizerRecipe recipe : PulverizerManager.getRecipeList()) {
        PulverizerManager.removeRecipe(recipe.getInput());
      }
      for (SmelterManager.SmelterRecipe recipe : SmelterManager.getRecipeList()) {
        SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
      }
      for (CompactorManager.Mode mode : CompactorManager.Mode.values()) {
        for (CompactorManager.CompactorRecipe recipe : CompactorManager.getRecipeList(mode)) {
          CompactorManager.removeRecipe(recipe.getInput(), mode);
        }
      }
      for (CentrifugeManager.CentrifugeRecipe recipe : CentrifugeManager.getRecipeList()) {
        CentrifugeManager.removeRecipe(recipe.getInput());
      }
      for (FactorizerManager.FactorizerRecipe recipe : FactorizerManager.getRecipeList(false)) {
        FactorizerManager.removeRecipe(recipe.getInput(), false);
      }
    } else if (ScriptExecutor.reload) {
      furnace.clear();
      pulverizer.clear();
      sawmill.clear();
      smelter.clear();
      compactor.clear();
      crucible.clear();
      centerfuge.clear();
      // TODO Enable Machine Reloading
    }
  }

  @Method(modid = "thermalexpansion")
  @FinalizeSupport
  public void finishSupport() {
    for (Object[] recipe : furnace) {
      FurnaceManager.addRecipe((int) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2]);
    }
    for (Object[] r : pulverizer) {
      if (r.length == 5) {
        PulverizerManager.addRecipe(
            (int) r[0], (ItemStack) r[1], (ItemStack) r[2], (ItemStack) r[3], (int) r[4]);
      } else {
        PulverizerManager.addRecipe((int) r[0], (ItemStack) r[1], (ItemStack) r[2]);
      }
    }
    for (Object[] r : sawmill) {
      if (r.length == 2) {
        SmelterManager.addRecipe((int) r[0], (ItemStack) r[1], (ItemStack) r[2], (ItemStack) r[3]);
      } else {
        SawmillManager.addRecipe((int) r[0], (ItemStack) r[1], (ItemStack) r[2]);
      }
    }
    for (Object[] r : compactor) {
      CompactorManager.addRecipe(
          (int) r[0], (ItemStack) r[1], (ItemStack) r[2], (CompactorManager.Mode) r[3]);
    }
    for (Object[] r : crucible) {
      CrucibleManager.addRecipe((int) r[0], (ItemStack) r[1], (FluidStack) r[2]);
    }
    for (Object[] r : centerfuge) {
      CentrifugeManager.addRecipe(
          (int) r[0], (ItemStack) r[1], (List<ItemStack>) r[2], (FluidStack) r[3]);
    }
    for (TransposerManager.TransposerRecipe r : fillTransposer) {
      TransposerManager.addFillRecipe(
          r.getEnergy(), r.getInput(), r.getOutput(), r.getFluid(), true);
    }
    for (TransposerManager.TransposerRecipe r : extractTransposer) {
      TransposerManager.addExtractRecipe(
          r.getEnergy(), r.getInput(), r.getOutput(), r.getFluid(), r.getChance(), true);
    }
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(modid = "thermalexpansion", inputFormat = "ItemStack ItemStack Integer")
  public void addRedstoneFurnace(Converter converter, String[] line) {
    furnace.add(
        new Object[] {
          Integer.parseInt(line[2]), converter.convert(line[1]), converter.convert(line[0])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(
    modid = "thermalexpansion",
    inputFormat = "ItemStack ItemStack Integer ItemStack Integer"
  )
  public void addPulverizer(Converter converter, String[] line) {
    pulverizer.add(
        new Object[] {
          Integer.parseInt(line[2]),
          converter.convert(line[1]),
          converter.convert(line[0]),
          converter.convert(line[3]),
          Integer.parseInt(line[4])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(modid = "thermalexpansion", inputFormat = "ItemStack ItemStack Integer")
  public void addTESawmill(Converter converter, String[] line) {
    sawmill.add(
        new Object[] {
          Integer.parseInt(line[2]), converter.convert(line[1]), converter.convert(line[0])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(modid = "thermalexpansion", inputFormat = "ItemStack ItemStack ItemStack Integer")
  public void addSmelter(Converter converter, String[] line) {
    smelter.add(
        new Object[] {
          Integer.parseInt(line[3]),
          converter.convert(line[1]),
          converter.convert(line[2]),
          converter.convert(line[0])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(modid = "thermalexpansion", inputFormat = "ItemStack ItemStack Integer String")
  public void addCompactor(Converter converter, String[] line) {
    compactor.add(
        new Object[] {
          Integer.parseInt(line[2]),
          converter.convert(line[1]),
          converter.convert(line[0]),
          getMode(line[3])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(modid = "thermalexpansion", inputFormat = "FluidStack ItemStack Integer")
  public void addMagmaCrucible(Converter converter, String[] line) {
    crucible.add(
        new Object[] {
          Integer.parseInt(line[2]), converter.convert(line[1]), converter.convert(line[0])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(
    modid = "thermalexpansion",
    inputFormat = "ItemStack ItemStack ItemStack ItemStack ItemStack Integer FluidStack",
    typeData = "Centerfuge",
    type = FunctionType.Linked
  )
  public void addTECenterfuge(Converter converter, String[] line) {
    centerfuge.add(
        new Object[] {
          Integer.parseInt(line[5]),
          converter.convert(line[4]),
          Arrays.asList(
              converter.convert(line[0]),
              converter.convert(line[1]),
              converter.convert(line[2]),
              converter.convert(line[3])),
          converter.convert(line[6])
        });
  }

  @Method(modid = "thermalexpansion")
  @ScriptFunction(
    modid = "thermalexpansion",
    inputFormat = "ItemStack ItemStack FluidStack Integer String Integer"
  )
  public void addFluidTransposer(Converter converter, String[] line) {
    TransposerManager.TransposerRecipe recipe =
        new TransposerManager.TransposerRecipe(
            (ItemStack) converter.convert(line[1]),
            (ItemStack) converter.convert(line[0]),
            (FluidStack) converter.convert(line[2]),
            Integer.parseInt(line[3]),
            line.length == 6 ? Integer.parseInt(line[5]) : 0);
    if (line[4].matches("[eE]xtract")) {
      extractTransposer.add(recipe);
    } else if (line[4].matches("[fF]ill")) {
      fillTransposer.add(recipe);
    }
  }

  @Method(modid = "thermalexpansion")
  private CompactorManager.Mode getMode(String mode) {
    if (mode.matches("[gG]ear")) {
      return CompactorManager.Mode.GEAR;
    } else if (mode.matches("[aA]ll")) {
      return CompactorManager.Mode.ALL;
    } else if (mode.matches("[cC]oin")) {
      return CompactorManager.Mode.COIN;
    } else if (mode.matches("[pP]late")) {
      return CompactorManager.Mode.PLATE;
    }
    return null;
  }
}
