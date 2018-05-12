package com.wurmcraft.script;

import com.wurmcraft.api.IModSupport;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.script.utils.ScriptFunctionWrapper;
import com.wurmcraft.script.utils.StackHelper;
import net.minecraftforge.fml.common.Loader;
import sun.rmi.log.LogHandler;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionsRegistry {

	public static List <IModSupport> loadedSupport = Collections.synchronizedList (new ArrayList <> ());
	private static SimpleBindings bindings;

	public static void register (IModSupport support) {
		if (support != null && !loadedSupport.contains (support))
			loadedSupport.add (support);
	}

	public static List <IModSupport> getSupport () {
		return loadedSupport;
	}

	public static Bindings createBindings () {
		if (bindings != null)
			return bindings;
		else {
			bindings = new SimpleBindings ();
			if (loadedSupport.size () > 0) {
				for (IModSupport controller : loadedSupport)
					if (Loader.isModLoaded (controller.modid ()) || controller.modid ().equals ("minecraft") || controller.modid ().equals ("events")) {
						controller.init ();
						Method[] methods = controller.getClass ().getDeclaredMethods ();
						for (Method method : methods)
							if (method.getAnnotation (ScriptFunction.class) != null) {
								bindings.put (method.getName ().toLowerCase (),new ScriptFunctionWrapper (controller,new StackHelper (Thread.currentThread (),true),method));
								bindings.put (method.getName (),new ScriptFunctionWrapper (controller,new StackHelper (Thread.currentThread (),true),method));
							}
					}
			}
			return bindings;
		}
	}

	public static void init () {
		createBindings ();
		for (IModSupport support : loadedSupport)
			if (Loader.isModLoaded (support.modid ()))
				support.init ();
	}

	public static void finishSupport () {
		bindings = null;
		for (IModSupport support : loadedSupport)
			if (Loader.isModLoaded (support.modid ()))
				support.finishSupport ();
	}

}
