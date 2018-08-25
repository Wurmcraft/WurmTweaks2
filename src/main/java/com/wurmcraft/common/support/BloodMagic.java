package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;


// TODO (5) https://github.com/WayofTime/BloodMagic/blob/1.12/src/api/java/WayofTime/bloodmagic/api/IBloodMagicAPI.java
@Support(modid = "bloodmagic")
public class BloodMagic {

  @InitSupport
  public void init() {

  }

  @FinalizeSupport
  public void finalize() {

  }

  @ScriptFunction(modid = "bloodmagic")
  public void addBloodAltar(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "bloodmagic")
  public void addAlchemyTable(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "bloodmagic")
  public void addTartaricForge(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "bloodmagic")
  public void addAlchemyArray(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "bloodmagic")
  public void addSacrificalCraft(Converter converter, String[] line) {

  }

}
