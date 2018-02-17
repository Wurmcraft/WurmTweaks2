package com.wurmcraft.wurmtweaks.script.support;

import cofh.thermalexpansion.util.managers.machine.*;
import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;

import java.util.Arrays;

public class ThermalExpansion extends ModSupport {

	@Override
	public String getModID () {
		return "thermalexpansion";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			for (FurnaceManager.FurnaceRecipe recipe : FurnaceManager.getRecipeList ())
				FurnaceManager.removeRecipe (recipe.getInput ());
			for (PulverizerManager.PulverizerRecipe recipe : PulverizerManager.getRecipeList ())
				PulverizerManager.removeRecipe (recipe.getInput ());
			for (SmelterManager.SmelterRecipe recipe : SmelterManager.getRecipeList ())
				SmelterManager.removeRecipe (recipe.getPrimaryInput (),recipe.getSecondaryInput ());
			for (CompactorManager.Mode mode : CompactorManager.Mode.values ())
				for (CompactorManager.CompactorRecipe recipe : CompactorManager.getRecipeList (mode))
					CompactorManager.removeRecipe (recipe.getInput (),mode);
			for (CentrifugeManager.CentrifugeRecipe recipe : CentrifugeManager.getRecipeList ())
				CentrifugeManager.removeRecipe (recipe.getInput ());
		}
	}

	@ScriptFunction (linkSize = 3, link = "furnace")
	public void addRedstoneFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addRedstoneFurnace('<output> <input> <energy>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		FurnaceManager.addRecipe (convertNI (input[2]),convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction (link = "crushing", linkSize = {3,5})
	public void addPulverizer (String line) {
		String[] input = verify (line,line.split (" ").length == 3 || line.split (" ").length == 5,"addPulverizer('<output> <input> <energy> | <secOutput> <secOutput%>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		if (line.split (" ").length == 3)
			PulverizerManager.addRecipe (convertNI (input[2]),convertS (input[1]),convertS (input[0]));
		else {
			isValid (input[3]);
			isValid (EnumInputType.INTEGER,input[4]);
			PulverizerManager.addRecipe (convertNI (input[2]),convertS (input[1]),convertS (input[0]),convertS (input[3]),convertNI (input[4]));
		}
	}

	@ScriptFunction (linkSize = 3, link = "saw")
	public void addTESawmill (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addTESawmill('<output> <input> <energy>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		SawmillManager.addRecipe (convertNI (input[2]),convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addSmelter (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addSmelter('<output> <inputA> <inputB> <energy')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		SmelterManager.addAlloyRecipe (convertNI (input[3]),convertS (input[1]),convertS (input[2]),convertS (input[0]));
	}

	@ScriptFunction
	public void addCompactor (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addCompactor('<output> <input> <energy> <mode>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		CompactorManager.Mode mode = getMode (input[3]);
		Preconditions.checkArgument (mode != null,"Invalid Mode %s",input[3]);
		CompactorManager.addRecipe (convertNI (input[2]),convertS (input[1]),convertS (input[0]),mode);
	}

	@ScriptFunction
	public void addMagmaCrucible (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addMagmaCrucible('<*output> <input> <energy>')");
		isValid (EnumInputType.FLUID,input[0]);
		isValid (input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		CrucibleManager.addRecipe (convertNI (input[2]),convertS (input[1]),convertF (input[0]));
	}

	@ScriptFunction
	public void addCenterfuge (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addCenterfuge('<output> <output2> <output3> <output4> <input> <energy> <*output5>");
		isValid (input[0],input[1],input[2],input[3],input[4]);
		isValid (EnumInputType.INTEGER,input[5]);
		isValid (EnumInputType.FLUID,input[6]);
		CentrifugeManager.addRecipe (convertNI (input[5]),convertS (input[4]),Arrays.asList (convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3])),convertF (input[6]));
	}

	private CompactorManager.Mode getMode (String mode) {
		if (mode.matches ("[pP]ress"))
			return CompactorManager.Mode.PRESS;
		else if (mode.matches ("[sS]torage"))
			return CompactorManager.Mode.STORAGE;
		else if (mode.matches ("[mM]int"))
			return CompactorManager.Mode.MINT;
		else if (mode.matches ("[gG]ear"))
			return CompactorManager.Mode.GEAR;
		return null;
	}
}
