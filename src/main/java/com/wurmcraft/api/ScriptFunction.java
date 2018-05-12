package com.wurmcraft.api;

import com.wurmcraft.script.WurmScript;
import net.minecraftforge.fml.common.Mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Used to create a function for use within WurmScript

 Required for creating a WurmScript function

 @see WurmScript
 @see IModSupport */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.METHOD)
public @interface ScriptFunction {
	/**
	 TODO
	 - ModID Override
	 - Linked Machines
	 */

}
