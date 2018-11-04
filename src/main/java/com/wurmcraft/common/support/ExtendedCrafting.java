package com.wurmcraft.common.support;

import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "extendedcrafting")
public class ExtendedCrafting {

  private NonBlockingHashSet<Object[]> compression = new NonBlockingHashSet();
  private NonBlockingHashSet<Object[]> table = new NonBlockingHashSet();
  private NonBlockingHashSet<Object[]> tableShapeless = new NonBlockingHashSet();
  private NonBlockingHashSet<Object[]> ender = new NonBlockingHashSet<>();

  @InitSupport(modid = "extendedcrafting")
  public void init() {
    if (ScriptExecutor.reload) {
      compression.clear();
      table.clear();
      tableShapeless.clear();
      ender.clear();
    }
  }

  @FinalizeSupport(modid = "extendedcrafting")
  public void finalizeSupport() {
    compression.forEach(
        data ->
            CompressorRecipeManager.getInstance()
                .addRecipe(
                    (ItemStack) data[0],
                    data[1],
                    ((ItemStack) (data[1])).getCount(),
                    (ItemStack) data[2],
                    (Boolean) data[3],
                    (Integer) data[4]));
    table.forEach(
        data ->
            TableRecipeManager.getInstance()
                .addShaped((Integer) data[0], (ItemStack) data[1], (Object[]) data[2]));
    tableShapeless.forEach(
        data ->
            TableRecipeManager.getInstance()
                .addShapeless((Integer) data[0], (ItemStack) data[1], data[2]));
    for (Object[] data : ender) {
      EnderCrafterRecipeManager.getInstance().addShaped((ItemStack) data[0], data[1]);
    }
  }

  @ScriptFunction(
    modid = "extendedcrafting",
    inputFormat = "ItemStack ItemStack ItemStack Boolean Integer"
  )
  public void addCompression(Converter converter, String[] line) {
    compression.add(
        new Object[] {
          converter.convert(line[0]),
          converter.convert(line[1]),
          ((ItemStack) converter.convert(line[1])).getCount(),
          converter.convert(line[2]),
          Boolean.parseBoolean(line[3]),
          Integer.parseInt(line[4])
        });
  }

  @ScriptFunction(modid = "extendedcrafting", inputFormat = "Integer ItemStack Object ...")
  public void addShapedTable(Converter converter, String[] line) {
    table.add(
        new Object[] {
          Integer.parseInt(line[0]),
          converter.convert(line[1], 1),
          RecipeUtils.getShapedRecipe(Arrays.copyOfRange(line, 2, line.length))
              .toArray(new Object[0])
        });
  }

  @ScriptFunction(modid = "extendedcrafting", inputFormat = "Integer ItemStack Object ...")
  public void addShapelessTable(Converter converter, String[] line) {
    tableShapeless.add(
        new Object[] {
          Integer.parseInt(line[0]),
          converter.convert(line[1], 1),
          RecipeUtils.getShapelessRecipeInput(Arrays.copyOfRange(line, 2, line.length))
        });
  }

  @ScriptFunction(modid = "extendedcrafting", inputFormat = "Integer Object ...")
  public void addEnder(Converter converter, String[] line) {
    ender.add(
        new Object[] {
          converter.convert(line[0], 1),
          RecipeUtils.getShapedRecipe(Arrays.copyOfRange(line, 1, line.length))
              .toArray(new Object[0])
        });
  }
}
