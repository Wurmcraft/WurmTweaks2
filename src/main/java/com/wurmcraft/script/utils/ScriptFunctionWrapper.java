package com.wurmcraft.script.utils;

import com.wurmcraft.api.IModSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ScriptFunctionWrapper implements Function<String, Void> {

 private IModSupport clazz;
 private Method method;
 private StackHelper helper;

 public ScriptFunctionWrapper(IModSupport clazz, StackHelper helper, Method method) {
  this.clazz = clazz;
  this.method = method;
  this.helper = helper;
 }

 @Override
 public Void apply(String s) {
  method.setAccessible(true);
  try {
   method.invoke(clazz, helper, s);
  } catch (IllegalAccessException | InvocationTargetException e) {
   System.err.println("Encountered exception applying: ");
   System.err.println("String: " + s);
   System.err.println("Method: " + method.toString());
   e.printStackTrace();
  }
  return null;
 }
}
