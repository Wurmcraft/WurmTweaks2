package com.wurmcraft.api.script.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically Registers ModSupport
 * Automatically adds modid to any ScriptFunction within a class with this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Support {

  /**
   * ModID required for this to be registered and loaded
   */
  String modid() default "";

  /**
   * Additional dependencies this support may need modid separated by an semicolon or space
   *
   * IE: minecraft;wurmtweaks;tconstruct
   * */
  String supportDependencies() default "";

  /**
   * Placeholder for later feature
   * */
  byte suppotCode() default 0;
}
