package com.wurmcraft.common.support.utils;

import java.lang.reflect.Method;
import java.util.function.Function;

public class ScriptFunctionWrapper implements Function<String, Void> {

  private final Method method;
  private Object clazz;

  public ScriptFunctionWrapper(Method method) {
    this.method = method;
    try {
      this.clazz = method.getDeclaringClass().newInstance();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public Void apply(String s) {
    if (method != null && clazz != null) {
      try {
        method.invoke(clazz, s);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
