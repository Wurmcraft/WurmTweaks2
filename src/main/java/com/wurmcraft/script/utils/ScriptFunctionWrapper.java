package com.wurmcraft.script.utils;

import com.wurmcraft.api.IModSupport;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ScriptFunctionWrapper implements Function<String, Void> {

 private final IModSupport clazz;
 private final Method method;
 private final StackHelper helper;
 private final PrintStream ps;

 public ScriptFunctionWrapper(IModSupport clazz, StackHelper helper, Method method, PrintStream ps) {
  this.clazz = clazz;
  this.method = method;
  this.helper = helper;
  this.ps = ps;
 }

 @Override
 public Void apply(String s) {
  method.setAccessible(true);
  try {
   method.invoke(clazz, helper, s);
  } catch (IllegalAccessException | InvocationTargetException e) {
   ps.println("Encountered exception applying: ");
   ps.println("String: " + s);
   ps.println("Method: " + method.toString());
   e.printStackTrace(ps);
  }
  return null;
 }
}
