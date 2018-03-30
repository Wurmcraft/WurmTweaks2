package com.wurmcraft.wurmtweaks.script.support;

import com.rwtema.extrautils2.api.machine.XUMachineCrusher;
import com.rwtema.extrautils2.tile.TileResonator;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;

public class ExtraUtils2 extends ModSupport {

	@Override
	public String getModID () {
		return "extrautils2";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			TileResonator.resonatorRecipes.clear ();
			while (XUMachineCrusher.INSTANCE.recipes_registry.iterator ().hasNext ())
				XUMachineCrusher.INSTANCE.recipes_registry.removeRecipe (XUMachineCrusher.INSTANCE.recipes_registry.iterator ().next ());
		}
	}

	@ScriptFunction
	public void addResonator (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addResonator('<output> <input> <energy>'");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		TileResonator.register (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}

	@ScriptFunction (link = "crushing", linkSize = {2,4})
	public void addXUCrusher (String line) {
		String[] input = verify (line,line.split (" ").length == 2 || line.split (" ").length == 4,"addXUCrusher('<output> <input> | <secOutput> <secOutput%>')");
		isValid (input[0],input[1]);
		if (line.split (" ").length == 2)
			XUMachineCrusher.addRecipe (convertS (input[1]),convertS (input[0]));
		else {
			isValid (input[2]);
			isValid (EnumInputType.FLOATNG,input[3]);
			XUMachineCrusher.addRecipe (convertS (input[1]),convertS (input[0]),convertS (input[2]),convertNF (input[3]));
		}
	}
}
