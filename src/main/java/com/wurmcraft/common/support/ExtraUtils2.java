package com.wurmcraft.common.support;

import com.rwtema.extrautils2.api.machine.XUMachineCrusher;
import com.rwtema.extrautils2.tile.TileResonator;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "extrautils2")
public class ExtraUtils2 {

  private NonBlockingHashSet<Object[]> resonator;
  private NonBlockingHashSet<Object[]> crusher;

  @InitSupport
  public void init() {
    resonator = new NonBlockingHashSet<>();
    crusher = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      TileResonator.resonatorRecipes.clear();
      while (XUMachineCrusher.INSTANCE.recipes_registry.iterator().hasNext()) {
        XUMachineCrusher.INSTANCE.recipes_registry
            .removeRecipe(XUMachineCrusher.INSTANCE.recipes_registry.iterator().next());
      }
    } else if (ScriptExecutor.reload) {
      // TODO Remove Recipes
      resonator.clear();
      crusher.clear();
    }
  }

  @FinalizeSupport
  public void finishSupport() {
    for (Object[] recipe : resonator) {
      TileResonator.register((ItemStack) recipe[0], (ItemStack) recipe[1], (int) recipe[2]);
    }
    for (Object[] recipe : crusher) {
      if (recipe.length == 4) {
        XUMachineCrusher
            .addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1], (ItemStack) recipe[2],
                (float) recipe[3]);
      } else {
        XUMachineCrusher.addRecipe((ItemStack) recipe[0], (ItemStack) recipe[1]);
      }
    }
  }

  @ScriptFunction(modid = "extrautils2", inputFormat = "ItemStack ItemStack Integer")
  public void addResonator(Converter converter, String[] line) {
    resonator.add(new Object[]{converter.convert(line[1]), converter.convert(line[0]),
        converter.convert(line[2])});
  }

  @ScriptFunction(modid = "extrautils2", inputFormat = "ItemStack ItemStack ItemStack Float")
  public void addXUCrusher(Converter converter, String[] line) {
    if (line.length == 2) {
      crusher.add(new Object[]{converter.convert(line[1]), converter.convert(line[0])});
    } else {
      crusher.add(new Object[]{converter.convert(line[1]), converter.convert(line[0]),
          converter.convert(line[2]), converter.convert(line[3])});
    }
  }
}
