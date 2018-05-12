package com.wurmcraft.script.utils;

import com.wurmcraft.api.IModSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ScriptFunctionWrapper implements Function <String, Void> {

	private IModSupport clazz;
	private Method method;
	private StackHelper helper;

	public ScriptFunctionWrapper (IModSupport clazz,StackHelper helper,Method method) {
		this.clazz = clazz;
		this.method = method;
		this.helper = helper;
	}

	@Override
	public Void apply (String s) {
		try {
			method.invoke (clazz,helper,s);
		} catch (IllegalAccessException | InvocationTargetException e) {
			int lineNo = -1;
			for(StackTraceElement ste: e.getStackTrace()) {
				if (ste.getClassName().startsWith("jdk.nashorn.internal.scripts.")) {
					lineNo =  ste.getLineNumber();
				}
			}
			System.out.println ("<" + lineNo + ">" + e.getLocalizedMessage ());
		}
		return null;
	}
}
