package com.wurmcraft.api.script.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add to any method with a empty constructor
 *
 * <p>Called after finishing running scripts (Used by the support to activate anything required
 * before the game has finished loading / reloading)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FinalizeSupport {

  /** ModID required to load load this method */
  String modid() default "";

  /** Event that will cause this even to be called (once the scripts have been loaded) */
  EnumInitType initType() default EnumInitType.POSTINIT;

  /**
   * Called on once of these events
   *
   * @see net.minecraftforge.fml.common.event.FMLPostInitializationEvent
   * @see net.minecraftforge.fml.common.event.FMLServerStartingEvent
   */
  enum EnumInitType {
    POSTINIT,
    SERVER_STARTING
  }
}
