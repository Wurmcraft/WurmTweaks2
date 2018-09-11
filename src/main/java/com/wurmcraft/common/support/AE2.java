package com.wurmcraft.common.support;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import appeng.api.features.IInscriberRecipe;
import appeng.api.features.InscriberProcessType;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.ae2.AE2Grinder;
import com.wurmcraft.common.support.utils.ae2.AE2Inscriber;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

// TODO Find out how even AE2 registry's show recipes within it
@Support(modid = "appliedenergistics")
public class AE2 {

  private static NonBlockingHashSet<IGrinderRecipe> scriptGrinder;
  private static NonBlockingHashSet<IInscriberRecipe> scriptInscriber;

  @Method(modid = "appliedenergistics")
  @InitSupport
  public void init() {
    if (scriptGrinder == null || scriptInscriber == null) {
      scriptGrinder = new NonBlockingHashSet<>();
      scriptInscriber = new NonBlockingHashSet<>();
    }
    if (ConfigHandler.removeAllRecipes) {
      while (AEApi.instance().registries().grinder().getRecipes().iterator().hasNext()) {
        AEApi.instance().registries().grinder()
            .removeRecipe(AEApi.instance().registries().grinder().getRecipes().iterator().next());
      }
      while (AEApi.instance().registries().inscriber().getRecipes().iterator().hasNext()) {
        AEApi.instance().registries().inscriber()
            .removeRecipe(AEApi.instance().registries().inscriber().getRecipes().iterator().next());
      }
    } else if (ScriptExecutor.reload) {
      for (IGrinderRecipe recipe : scriptGrinder) {
        AEApi.instance().registries().grinder().removeRecipe(recipe);
      }
      scriptGrinder.clear();
      for (IInscriberRecipe recipe : scriptInscriber) {
        AEApi.instance().registries().inscriber().removeRecipe(recipe);
      }
      scriptInscriber.clear();
    }
  }

  @Method(modid = "appliedenergistics")
  @FinalizeSupport
  public void finalize() {
    for (IGrinderRecipe recipe : scriptGrinder) {
      AEApi.instance().registries().grinder().addRecipe(recipe);
    }
    for (IInscriberRecipe recipe : scriptInscriber) {
      AEApi.instance().registries().inscriber().addRecipe(recipe);
    }
  }

  @Method(modid = "appliedenergistics")
  @ScriptFunction(modid = "appliedenergistics", inputFormat = "ItemStack ItemStack ItemStack Float Integer", typeData = "Crusher", type = FunctionType.Linked)
  public void addAEGrinder(Converter converter, String[] line) {
    scriptGrinder.add(new AE2Grinder((ItemStack) converter.convert(line[1]),
        (ItemStack) converter.convert(line[0]), (ItemStack) converter.convert(line[2]),
        Float.parseFloat(line[3]), Integer.parseInt(line[4])));
  }

  @Method(modid = "appliedenergistics")
  @ScriptFunction(modid = "appliedenergistics", inputFormat = "ItemStack ItemStack ItemStack String ItemStack")
  public void addInscriber(Converter converter, String[] line) {
    scriptInscriber.add(new AE2Inscriber(
        converter.getBulkItemsAsList(Arrays.copyOfRange(line, 4, line.length)),
        (ItemStack) converter.convert(line[0]), (ItemStack) converter.convert(line[1]),
        (ItemStack) converter.convert(line[2]), Objects.requireNonNull(getType(line[3]))));
  }

  @Method(modid = "appliedenergistics")
  private InscriberProcessType getType(String type) {
    if (type.equalsIgnoreCase(InscriberProcessType.INSCRIBE.name())) {
      return InscriberProcessType.INSCRIBE;
    } else if (type.equalsIgnoreCase(InscriberProcessType.PRESS.name())) {
      return InscriberProcessType.INSCRIBE;
    }
    return null;
  }
}
