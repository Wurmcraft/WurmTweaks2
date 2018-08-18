package com.wurmcraft.common.script;

import com.wurmcraft.api.script.FunctionWrapper;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class FunctionBuilder {

  public static NonBlockingHashMap<String, FunctionWrapper> searchForFunctions(
      ASMDataTable asmTable) {
    NonBlockingHashMap<String, FunctionWrapper> functions = new NonBlockingHashMap<>();
    for (ASMData data : asmTable.getAll(Support.class.getName())) {
      try {
        Class<?> asmClass = Class.forName(data.getClassName());
        Method[] supportFunctions = searchForScriptFunctions(asmClass);
        for (Method method : supportFunctions) {
          String name = getFunctionName(method);
          functions.put(name, createFunction(asmClass.getAnnotation(Support.class),
              method.getAnnotation(ScriptFunction.class), method));
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    NonBlockingHashMap<String, FunctionWrapper> looseFunctions = searchForLooseFunctions(asmTable);
    for (String key : looseFunctions.keySet()) {
      functions.putIfAbsent(key, looseFunctions.get(key));
    }
    return functions;
  }

  private static NonBlockingHashMap<String, FunctionWrapper> searchForLooseFunctions(
      ASMDataTable asmDataTable) {
    NonBlockingHashMap<String, FunctionWrapper> functions = new NonBlockingHashMap<>();
    for (ASMData data : asmDataTable.getAll(ScriptFunction.class.getName())) {
      try {

        Class<?> asmClass = Class.forName(data.getClassName());
        Method[] scriptFunctions = searchForScriptFunctions(asmClass);
        for (Method method : scriptFunctions) {
          functions.putIfAbsent(getFunctionName(method),
              createFunction(null, method.getAnnotation(ScriptFunction.class), method));
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return functions;
  }

  private static Method[] searchForScriptFunctions(Class clazz) {
    List<Method> methods = new ArrayList<>();
    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getAnnotation(ScriptFunction.class) != null) {
        methods.add(method);
      }
    }
    return methods.toArray(new Method[0]);
  }

  private static String getFunctionName(Method method) {
    return method.getAnnotation(ScriptFunction.class).name().length() > 0 ? method
        .getAnnotation(ScriptFunction.class).name() : method.getName();
  }

  private static FunctionWrapper createFunction(Support support, ScriptFunction function,
      Method method) {
    if (validFunction(method)) {
      if (support != null) {
        return new FunctionWrapper(
            function.modid().length() > 0 ? function.modid() : support.modid(),
            support.supportDependencies(),
            function.threadSafe() || (support.threaded()), support.suppotCode(), function.type(),
            function.precedence(), getFunctionName(method), function.typeData(),
            function.inputFormat(), function.guiVar(), method);
      } else {
        return new FunctionWrapper(function.modid(), "", function.threadSafe(), (byte) 0,
            function.type(),
            function.precedence(), function.name(), function.typeData(), function.inputFormat(),
            function.guiVar(), method);
      }
    }
    return null;
  }

  private static boolean validFunction(Method method) {
    return true;
  }

  public static void preInitSupport() {

  }

  public static void initSupport() {

  }

  public static void postInitFinalizeSupport() {

  }

  public static void serverStartingFinalizeSupport() {

  }
}
