package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.api.IModSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ScriptFunctionWrapper implements Function <String, Void> {

	private IModSupport clazz;
	private Method method;

	public ScriptFunctionWrapper (IModSupport clazz,Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	@Override
	public Void apply (String s) {
		try {
			method.invoke (clazz,s);
		} catch (IllegalAccessException | InvocationTargetException e) {
		e.getCause ().printStackTrace ();
		}
		return null;
	}
}
