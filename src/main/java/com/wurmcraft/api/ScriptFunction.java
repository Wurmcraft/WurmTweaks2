package com.wurmcraft.api;

import com.wurmcraft.script.WurmScript;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**Used to create a function for use within WurmScript
 * <p>
 * Required for creating a WurmScript function
 *
 * @see WurmScript
 * @see IModSupport
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ScriptFunction {
    /**
     Additional modid to check before this method is valid
     */
    String modid () default "";

    // TODO Implement LinkedRegistry
    String[] link () default "";
}
