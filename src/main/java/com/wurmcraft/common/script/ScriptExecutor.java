package com.wurmcraft.common.script;


import com.wurmcraft.api.script.FunctionWrapper;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

public class ScriptExecutor {

  public static NonBlockingHashMap<String, FunctionWrapper> functions = new NonBlockingHashMap<>();
  private static NonBlockingHashSet<NonBlockingHashMap<String, FunctionWrapper>> sortedFunctions = new NonBlockingHashSet<>();

  public static void sortFunctions(NonBlockingHashMap<String, FunctionWrapper> toBeSorted) {
    NonBlockingHashMap<String, FunctionWrapper> pre = new NonBlockingHashMap<>();
    NonBlockingHashMap<String, FunctionWrapper> normal = new NonBlockingHashMap<>();
    for (String key : toBeSorted.keySet()) {
      if (toBeSorted.get(key).isPrecedence()) {
        pre.put(key, toBeSorted.get(key));
      } else {
        normal.put(key, toBeSorted.get(key));
      }
    }
    sortedFunctions.add(pre);
    sortedFunctions.add(normal);
  }

}
