package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import vazkii.botania.api.BotaniaAPI;

@Support(modid = "botania")
public class Botania {

  private static NonBlockingHashSet<Object[]> conjuration;
  private static NonBlockingHashSet<Object[]> alchemy;
  private static NonBlockingHashSet<Object[]> infusion;
  private static NonBlockingHashSet<Object[]> elven;
  private static NonBlockingHashSet<Object[]> rune;
  private static NonBlockingHashSet<Object[]> pureDaisy;
  private static NonBlockingHashSet<Object[]> apohecary;

  @InitSupport
  public void init() {
    if (conjuration == null) {
      conjuration = new NonBlockingHashSet<>();
      alchemy = new NonBlockingHashSet<>();
      infusion = new NonBlockingHashSet<>();
      elven = new NonBlockingHashSet<>();
      rune = new NonBlockingHashSet<>();
      pureDaisy = new NonBlockingHashSet<>();
      apohecary = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllMachineRecipes) {
      BotaniaAPI.manaInfusionRecipes.clear();
      BotaniaAPI.elvenTradeRecipes.clear();
      BotaniaAPI.pureDaisyRecipes.clear();
    } else if (ScriptExecutor.reload) {
      conjuration.clear();
      infusion.clear();
      elven.clear();
      rune.clear();
      pureDaisy.clear();
      apohecary.clear();
      alchemy.clear();
      // TODO Recipe Removal on Reload
    }
  }

  @FinalizeSupport
  public void finishSupport() {
    for (Object[] recipe : conjuration) {
      BotaniaAPI.registerManaConjurationRecipe((ItemStack) recipe[0], (ItemStack) recipe[1],
          (int) recipe[2]);
    }
    for (Object[] recipe : infusion) {
      BotaniaAPI.registerManaInfusionRecipe((ItemStack) recipe[0], (ItemStack) recipe[1],
          (int) recipe[2]);
    }
    for (Object[] recipe : elven) {
      BotaniaAPI.registerElvenTradeRecipe((ItemStack[]) recipe[0], (ItemStack) recipe[1]);
    }
    for (Object[] recipe : rune) {
      BotaniaAPI
          .registerRuneAltarRecipe((ItemStack) recipe[0], (int) recipe[1], (Object[]) recipe[2]);
    }
    for (Object[] recipe : pureDaisy) {
      BotaniaAPI.registerPureDaisyRecipe(recipe[0], (IBlockState) recipe[1]);
    }
    for (Object[] recipe : apohecary) {
      BotaniaAPI.registerPetalRecipe((ItemStack) recipe[0], (Object[]) recipe[1]);
    }
    for (Object[] recipe : alchemy) {
      BotaniaAPI.registerManaAlchemyRecipe((ItemStack) recipe[0], recipe[1], (int) recipe[2]);
    }
  }

  @ScriptFunction(modid = "botania", inputFormat = "ItemStack ItemStack ...")
  public void addApothecary(Converter converter, String[] line) {
    apohecary.add(new Object[]{converter.convert(line[0]),
        RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 1, line.length)).toArray(
            new Object[0])});
  }

  @ScriptFunction(modid = "botania")
  public void addPureDaisy(Converter converter, String[] line) {
    pureDaisy.add(
        new Object[]{Block.getBlockFromItem(((ItemStack) converter.convert(line[1], 1)).getItem()),
            Block.getBlockFromItem(
                ((ItemStack) converter.convert(line[0], 1)).getItem()).getDefaultState()});
  }

  @ScriptFunction(modid = "botania", inputFormat = "ItemStack Integer ItemStack/OreDictionary ...")
  public void addRune(Converter converter, String[] line) {
    rune.add(new Object[]{converter.convert(line[0]), Integer.parseInt(line[1]),
        RecipeUtils.getShapelessIngredient(Arrays.copyOfRange(line, 2, line.length)).toArray(
            new Object[0])});
  }


  @ScriptFunction(modid = "botania", inputFormat = "ItemStack ItemStack")
  public void addElven(Converter converter, String[] line) {
    infusion.add(new Object[]{converter.convert(line[0]), converter.convert(line[1])});
  }

  @ScriptFunction(modid = "botania", inputFormat = "ItemStack ItemStack Integer")
  public void addInfusion(Converter converter, String[] line) {
    infusion.add(new Object[]{converter.convert(line[0]), converter.convert(line[1]),
        Integer.parseInt(line[2])});
  }

  @ScriptFunction(modid = "botania", inputFormat = "ItemStack ItemStack Integer")
  public void addAlchemy(Converter converter, String[] line) {
    alchemy.add(new Object[]{converter.convert(line[0]), converter.convert(line[1]),
        Integer.parseInt(line[2])});
  }

  @ScriptFunction(modid = "botania", inputFormat = "ItemStack ItemStack Integer")
  public void addConjuration(Converter converter, String[] line) {
    conjuration.add(new Object[]{converter.convert(line[0]), converter.convert(line[1]),
        Integer.parseInt(line[2])});
  }
}
