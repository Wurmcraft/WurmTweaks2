package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;

// TODO (3) https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/rv6-1.12/src/api/java/appeng/api/AEApi.java
@Support(modid = "appliedenergistics")
public class AE2 {

  @InitSupport
  public void init() {

  }

  @FinalizeSupport
  public void finalize() {

  }

  @ScriptFunction(modid = "appliedenergistics")
  public void addAECharger(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "appliedenergistics" ,typeData = "Crusher", type = FunctionType.Linked)
  public void addAEGrinder(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "appliedenergistics")
  public void addInscriber(Converter converter, String[] line) {

  }

}
