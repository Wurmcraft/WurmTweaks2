package com.wurmcraft.common.script;

import com.wurmcraft.api.script.FunctionWrapper;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.InitSupport.EnumInitType;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.ScriptFunctionWrapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.script.Bindings;
import javax.script.SimpleBindings;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

public class FunctionBuilder {

  private static NonBlockingHashMap<String, NonBlockingHashSet<Object[]>> initData = new NonBlockingHashMap<>();

  public static NonBlockingHashMap<String, FunctionWrapper> init(ASMDataTable table) {
    NonBlockingHashMap<String, FunctionWrapper> returnData = searchForFunctions(table);
    sortInitData(table);
    return returnData;
  }

  private static NonBlockingHashMap<String, FunctionWrapper> searchForFunctions(
      ASMDataTable asmTable) {
    NonBlockingHashMap<String, FunctionWrapper> functions = new NonBlockingHashMap<>();
    for (ASMData data : asmTable.getAll(Support.class.getName())) {
      try {
        Class<?> asmClass = Class.forName(data.getClassName());
        Method[] supportFunctions = searchForAnnotations(asmClass, Support.class);
        for (Method method : supportFunctions) {
          String name = getFunctionName(method);
          if (method.getAnnotation(ScriptFunction.class).modid().length() == 0 && isModLoaded(
              asmClass.getAnnotation(Support.class).modid()) || isModLoaded(
              method.getAnnotation(ScriptFunction.class).modid())) {
            functions.put(name,
                Objects.requireNonNull(createFunction(asmClass.getAnnotation(Support.class),
                    method.getAnnotation(ScriptFunction.class), method, autoCast(asmClass))));
          }
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
        Method[] scriptFunctions = searchForAnnotations(asmClass, ScriptFunction.class);
        for (Method method : scriptFunctions) {
          if (method.getAnnotation(ScriptFunction.class).modid().length() == 0 || isModLoaded(
              method.getAnnotation(ScriptFunction.class).modid())) {
            functions.putIfAbsent(getFunctionName(method),
                createFunction(null, method.getAnnotation(ScriptFunction.class), method,
                    autoCast(asmClass)));
          }
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return functions;
  }

  public static <T> T autoCast(Class<T> type) {
    return (T) type;
  }

  private static <T extends Annotation> Method[] searchForAnnotations(Class clazz,
      Class<T> annotation) {
    List<Method> methods = new ArrayList<>();
    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getAnnotation(annotation) != null) {
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
      Method method, Object clazz) {
    if (validFunction(method)) {
      if (support != null) {
        return new FunctionWrapper(
            function.modid().length() > 0 ? function.modid() : support.modid(),
            support.supportDependencies(),
            function.threadSafe() || (support.threaded()), support.suppotCode(), function.type(),
            function.precedence(), getFunctionName(method), function.typeData(),
            function.inputFormat(), function.guiVar(), method, clazz);
      } else {
        return new FunctionWrapper(function.modid(), "", function.threadSafe(), (byte) 0,
            function.type(),
            function.precedence(), function.name(), function.typeData(), function.inputFormat(),
            function.guiVar(), method, clazz);
      }
    }
    return null;
  }

  private static boolean validFunction(Method method) {
    return true;
  }

  private static void sortInitData(ASMDataTable dataTable) {
    NonBlockingHashSet<Object[]> preInit = new NonBlockingHashSet<>();
    NonBlockingHashSet<Object[]> init = new NonBlockingHashSet<>();
    NonBlockingHashSet<Object[]> postInit = new NonBlockingHashSet<>();
    NonBlockingHashSet<Object[]> serverStarting = new NonBlockingHashSet<>();
    // Before Scripts are Run
    for (ASMData data : dataTable.getAll(InitSupport.class.getName())) {
      try {
        Class<?> asmClass = Class.forName(data.getClassName());
        Method[] initMethods = searchForAnnotations(asmClass, InitSupport.class);
        for (Method method : initMethods) {
          InitSupport initSupport = method.getAnnotation(InitSupport.class);
          if (initSupport.initType().equals(EnumInitType.PREINIT)) {
            preInit.add(new Object[]{method, asmClass});
          } else if (initSupport.initType().equals(EnumInitType.INIT)) {
            init.add(new Object[]{method, asmClass});
          }
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    // After Scripts are run
    for (ASMData data : dataTable.getAll(FinalizeSupport.class.getName())) {
      try {
        Class<?> asmClass = Class.forName(data.getClassName());
        Method[] initMethods = searchForAnnotations(asmClass, FinalizeSupport.class);
        for (Method method : initMethods) {
          FinalizeSupport initSupport = method.getAnnotation(FinalizeSupport.class);
          if (initSupport.initType().equals(FinalizeSupport.EnumInitType.POSTINIT)) {
            postInit.add(new Object[]{method, asmClass});
          } else if (initSupport.initType().equals(FinalizeSupport.EnumInitType.SERVER_STARTING)) {
            serverStarting.add(new Object[]{method, asmClass});
          }
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    initData.put("pre", preInit);
    initData.putIfAbsent("init", init);
    initData.put("post", postInit);
    initData.put("starting", serverStarting);
  }

  private static void invokeMethod(Object[] obj) {
    try {
      Method method = (Method) obj[0];
      method.setAccessible(true);
      try {
        method.invoke(((Class<?>) obj[1]).newInstance(), new Object[]{});
      } catch (InstantiationException e) {
        e.printStackTrace();
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static void preInitSupport() {
    for (Object[] obj : initData.get("pre")) {
      invokeMethod(obj);
    }
  }

  public static void initSupport() {
    for (Object[] obj : initData.get("init")) {
      invokeMethod(obj);
    }
  }

  public static void postInitFinalizeSupport() {
    if (ScriptExecutor.finished) {
      for (Object[] obj : initData.get("post")) {
        invokeMethod(obj);
      }
    } else {
      while (!ScriptExecutor.finished) {
        try {
          Thread.currentThread().sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      postInitFinalizeSupport();
    }
  }

  public static void serverStartingFinalizeSupport() {
    for (Object[] obj : initData.get("starting")) {
      invokeMethod(obj);
    }
  }

  public static boolean isModLoaded(String modid) {
    return Loader.isModLoaded(modid) || modid.equalsIgnoreCase("minecraft") || modid
        .equalsIgnoreCase("events");
  }

  public static Bindings createFunctionBindings(
      NonBlockingHashMap<String, FunctionWrapper> functions, Converter converter) {
    SimpleBindings bindings = new SimpleBindings();
    for (String key : functions.keySet()) {
      FunctionWrapper wrapper = functions.get(key);
      bindings
          .put(key, new ScriptFunctionWrapper((Method) wrapper.getFunction(), converter));
    }
    if (bindings.size() < ScriptExecutor.functions.size()) {
      for (String key : ScriptExecutor.functions.keySet()) {
        if (!bindings.containsKey(key)) {
          bindings.put(key, new ScriptFunctionWrapper(null, null));
        }
      }
    }
    // TODO Linking Support
    return bindings;
  }
}
