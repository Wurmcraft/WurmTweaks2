package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;

// TODO (5) https://github.com/HellFirePvP/AstralSorcery/blob/master/src/main/java/hellfirepvp/astralsorcery/common/registry/RegistryRecipes.java
@Support(modid = "astralsorcery")
public class AstralSorcery {

  @InitSupport
  public void init() {

  }

  @FinalizeSupport
  public void finalize() {

  }

  @ScriptFunction(modid = "astralsorcery")
  public void addGrindStone(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "astralsorcery")
  public void addRitual(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "astralsorcery")
  public void addAttunment(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "astralsorcery")
  public void addInfusion(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "astralsorcery")
  public void addWeakInfusion(Converter converter, String[] line) {

  }

}
