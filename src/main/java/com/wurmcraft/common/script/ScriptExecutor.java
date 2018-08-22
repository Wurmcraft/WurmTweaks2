package com.wurmcraft.common.script;


import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.FunctionWrapper;
import com.wurmcraft.common.support.utils.Converter;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.io.*;

public class ScriptExecutor {

  public static NonBlockingHashMap<String, FunctionWrapper> functions = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<String, FunctionWrapper>[] sortedFunctions = new NonBlockingHashMap[2];
  private static NonBlockingHashSet<Thread> preThreads = new NonBlockingHashSet<>();
  private static NonBlockingHashSet<Thread> threads = new NonBlockingHashSet<>();
  public static volatile boolean reload = false;
  public static volatile boolean finished = false;

  public static void sortFunctions(NonBlockingHashMap<String, FunctionWrapper> toBeSorted) {
    if (sortedFunctions[1] != null && sortedFunctions[1].size() > 0) {
      if (sortedFunctions[0] != null) {
        sortedFunctions[0].clear();
      }
      sortedFunctions[1].clear();
    }
    NonBlockingHashMap<String, FunctionWrapper> pre = new NonBlockingHashMap<>();
    NonBlockingHashMap<String, FunctionWrapper> normal = new NonBlockingHashMap<>();
    for (String key : toBeSorted.keySet()) {
      if (toBeSorted.get(key).isPrecedence()) {
        pre.put(key, toBeSorted.get(key));
      } else {
        normal.put(key, toBeSorted.get(key));
      }
    }
    sortedFunctions[0] = pre;
    sortedFunctions[1] = normal;
  }

  public static void runScripts() {
    if (sortedFunctions[1] != null && sortedFunctions[1].size() == 0) {
      sortFunctions(functions);
    }
    Thread managerThread = new Thread(() -> {
      // Pre
      for (File script : ScriptChecker.getScriptsToRun()) {
        if (script.exists()) {
          Thread thread = createThread(script, true);
          thread.setName("WurmScript Pre-Worker (" + script.getName().replaceAll(".ws", "") + ")");
          preThreads.add(thread);
          thread.start();
        }
      }
      waitTillThreadsCompleted(preThreads);
      // Normal
      for (File script : ScriptChecker.getScriptsToRun()) {
        Thread thread = createThread(script, false);
        thread.setName("WurmScript Worker (" + script.getName().replaceAll(".ws", "") + ")");
        threads.add(thread);
        thread.start();
      }
      waitTillThreadsCompleted(threads);
      finished = true;
    });
    managerThread.setName("WurmScript Manager");
    managerThread.start();
  }

  private static void waitTillThreadsCompleted(NonBlockingHashSet<Thread> threads) {
    boolean notCompleted = true;
    do {
      try {
        Thread.sleep(500);
        int completed = 0;
        for (Thread thread : threads) {
          if (thread.isInterrupted()) {
            completed++;
          }
        }
        if (completed == threads.size()) {
          notCompleted = false;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (!notCompleted);
  }

  private static Thread createThread(File file, boolean pre) {
    return new Thread(() -> {
      final ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
      Bindings bindings = null;
      Converter converter = new Converter();
      if (sortedFunctions[1] == null || sortedFunctions[1].isEmpty()) {
        sortFunctions(functions);
      }
      if (pre) {
        bindings = FunctionBuilder.createFunctionBindings(sortedFunctions[0], converter);
      }
      if (bindings == null || bindings.size() == 0) {
        bindings = FunctionBuilder.createFunctionBindings(sortedFunctions[1], converter);
      }
      if (bindings.size() == 0) {
        WurmTweaks.logger
            .error("Unable to load any WurmScript Functions, Stopping Thread, " + sortedFunctions[1]
                .size() + " " + functions.size());
      } else {
        try {
          if (file.exists()) {
            engine.eval(new FileReader(file), bindings);
          } else {
            WurmTweaks.logger.warn("Unable to load script from '" + file.getName()
                + "' which is located in the master script");
          }
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
      Thread.currentThread().interrupt();
    });
  }

  public static void waitTillScriptsFinish() {
    while (!ScriptExecutor.finished) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }
  }
}
