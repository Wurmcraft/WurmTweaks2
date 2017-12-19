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
	 Only use to override IModSupport

	 @see IModSupport
	 */
	String modid () default "";

	/**
	 Used For Script Function Names Along With The Method Name
	 */
	String[] aliases () default "";
}
