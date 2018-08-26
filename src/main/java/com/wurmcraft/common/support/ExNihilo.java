package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import exnihilocreatio.registries.CompostRegistry;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "exnihilocreatio")
public class ExNihilo {

  private static NonBlockingHashSet<Crook> scriptCrook;
  private static NonBlockingHashSet<Compost> scriptCompost;
  private static NonBlockingHashSet<Crucible> scriptCrucible;
  private static NonBlockingHashSet<Sieve> scriptSieve;

  @InitSupport
  public void init() {
    if (scriptCrook == null) {
      scriptCrook = new NonBlockingHashSet<>();
      scriptCompost = new NonBlockingHashSet<>();
      scriptCrucible = new NonBlockingHashSet<>();
      scriptSieve = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllRecipes) {
      ExNihiloRegistryManager.CROOK_REGISTRY.getRecipeList().clear();
    } else if (ScriptExecutor.reload) {
      // TODO Setup Recipe Reloads
    }
  }

  @FinalizeSupport
  public void finailze() {
    scriptCrook.forEach(crook -> ExNihiloRegistryManager.CROOK_REGISTRY
        .register(crook.input, crook.reward, crook.chance, crook.chance));
    scriptCompost.forEach(compost -> CompostRegistry.register(compost.item, compost.compost));
    scriptCrucible
        .forEach(crucible -> ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.register(crucible.item,
            new Meltable(crucible.fluid.getUnlocalizedName(), crucible.fluid.amount)));
    scriptSieve.forEach(sieve -> ExNihiloRegistryManager.SIEVE_REGISTRY
        .register(sieve.input, sieve.drop, sieve.chance, sieve.lvl));
  }

  @ScriptFunction(modid = "exnihilocreatio", inputFormat = "ItemStack ItemStack Float Float")
  public void addCrook(Converter converter, String[] line) {
    scriptCrook.add(new Crook(new BlockInfo((ItemStack) converter.convert(line[0], 1)),
        (ItemStack) converter.convert(line[1], 1), Float.parseFloat(line[2]),
        Float.parseFloat(line[3])));
  }

  @ScriptFunction(modid = "exnihilocreatio", inputFormat = "ItemStack Float ItemStack")
  public void addCompost(Converter converter, String[] line) {
    scriptCompost.add(new Compost(new ItemInfo((ItemStack) converter.convert(line[0])),
        new Compostable(Float.parseFloat(line[1]), new Color(150, 150, 150, 80),
            new BlockInfo((ItemStack) converter.convert(line[2])))));
  }

  @ScriptFunction(modid = "exnihilocreatio")
  public void addENCrucible(Converter converter, String[] line) {
    scriptCrucible.add(new Crucible((ItemStack) converter.convert(line[0]),
        (FluidStack) converter.convert(line[1])));
  }

  @ScriptFunction(modid = "exnihilocreatio")
  public void addSieve(Converter converter, String[] line) {
    scriptSieve.add(new Sieve((ItemStack) converter.convert(line[1], 1),
        new ItemInfo((ItemStack) converter.convert(line[0], 1)), Float.parseFloat(line[2]),
        Integer.parseInt(line[3])));
  }

  public class Crook {

    public BlockInfo input;
    public ItemStack reward;
    public float chance;
    public float fortune;

    public Crook(BlockInfo input, ItemStack reward, float chance, float fortune) {
      this.input = input;
      this.reward = reward;
      this.chance = chance;
      this.fortune = fortune;
    }
  }

  public class Compost {

    public ItemInfo item;
    public Compostable compost;

    public Compost(ItemInfo item, Compostable compost) {
      this.item = item;
      this.compost = compost;
    }
  }

  public class Crucible {

    public ItemStack item;
    public FluidStack fluid;

    public Crucible(ItemStack item, FluidStack fluid) {
      this.item = item;
      this.fluid = fluid;
    }
  }

  public class Sieve {

    public ItemStack input;
    public StackInfo drop;
    public float chance;
    public int lvl;

    public Sieve(ItemStack input, StackInfo drop, float chance, int lvl) {
      this.input = input;
      this.drop = drop;
      this.chance = chance;
      this.lvl = lvl;
    }
  }
}
