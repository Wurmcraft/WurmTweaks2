package com.wurmcraft.wurmtweaks.script.support;

import blusunrize.immersiveengineering.api.crafting.*;
import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ImmersiveEngineering extends ModSupport {

	@Override
	public String getModID () {
		return "immersiveengineering";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			AlloyRecipe.recipeList.clear ();
			ArcFurnaceRecipe.recipeList.clear ();
			BlastFurnaceRecipe.recipeList.clear ();
			CokeOvenRecipe.recipeList.clear ();
			CrusherRecipe.recipeList.clear ();
			FermenterRecipe.recipeList.clear ();
			MetalPressRecipe.recipeList.clear ();
			RefineryRecipe.recipeList.clear ();
			SqueezerRecipe.recipeList.clear ();
		}
	}

	@ScriptFunction
	public void addAlloyRecipe (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addAlloyRecipe('<output> <input> <input2> <time>");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		AlloyRecipe.addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addArcFurnace (String line) {
		String[] input = verify (line,line.split (" ").length >= 4,"addArcFurnace('<output> <slag> <input> <time> <energy> | <additives'");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4]);
		List <ItemStack> additives = new ArrayList <> ();
		if (input.length > 5) {
			for (int index = 5; index < input.length; index++) {
				isValid (input[index]);
				additives.add (convertS (input[index]));
			}
			ArcFurnaceRecipe.addRecipe (convertS (input[0]),convertS (input[2]),convertS (input[1]),convertNI (input[3]),convertNI (input[4]),additives);
		} else
			ArcFurnaceRecipe.addRecipe (convertS (input[0]),convertS (input[2]),convertS (input[1]),convertNI (input[3]),convertNI (input[4]));
	}

	@ScriptFunction
	public void addBlastFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addBlastFurnace(<output> <slag> <input> <time>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		BlastFurnaceRecipe.addRecipe (convertS (input[0]),convertS (input[2]),convertNI (input[3]),convertS (input[1]));
	}

	@ScriptFunction
	public void addBlastFuel (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addBlastFuel('<stack> <time>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		BlastFurnaceRecipe.addBlastFuel (convertS (input[0]),convertNI (input[1]));
	}

	@ScriptFunction
	public void addCokeOven (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addCokeOven('<output> <input> <creosote> <time>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		CokeOvenRecipe.addRecipe (convertS (input[0]),convertS (input[1]),convertNI (input[3]),convertNI (input[2]));
	}

	@ScriptFunction (link = "crushing", linkSize = {3,5})
	public void addCrusher (String line) {
		String[] input = verify (line,line.split (" ").length == 3 || line.split (" ").length == 5,"addCrusher('<output> <input> <energy> | <secOutput> <secOutput%>");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		if (line.split (" ").length == 3)
			CrusherRecipe.addRecipe (convertS (input[0]),convertS (input[1]),convertNI (input[2]));
		else if (line.split (" ").length == 5) {
			isValid (input[3]);
			isValid (EnumInputType.FLOATNG,input[4]);
			CrusherRecipe recipe = new CrusherRecipe (convertS (input[0]),convertS (input[1]),convertNI (input[2]));
			recipe.addToSecondaryOutput (convertS (input[3]),convertNF (input[4]));
			CrusherRecipe.recipeList.add (recipe);
		}
	}

	@ScriptFunction
	public void addFermenter (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addFermenter('<output> <*output> <input> <energy')");
		isValid (input[0],input[2]);
		isValid (EnumInputType.FLUID,input[1]);
		isValid (EnumInputType.INTEGER,input[3]);
		FermenterRecipe.addRecipe (convertF (input[1]),convertS (input[0]),convertS (input[2]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addMetalPress (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addMetalPress(<output> <input> <mold> <time>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		MetalPressRecipe.addRecipe (convertS (input[0]),convertS (input[2]),convertS (input[3]),convertNI (input[1]));
	}

	@ScriptFunction
	public void addRefinery (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addRefinery('<*output> <*input> <*input> <energy>')");
		isValid (EnumInputType.FLUID,input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		RefineryRecipe.addRecipe (convertF (input[0]),convertF (input[1]),convertF (input[2]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addSqueezer (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addSqueezer('<output> <*output> <input> <energy>')");
		isValid (input[0],input[2]);
		isValid (EnumInputType.FLUID,input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		SqueezerRecipe.addRecipe (convertF (input[1]),convertS (input[0]),convertS (input[1]),convertNI (input[2]));
	}
}
