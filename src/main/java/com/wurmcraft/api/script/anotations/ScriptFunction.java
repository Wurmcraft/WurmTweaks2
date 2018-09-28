package com.wurmcraft.api.script.anotations;

import com.wurmcraft.common.support.utils.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply to any method with the parameters (Converter, String[])
 *
 * @see com.wurmcraft.common.support.Minecraft
 * @see com.wurmcraft.common.support.Events
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ScriptFunction {

  /** ModID required for this function to be loaded */
  String modid() default "";

  /**
   * How this function should be treated
   *
   * @see com.wurmcraft.common.support.Minecraft#addShaped(Converter, String[])
   */
  FunctionType type() default FunctionType.Standard;

  /**
   * True will cause this function to be run before the rest of the scripts.
   *
   * @see com.wurmcraft.common.support.Minecraft#addOreEntry(Converter, String[])
   */
  boolean precedence() default false;

  /** Name Override for this function, Default is the method name */
  String name() default "";

  /**
   * Data storage for use within an custom FunctionType
   *
   * @see ScriptFunction#type()
   */
  String typeData() default "";

  /**
   * Types of input required by this function
   *
   * @see com.wurmcraft.common.support.Minecraft#addFurnace(Converter, String[])
   */
  String inputFormat() default "";

  /** Currently Unused / Supported */
  String[] guiVar() default {"wurmtweaks2", "-1"};

  /**
   * How a function is run
   *
   * <p>Linked = Linked to another set of functions that are all called based on there combined name
   * Standard = Called once its name is called
   */
  enum FunctionType {
    Custom,
    Linked,
    Standard
  }
}
