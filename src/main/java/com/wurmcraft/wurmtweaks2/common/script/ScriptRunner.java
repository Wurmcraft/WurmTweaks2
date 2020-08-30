package com.wurmcraft.wurmtweaks2.common.script;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.python.antlr.ast.Str;

public class ScriptRunner {

  public static final ScriptEngineManager manager = new ScriptEngineManager();
  public static ScriptEngine engine = manager.getEngineByName("jython");
  public static String[] core_py = new String[] {
      "from com.wurmcraft.wurmtweaks2.common.script.jython.recipes import ShapelessRecipe;",
      "from com.wurmcraft.wurmtweaks2.common.script.jython import Item;",
  };

  public static Bindings createBindings() {
    Bindings bindings = engine.createBindings();
    return bindings;
  }
}
