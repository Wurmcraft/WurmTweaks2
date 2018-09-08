package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import java.util.Arrays;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.common.recipes.AmadronOfferManager;
import me.desht.pneumaticcraft.common.recipes.AssemblyRecipe;
import me.desht.pneumaticcraft.common.recipes.PressureChamberRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "pneumaticcraft")
public class PnumaticCraft {

  private static NonBlockingHashSet<Object[]> drill;
  private static NonBlockingHashSet<Object[]> laser;
  private static NonBlockingHashSet<Object[]> pressure;
  private static NonBlockingHashSet<Object[]> amadron;
  private static NonBlockingHashSet<Object[]> defaultAmadron;

  @Method(modid = "pneumaticcraft")
  @InitSupport
  public void init() {
    drill = new NonBlockingHashSet<>();
    laser = new NonBlockingHashSet<>();
    pressure = new NonBlockingHashSet<>();
    amadron = new NonBlockingHashSet<>();
    defaultAmadron = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      AssemblyRecipe.drillRecipes.clear();
      AssemblyRecipe.laserRecipes.clear();
      PressureChamberRecipe.chamberRecipes.clear();
      AmadronOfferManager.getInstance().getStaticOffers().clear();
      AmadronOfferManager.getInstance().getPeriodicOffers().clear();
      AmadronOfferManager.getInstance().getAllOffers().clear();
    } else if (ScriptExecutor.reload) {
      // TODO Remove Recipes On Reload
      drill.clear();
      laser.clear();
      pressure.clear();
      amadron.clear();
      defaultAmadron.clear();
    }
  }

  @Method(modid = "pneumaticcraft")
  @FinalizeSupport
  public void finishSupport() {
    for (Object[] recipe : drill) {
      PneumaticRegistry.getInstance().getRecipeRegistry()
          .addAssemblyDrillRecipe(recipe[0], recipe[1]);
    }
    for (Object[] recipe : laser) {
      PneumaticRegistry.getInstance().getRecipeRegistry()
          .addAssemblyLaserRecipe(recipe[0], recipe[1]);
    }
    for (Object[] recipe : pressure) {
      PneumaticRegistry.getInstance().getRecipeRegistry()
          .registerPressureChamberRecipe((ItemStack[]) recipe[0], (float) recipe[1],
              (ItemStack[]) recipe[2]);
    }
    for (Object[] recipe : defaultAmadron) {
      PneumaticRegistry.getInstance().getRecipeRegistry()
          .registerDefaultStaticAmadronOffer(recipe[0], recipe[1]);
    }
    for (Object[] recipe : amadron) {
      PneumaticRegistry.getInstance().getRecipeRegistry()
          .registerDefaultPeriodicAmadronOffer(recipe[0], recipe[1]);
    }
  }

  @Method(modid = "pneumaticcraft")
  @ScriptFunction(modid = "pneumaticcraft", inputFormat = "ItemStack ItemStack")
  public void addAssemblyDrill(Converter converter, String[] line) {
    drill.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "pneumaticcraft")
  @ScriptFunction(modid = "pneumaticcraft", inputFormat = "ItemStack ItemStack")
  public void addAssemblyLaser(Converter converter, String[] line) {
    laser.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
  }

  @Method(modid = "pneumaticcraft")
  @ScriptFunction(modid = "pneumaticcraft", inputFormat = "ItemStack Integer ItemStack ...")
  public void addPressureChamber(Converter converter, String[] line) {
    pressure.add(new Object[]{
        converter.getBulkItemsAsList(Arrays.copyOfRange(line, 2, line.length)).toArray(
            new ItemStack[0]), Float.parseFloat(line[1]),
        new ItemStack[]{(ItemStack) converter.convert(line[0])}});
  }

  @Method(modid = "pneumaticcraft")
  @ScriptFunction(modid = "pneumaticcraft", inputFormat = "ItemStack ItemStack")
  public void addDefaultAmadron(Converter converter, String[] line) {
    defaultAmadron.add(new Object[]{converter.convert(line[0]), converter.convert(line[1])});
  }

  @Method(modid = "pneumaticcraft")
  @ScriptFunction(modid = "pneumaticcraft", inputFormat = "ItemStack ItemStack")
  public void addAmadron(Converter converter, String[] line) {
    amadron.add(new Object[]{converter.convert(line[0]), converter.convert(line[1])});
  }
}
