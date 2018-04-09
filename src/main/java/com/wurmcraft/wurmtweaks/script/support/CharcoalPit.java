package com.wurmcraft.wurmtweaks.script.support;

import charcoalPit.crafting.OreSmeltingRecipes;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;

public class CharcoalPit extends ModSupport {

	@Override
	public String getModID () {
		return "charcoal_pit";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes)
			OreSmeltingRecipes.alloyRecipes.clear ();
	}

	@ScriptFunction
	public void addBloomery (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addBloomery('<output> <input')");
		isValid (input[0],input[1]);
		OreSmeltingRecipes.addAlloyRecipe (new OreSmeltingRecipes.AlloyRecipe (convertS (input[0]),convertS (input[0]).getCount (),true,true,convertS (input[1])));
	}
}
