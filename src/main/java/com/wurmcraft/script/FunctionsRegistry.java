package com.wurmcraft.script;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.script.utils.ScriptFunctionWrapper;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import net.minecraftforge.fml.common.Loader;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionsRegistry {
 public static final List<Class<? extends SupportBase>> supportClasses = Collections.synchronizedList(new ArrayList<>());
 public static final NonBlockingHashSet<? super SupportBase> loadedSupport = new NonBlockingHashSet<>();
// private static SimpleBindings bindings;

 public static void register(Class<? extends SupportBase> support) {
  if (support != null && !supportClasses.contains(support))
   supportClasses.add(support);
 }

 public static Bindings createBindings(PrintStream log) {
   SimpleBindings bindings = new SimpleBindings();
   if (supportClasses.size() > 0) {
    supportClasses.forEach(clazz -> {
     SupportBase controller = null;
     try {
      controller = clazz.newInstance();
     } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
     }
     if (Loader.isModLoaded(controller.modid()) || controller.modid().equals("minecraft") || controller.modid().equals("events")) {
      for (Method method : clazz.getDeclaredMethods()) {
       if (method.getAnnotation(ScriptFunction.class) != null) {
        if (method.getAnnotation(ScriptFunction.class).modid().length() > 0 &&
         Loader.isModLoaded(method.getAnnotation(ScriptFunction.class).modid()) ||
         method.getAnnotation(ScriptFunction.class).modid().length() == 0)
        {
         controller.setPrintStream(log);
         controller.init();
         bindings.put(
          method.getName().toLowerCase(),
          new ScriptFunctionWrapper(controller, new StackHelper(true, log), method, log)
         );
         loadedSupport.add(controller);
        }
       }
      }
     }
    });
   }
  return bindings;
 }

 public static void finishSupport() {
  loadedSupport.forEach(support -> {
   SupportBase sb = (SupportBase)support;
   if (Loader.isModLoaded(sb.modid()))
    sb.finishSupport();
  });
  loadedSupport.clear();
 }
}
