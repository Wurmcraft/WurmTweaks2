package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;

// TODO (7) https://gitlab.com/BetterWithMods/BetterWithMods/tree/1.12/src/main/java/betterwithmods/common/registry
@Support(modid = "betterwithmods")
public class BetterWithMods {

  @InitSupport
  public void init() {

  }

  @FinalizeSupport
  public void finalize() {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addHopperFilter(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addShapedAnvil(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addShapelessAnvil(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addKiln(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addBWMSawMill(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addTurntable(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "betterwithmods")
  public void addBWMHeat(Converter converter, String[] line) {

  }
}
