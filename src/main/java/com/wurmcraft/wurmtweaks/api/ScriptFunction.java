package com.wurmcraft.wurmtweaks.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Adds a Function To WurmScript
 Must be used within a Registed IModSupport

 @see IModSupport */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface ScriptFunction {

	/**
	 Allows for Machine Recipes to be grouped based on there function
	 */
	String link () default "";

	/**
	 Used by link to determine the recipe length
	 1 for each set of possible input lengths
	 */
	int[] linkSize () default 0;

	/**
	 Only use to override IModSupport

	 @see IModSupport
	 */
	String modid () default "";

	/**
	 Used For Script Function Names Along With The Method Name
	 */
	String[] aliases () default "";
}
