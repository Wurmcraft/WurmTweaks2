package com.wurmcraft.wurmtweaks2.common.script;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptRunner {

    public static final ScriptEngineManager manager = new ScriptEngineManager();
    public static ScriptEngine engine = manager.getEngineByName("jython");
    public static String[] core_py = new String[]{
            "from com.wurmcraft.wurmtweaks2.common.script.jython.recipes import ShapelessRecipe;",
            "from com.wurmcraft.wurmtweaks2.common.script.jython.recipes import ShapedRecipe;",
            "from com.wurmcraft.wurmtweaks2.common.script.jython.recipes import FurnaceRecipe;",
            "from com.wurmcraft.wurmtweaks2.common.script.jython import Item;",
            "from com.wurmcraft.wurmtweaks2.common.script.jython import OreDictionary;",
            "from com.wurmcraft.wurmtweaks2.common.script.jython.recipes import BrewingRecipe;"
    };

    public static Bindings createBindings() {
        Bindings bindings = engine.createBindings();
        return bindings;
    }
}
