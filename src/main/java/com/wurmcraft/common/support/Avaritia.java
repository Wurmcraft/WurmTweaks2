package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;

// TODO (3) https://github.com/Morpheus1101/Avaritia/blob/master/src/main/java/morph/avaritia/recipe/AvaritiaRecipeManager.java
@Support(modid = "avaritia")
public class Avaritia {

  @InitSupport
  public void init() {

  }

  @FinalizeSupport
  public void finalize() {

  }

  @ScriptFunction(modid = "avaritia")
  public void addCompression(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "avaritia")
  public void addExtreamShapeless(Converter converter, String[] line) {

  }

  @ScriptFunction(modid = "avaritia")
  public void addExtreamShaped(Converter converter, String[] line) {

  }
}
