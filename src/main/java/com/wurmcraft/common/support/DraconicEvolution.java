package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;

// TODO (1) https://github.com/brandon3055/Draconic-Evolution/tree/master/src/main/java/com/brandon3055/draconicevolution/api/fusioncrafting
@Support(modid = "draconicevolution")
public class DraconicEvolution {

  @InitSupport
  public void init() {

  }

  @FinalizeSupport
  public void finalize() {

  }

  @ScriptFunction(modid = "draconicevolution")
  public void addDEFusion(Converter converter, String[] line) {

  }
}
