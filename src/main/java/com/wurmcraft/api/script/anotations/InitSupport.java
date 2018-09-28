package com.wurmcraft.api.script.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add to any method with a empty constructor
 *
 * <p>Called to initialize any data required by the scripts prior to running the game
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitSupport {

  String modid() default "";

  EnumInitType initType() default EnumInitType.INIT;

  enum EnumInitType {
    PREINIT,
    INIT
  }
}
