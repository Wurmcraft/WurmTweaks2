package com.wurmcraft.common.support;

import static com.google.common.collect.Lists.newArrayList;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import toughasnails.config.json.ArmorTemperatureData;
import toughasnails.init.ModConfig;

@Support(modid = "toughasnails")
public class ToughAsNails {

  public NonBlockingHashSet<ArmorTemperatureData> armorTemp;

  @InitSupport(modid = "toughasnails")
  public void init() {
    armorTemp = new NonBlockingHashSet<>();
  }

  @FinalizeSupport(modid = "toughasnails")
  public void finalizeSupport() {
    ModConfig.armorTemperatureData.addAll(armorTemp);
  }

  @ScriptFunction(modid = "toughasnails", inputFormat = "ItemStack Integer")
  public void addArmorTemp(Converter converter, String[] line) {
    armorTemp.add(
        new ArmorTemperatureData(
            newArrayList(
                ((ItemStack) converter.convert(line[0])).getItem().getRegistryName().toString()),
            Integer.parseInt(line[1])));
  }
}
