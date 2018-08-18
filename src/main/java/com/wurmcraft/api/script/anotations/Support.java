package com.wurmcraft.api.script.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  TODO Document
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Support {

  /**
   *
   */
  String modid() default "";

  /**
   *
   */
  String supportDependencies() default "";

  /**
   *
   */
  boolean threaded() default false;

  /**
   *
   */
  byte suppotCode() default 0;

}
