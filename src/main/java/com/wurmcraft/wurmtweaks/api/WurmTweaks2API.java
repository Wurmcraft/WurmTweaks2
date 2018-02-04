package com.wurmcraft.wurmtweaks.api;

import java.util.ArrayList;
import java.util.List;

public class WurmTweaks2API {

	protected static List <IModSupport> activeControllers = new ArrayList <> ();

	public static void register (IModSupport mod) {
		if (!activeControllers.contains (mod))
			activeControllers.add (mod);
	}
}
