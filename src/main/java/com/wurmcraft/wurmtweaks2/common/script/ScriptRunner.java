package com.wurmcraft.wurmtweaks2.common.script;

import com.wurmcraft.wurmtweaks2.WurmTweaks2;
import com.wurmcraft.wurmtweaks2.common.config.ConfigHandler;
import com.wurmcraft.wurmtweaks2.common.script.loader.ScriptIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import joptsimple.internal.Strings;
import net.minecraft.util.text.TextComponentString;

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

  public static void runScripts() {
    runScripts(ConfigHandler.initialScript);
  }

  public static void runScripts(String val) {
    if (val.startsWith("http")
        || val.startsWith("https:")) {    // Web script
      try {
        String urlData = readUrlToString(val);
        String name = val.substring(val.lastIndexOf("/") + 1);
        if (val.endsWith(".ws")) { // Download more scripts
          for (String line : urlData.split("\n")) {
            if (line.startsWith("http") && line.endsWith(".py")
                || line.startsWith("https") && line.endsWith(".py")) {
              runScripts(line);
            }
          }
        } else if (val.endsWith(".py")) {
          saveDownloadedScript(name, urlData);
          runScript(name, urlData);
        } else {
          WurmTweaks2.LOGGER.warn("Failed to run script '" + val + "'");
        }
      } catch (IOException e) {
        WurmTweaks2.LOGGER.warn(
            "Failed to read URL for script(s) (" + val + ") '"
                + e.getMessage() + "'");
        e.printStackTrace();
      }
    } else {    // Local Script(s)
      File[] scripts = ScriptIO.getScripts();
      for (File script : scripts) {
        try {
          runScript(script.getName(), Strings.join(Files.readAllLines(script.toPath()).toArray(new String[0]),
              "\n")
          );
        } catch (Exception e) {
          WurmTweaks2.LOGGER.warn(e.getMessage());
        }
      }
    }
  }

  public static String readUrlToString(String url) throws IOException {
    URL oracle = new URL(url);
    BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
    StringBuilder builder = new StringBuilder();
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      builder.append(inputLine).append("\n");
    }
    in.close();
    return builder.toString();
  }

  private static void saveDownloadedScript(String name, String data) {
    try {
      Files.write(new File(ScriptIO.SAVE_DIR + File.separator + name).toPath(),
          data.getBytes());
    } catch (IOException e) {
      WurmTweaks2.LOGGER.warn("Failed to save script '" + name + "'");
      e.printStackTrace();
    }
  }

  public static void runScript(String name, String scriptData) {
    File[] scripts = ScriptIO.getScripts();
    for (File script : scripts) {
      Writer writer = new StringWriter();
      if (script.getName().matches(name)) {
        WurmTweaks2.LOGGER.info("Running script '" + script.getName() + "'");
        try {
          ScriptRunner.createBindings();
          ScriptContext context = new SimpleScriptContext();
          context.setBindings(ScriptRunner.createBindings(),
              ScriptContext.GLOBAL_SCOPE);
          context.setBindings(ScriptRunner.createBindings(),
              ScriptContext.ENGINE_SCOPE);
          context.setAttribute(ScriptEngine.FILENAME, script.getName(),
              ScriptContext.ENGINE_SCOPE);
          context.setAttribute("mc_version", "1.12.2", ScriptContext.GLOBAL_SCOPE);
          context.setWriter(writer);
          ScriptRunner.engine.eval(
              Strings.join(ScriptRunner.core_py, "") + scriptData, context);
        } catch (Exception e) {
          WurmTweaks2.LOGGER.warn(e.getMessage());
        }
      }
      return;
    }
  }
}
