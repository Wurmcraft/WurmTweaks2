package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;

public class AstralSorcery extends ModSupport {

	@Override
	public String getModID () {
		return "astralsorcery";
	}

	@Override
	public void init () {
		//		Currently Disabled
		//		if (ConfigHandler.removeAllMachineRecipes) {
		//			InfusionRecipeRegistry.recipes.clear ();
		//			InfusionRecipeRegistry.mtRecipes.clear ();
		//			AltarRecipeRegistry.recipes.clear ();
		//			AltarRecipeRegistry.mtRecipes.clear ();
		//		}
	}

	@ScriptFunction
	public void addBasicInfusion (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addBasicInfusion('<output> <input>')");
		isValid (input[0],input[1]);
		InfusionRecipeRegistry.registerBasicInfusion (convertS (input[0]),convertS (input[1]));
	}

	@ScriptFunction
	public void addSlowInfusion (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addSlowInfusion('<output> <input>')");
		isValid (input[0],input[1]);
		InfusionRecipeRegistry.registerLowConsumptionInfusion (convertS (input[0]),convertS (input[1]));
	}
}
