package com.wurmcraft.common.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import mekanism.api.MekanismAPI;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.recipe.RecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "mekanism")
public class Mekanism {

  private static NonBlockingHashSet<Object[]> enricher;
  private static NonBlockingHashSet<Object[]> osmiumCompressor;
  private static NonBlockingHashSet<Object[]> combiner;
  private static NonBlockingHashSet<Object[]> crusher;
  private static NonBlockingHashSet<Object[]> purification;
  private static NonBlockingHashSet<Object[]> metaInfuser;
  private static NonBlockingHashSet<Object[]> chemInfuser;
  private static NonBlockingHashSet<Object[]> oxidiser;
  private static NonBlockingHashSet<Object[]> chemInjection;
  private static NonBlockingHashSet<Object[]> electroSeperator;
  private static NonBlockingHashSet<Object[]> sawmill;
  private static NonBlockingHashSet<Object[]> chemicalDissolution;
  private static NonBlockingHashSet<Object[]> washer;
  private static NonBlockingHashSet<Object[]> crystallizer;
  private static NonBlockingHashSet<Object[]> pressure;
  private static NonBlockingHashSet<Object[]> thermal;
  private static NonBlockingHashSet<Object[]> solar;
  private static NonBlockingHashSet<Object[]> box;

  @Method(modid = "mekanism")
  @InitSupport
  public void init() {
    if (enricher == null) {
      enricher = new NonBlockingHashSet<>();
      osmiumCompressor = new NonBlockingHashSet<>();
      combiner = new NonBlockingHashSet<>();
      crusher = new NonBlockingHashSet<>();
      purification = new NonBlockingHashSet<>();
      metaInfuser = new NonBlockingHashSet<>();
      chemInfuser = new NonBlockingHashSet<>();
      oxidiser = new NonBlockingHashSet<>();
      chemInjection = new NonBlockingHashSet<>();
      electroSeperator = new NonBlockingHashSet<>();
      sawmill = new NonBlockingHashSet<>();
      chemicalDissolution = new NonBlockingHashSet<>();
      washer = new NonBlockingHashSet<>();
      crystallizer = new NonBlockingHashSet<>();
      pressure = new NonBlockingHashSet<>();
      thermal = new NonBlockingHashSet<>();
      solar = new NonBlockingHashSet<>();
      box = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      RecipeHandler.Recipe.CHEMICAL_CRYSTALLIZER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_INFUSER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_INJECTION_CHAMBER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_WASHER.get().clear();
      RecipeHandler.Recipe.COMBINER.get().clear();
      RecipeHandler.Recipe.CRUSHER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_WASHER.get().clear();
      RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get().clear();
      RecipeHandler.Recipe.ENERGIZED_SMELTER.get().clear();
      RecipeHandler.Recipe.ENRICHMENT_CHAMBER.get().clear();
      RecipeHandler.Recipe.PURIFICATION_CHAMBER.get().clear();
      RecipeHandler.Recipe.PRECISION_SAWMILL.get().clear();
      RecipeHandler.Recipe.METALLURGIC_INFUSER.get().clear();
      RecipeHandler.Recipe.THERMAL_EVAPORATION_PLANT.get().clear();
      RecipeHandler.Recipe.SOLAR_NEUTRON_ACTIVATOR.get().clear();
      RecipeHandler.Recipe.OSMIUM_COMPRESSOR.get().clear();
    } else if (ScriptExecutor.reload) {
      enricher.clear();
      osmiumCompressor.clear();
      combiner.clear();
      crusher.clear();
      purification.clear();
      metaInfuser.clear();
      chemInfuser.clear();
      oxidiser.clear();
      chemInjection.clear();
      electroSeperator.clear();
      sawmill.clear();
      chemicalDissolution.clear();
      washer.clear();
      crystallizer.clear();
      pressure.clear();
      thermal.clear();
      solar.clear();
      box.clear();
      // TODO Remove Recipes on Reload
    }
  }

