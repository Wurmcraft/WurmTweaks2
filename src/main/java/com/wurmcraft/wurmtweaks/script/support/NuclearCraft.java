package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import nc.recipe.NCRecipes;

public class NuclearCraft extends ModSupport {

	@Override
	public String getModID () {
		return "nuclearcraft";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			NCRecipes.PRESSURIZER_RECIPES.recipes.clear ();
			NCRecipes.ISOTOPE_SEPARATOR_RECIPES.recipes.clear ();
			NCRecipes.MANUFACTORY_RECIPES.recipes.clear ();
			NCRecipes.ALLOY_FURNACE_RECIPES.recipes.clear ();
			NCRecipes.CHEMICAL_REACTOR_RECIPES.recipes.clear ();
			NCRecipes.SUPERCOOLER_RECIPES.recipes.clear ();
			NCRecipes.INFUSER_RECIPES.recipes.clear ();
			NCRecipes.INGOT_FORMER_RECIPES.recipes.clear ();
			NCRecipes.MELTER_RECIPES.recipes.clear ();
		}
	}

	@ScriptFunction
	public void addManufactory (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addManufactory('<output> <input> <time>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		NCRecipes.MANUFACTORY_RECIPES.addRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}

	@ScriptFunction
	public void addIsotopeSeparator (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addIsotopeSeparator('<output> <output2> <input> <time')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		NCRecipes.ISOTOPE_SEPARATOR_RECIPES.addRecipe (convertS (input[2]),convertS (input[0]),convertS (input[1]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addAlloyFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addAlloyFurnace('<output> <input> <input2> <time>')");
		isValid (input[0],input[1],input[2]);
		isValid (input[3]);
		NCRecipes.ISOTOPE_SEPARATOR_RECIPES.addRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addSupercooler (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addSupercooler('<*output> <*input> <time>')");
		isValid (EnumInputType.FLUID,input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		NCRecipes.SUPERCOOLER_RECIPES.addRecipe (convertF (input[1]),convertF (input[0]),convertNI (input[2]));
	}

	@ScriptFunction
	public void addPressurizer (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addPressurizer('<output> <input> <time>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		NCRecipes.PRESSURIZER_RECIPES.addRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}
}
