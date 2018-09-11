package com.wurmcraft.api.script.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ScriptFunction {

  String modid() default "";

  boolean threadSafe() default false;

  FunctionType type() default FunctionType.Standard;

  boolean precedence() default false;

  String name() default "";

  String typeData() default "";

  String inputFormat() default "";

  String[] guiVar() default {"wurmtweaks2", "-1"};

  enum FunctionType {
    Custom,
    Linked,
    Standard
  }
}