  @Method(modid = "mekanism")
  @FinalizeSupport
  public void finishSupport() {
    for (Object[] r : enricher) {
      RecipeHandler.addEnrichmentChamberRecipe((ItemStack) r[0], (ItemStack) r[1]);
    }
    for (Object[] r : osmiumCompressor) {
      RecipeHandler.addOsmiumCompressorRecipe((ItemStack) r[0], (ItemStack) r[1]);
    }
    for (Object[] r : combiner) {
      RecipeHandler.addCombinerRecipe((ItemStack) r[0], (ItemStack) r[1]);
    }
    for (Object[] r : crusher) {
      RecipeHandler.addCrusherRecipe((ItemStack) r[0], (ItemStack) r[1]);
    }
    for (Object[] r : metaInfuser) {
      RecipeHandler.addMetallurgicInfuserRecipe((InfuseType) r[0], (int) r[1], (ItemStack) r[2],
          (ItemStack) r[3]);
    }
    for (Object[] r : chemInfuser) {
      RecipeHandler.addChemicalInfuserRecipe((GasStack) r[0], (GasStack) r[1], (GasStack) r[2]);
    }
    for (Object[] r : oxidiser) {
      RecipeHandler.addChemicalOxidizerRecipe((ItemStack) r[0], (GasStack) r[1]);
    }
    for (Object[] r : chemInjection) {
      RecipeHandler
          .addChemicalInjectionChamberRecipe((ItemStack) r[0], (Gas) r[1], (ItemStack) r[2]);
    }
    for (Object[] r : electroSeperator) {
      RecipeHandler
          .addElectrolyticSeparatorRecipe((FluidStack) r[0], (double) r[1], (GasStack) r[2],
              (GasStack) r[3]);
    }
    for (Object[] r : sawmill) {
      if (r.length == 4) {
        RecipeHandler
            .addPrecisionSawmillRecipe((ItemStack) r[0], (ItemStack) r[1], (ItemStack) r[2],
                (float) r[3]);
      } else {
        RecipeHandler.addPrecisionSawmillRecipe((ItemStack) r[0], (ItemStack) r[1]);
      }
    }
    for (Object[] r : chemicalDissolution) {
      RecipeHandler.addChemicalDissolutionChamberRecipe((ItemStack) r[0], (GasStack) r[1]);
    }
    for (Object[] r : washer) {
      RecipeHandler.addChemicalWasherRecipe((GasStack) r[0], (GasStack) r[1]);
    }
    for (Object[] r : crystallizer) {
      RecipeHandler.addChemicalCrystallizerRecipe((GasStack) r[0], (ItemStack) r[1]);
    }
    for (Object[] r : pressure) {
      RecipeHandler
          .addPRCRecipe((ItemStack) r[0], (FluidStack) r[1], (GasStack) r[2], (ItemStack) r[3],
              (GasStack) r[4], (double) r[5], (int) r[6]);
    }
    for (Object[] r : thermal) {
      RecipeHandler.addThermalEvaporationRecipe((FluidStack) r[0], (FluidStack) r[1]);
    }
    for (Object[] r : solar) {
      RecipeHandler.addSolarNeutronRecipe((GasStack) r[0], (GasStack) r[1]);
    }
    for (Object[] r : box) {
      MekanismAPI.addBoxBlacklist((Block) r[0], (int) r[1]);
    }
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack")
  public void addEnrichmentChamber(Converter converter, String[] line) {
    enricher.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack")
  public void addOsmiumCompressor(Converter converter, String[] line) {
    osmiumCompressor.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack")
  public void addCombiner(Converter converter, String[] line) {

    combiner.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack")
  public void addMCrusher(Converter converter, String[] line) {
    crusher.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack")
  public void addPurification(Converter converter, String[] line) {

    purification.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack String Integer")
  public void addMetallurgicInfuser(Converter converter, String[] line) {
    InfuseType type = InfuseRegistry.get(line[2]);
    Preconditions.checkNotNull(type);
    metaInfuser.add(new Object[]{type, Integer.parseInt(line[3]),
        converter.convert(line[1]), converter.convert(line[0])});
  }

  // TODO Gas Converter
  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "GasStack GasStack GasStack")
  public void addChemicalInfuser(Converter converter, String[] line) {
    chemInfuser.add(new Object[]{converter.convert(line[0]), converter.convert(line[2]),
        converter.convert(line[1])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "GasStack ItemStack")
  public void addChemicalOxidizer(Converter converter, String[] line) {
    oxidiser.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack GasStack")
  public void addChemicalInjection(Converter converter, String[] line) {
    chemInjection.add(
        new Object[]{converter.convert(line[1]), ((GasStack) converter.convert(line[2])).getGas(),
            converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack GasStack Integer")
  public void addElectrolyticSeparator(Converter converter, String[] line) {
    electroSeperator.add(
        new Object[]{converter.convert(line[2]), Float.parseFloat(line[3]),
            converter.convert(line[0]), converter.convert(line[1])});

  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack ItemStack ItemStack Float")
  public void addSawmill(Converter converter, String[] line) {
    sawmill.add(new Object[]{converter.convert(line[1]),
        converter.convert(line[0]), converter.convert(line[2]), Float.parseFloat(line[3])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "GasStack ItemStack")
  public void addChemicalDissolution(Converter converter, String[] line) {
    chemicalDissolution.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "GasStack GasStack")
  public void addChemicalWasher(Converter converter, String[] line) {
    washer.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack GasStack")
  public void addChemicalCrystallizer(Converter converter, String[] line) {
    crystallizer.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack GasStack GasStack FluidStack ItemStack Integer Integer")
  public void addMekPressureChamber(Converter converter, String[] line) {
    pressure.add(new Object[]{converter.convert(line[4]), converter.convert(line[3]),
        converter.convert(line[1]),
        converter.convert(line[0]), converter.convert(line[2]), (double) Float.parseFloat(line[5]),
        Integer.parseInt(line[6])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "GasStack GasStack")
  public void addThermalEvaporation(Converter converter, String[] line) {
    thermal.add(new Object[]{converter.convert(line[0]), converter.convert(line[1])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "GasStack GasStack")
  public void addSolarNeutron(Converter converter, String[] line) {
    solar.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "mekanism")
  @ScriptFunction(modid = "mekanism", inputFormat = "ItemStack")
  public void addBoxBlacklist(Converter converter, String[] line) {
    box.add(new Object[]{converter.convert(line[0]),
        ((ItemStack) converter.convert(line[0])).getItemDamage()});
  }
}
