package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.WurmTweaks;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import joptsimple.internal.Strings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinkedRegistry {

	public String[] getLinkedMachinesRecipes (String link,String line) {
		Method[] methods = getLinkedMachines (link);
		if (methods.length > 0)
			return generateLinkRecipes (getLinkInputs (link,line).split (" "),methods);
		return null;
	}

	public Method[] getLinkedMachines (String linkName) {
		List <Method> linkMethods = new ArrayList <> ();
		for (IModSupport support : WurmTweaks.dl.wurmScript.getActiveControllers ()) {
			Method[] supportMethods = support.getClass ().getDeclaredMethods ();
			for (Method method : supportMethods)
				if (method.getAnnotation (ScriptFunction.class) != null && method.getAnnotation (ScriptFunction.class).link ().length () > 0 && method.getAnnotation (ScriptFunction.class).link ().equals (linkName))
					linkMethods.add (method);
		}
		return linkMethods.toArray (new Method[0]);
	}

	private String[] generateLinkRecipes (String[] inputs,Method[] methods) {
		List <String> generatedRecipes = new ArrayList <> ();
		for (Method method : methods) {
			int[] inputSizes = method.getAnnotation (ScriptFunction.class).linkSize ();
			for (int size : inputSizes) {
				if (size <= inputs.length) {
					StringBuilder stringBuilder = new StringBuilder ();
					stringBuilder.append (method.getName ()).append ("('");
					String[] formattedInputs = Arrays.copyOfRange (inputs,0,size);
					stringBuilder.append (Strings.join (formattedInputs," ")).append ("')");
					generatedRecipes.add (stringBuilder.toString ());
				}
			}
		}
		return generatedRecipes.toArray (new String[0]);
	}

	private String getLinkInputs (String link,String line) {
		return line.replaceAll ("\\('","").replaceAll ("'\\)","").replaceAll ("add" + link.substring (0,1).toUpperCase () + link.substring (1,link.length ()).toLowerCase (),"");
	}
}
