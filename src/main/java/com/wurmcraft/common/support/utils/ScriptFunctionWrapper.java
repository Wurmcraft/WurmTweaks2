package com.wurmcraft.common.support.utils;

import java.lang.reflect.Method;
import java.util.function.Function;

public class ScriptFunctionWrapper implements Function<String, Void> {

  private final Method method;
  private Object clazz;
  private Converter converter;

  public ScriptFunctionWrapper(Method method, Converter converter) {
    this.method = method;
    if (method != null) {
      try {
        this.clazz = method.getDeclaringClass().newInstance();
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    this.converter = converter;
  }

  @Override
  public Void apply(String s) {
    if (method != null && clazz != null) {
      try {
        method.invoke(clazz, converter, s.split(" "));
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
