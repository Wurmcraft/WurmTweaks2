package com.wurmcraft.common.support;

import static com.buuz135.industrial.api.IndustrialForegoingHelper.addBioReactorEntry;
import static com.buuz135.industrial.api.IndustrialForegoingHelper.addLaserDrillEntry;
import static com.buuz135.industrial.api.IndustrialForegoingHelper.addProteinReactorEntry;
import static com.buuz135.industrial.api.IndustrialForegoingHelper.addSludgeRefinerEntry;

import com.buuz135.industrial.api.recipe.BioReactorEntry;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import com.buuz135.industrial.api.recipe.ProteinReactorEntry;
import com.buuz135.industrial.api.recipe.SludgeEntry;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "industrialforegoing")
public class IndustrialForgoing {

  private static NonBlockingHashSet<BioReactorEntry> bioReactor;
  private static NonBlockingHashSet<SludgeEntry> sludge;
  private static NonBlockingHashSet<ProteinReactorEntry> protein;
  private static NonBlockingHashSet<LaserDrillEntry> laser;

  @Method(modid = "industrialforegoing")
  @InitSupport
  public void init() {
    if (bioReactor == null) {
      bioReactor = new NonBlockingHashSet<>();
      sludge = new NonBlockingHashSet<>();
      protein = new NonBlockingHashSet<>();
      laser = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      BioReactorEntry.BIO_REACTOR_ENTRIES.clear();
      SludgeEntry.SLUDGE_RECIPES.clear();
      ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES.clear();
      LaserDrillEntry.LASER_DRILL_ENTRIES.clear();
    } else if (ScriptExecutor.reload) {
      bioReactor.forEach(entry -> BioReactorEntry.BIO_REACTOR_ENTRIES.remove(entry));
      bioReactor.clear();
      sludge.forEach(entry -> SludgeEntry.SLUDGE_RECIPES.remove(entry));
      sludge.clear();
      protein.forEach(entry -> ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES.remove(entry));
      protein.clear();
      laser.forEach(entry -> LaserDrillEntry.LASER_DRILL_ENTRIES.remove(entry));
      laser.clear();
    }
  }

  @Method(modid = "industrialforegoing")
  @FinalizeSupport
  public void finishSupport() {
    for (BioReactorEntry recipe : bioReactor) {
      addBioReactorEntry(recipe);
    }
    for (SludgeEntry recipe : sludge) {
      addSludgeRefinerEntry(recipe);
    }
    for (ProteinReactorEntry recipe : protein) {
      addProteinReactorEntry(recipe);
    }
    for (LaserDrillEntry recipe : laser) {
      addLaserDrillEntry(recipe);
    }
  }

  @Method(modid = "industrialforegoing")
  @ScriptFunction(modid = "industrialforegoing", inputFormat = "ItemStack")
  public void addBioReactor(Converter converter, String[] line) {
    bioReactor.add(new BioReactorEntry((ItemStack) converter.convert(line[0], 1)));
  }

  @Method(modid = "industrialforegoing")
  @ScriptFunction(modid = "industrialforegoing", inputFormat = "ItemStack Integer")
  public void addSludgeRefiner(Converter converter, String[] line) {
    sludge
        .add(new SludgeEntry((ItemStack) converter.convert(line[0], 1), Integer.parseInt(line[1])));
  }

  @Method(modid = "industrialforegoing")
  @ScriptFunction(modid = "industrialforegoing", inputFormat = "ItemStack")
  public void addProteinReactor(Converter converter, String[] line) {
    protein.add(new ProteinReactorEntry((ItemStack) converter.convert(line[0], 1)));
  }

  @Method(modid = "industrialforegoing")
  @ScriptFunction(modid = "industrialforegoing", inputFormat = "ItemStack Integer Integer", typeData = "Laser", type = FunctionType.Linked)
  public void addLaser(Converter converter, String[] line) {
    laser.add(
        new LaserDrillEntry(Integer.parseInt(line[1]), (ItemStack) converter.convert(line[0], 1),
            Integer.parseInt(line[2])));
  }
}
