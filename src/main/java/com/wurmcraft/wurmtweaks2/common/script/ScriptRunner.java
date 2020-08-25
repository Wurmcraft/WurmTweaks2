package com.wurmcraft.wurmtweaks2.common.script;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptRunner {

  public static final ScriptEngineManager manager = new ScriptEngineManager();
  public static ScriptEngine engine = manager.getEngineByName("jython");

  public static Bindings createBindings() {
    Bindings bindings = engine.createBindings();
    return bindings;
  }
}
