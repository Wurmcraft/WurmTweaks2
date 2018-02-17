package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraft.block.Block;

public class ActuallyAdditions extends ModSupport {

	@Override
	public String getModID () {
		return "actuallyadditions";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			ActuallyAdditionsAPI.CRUSHER_RECIPES.clear ();
			ActuallyAdditionsAPI.TREASURE_CHEST_LOOT.clear ();
			ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES.clear ();
			ActuallyAdditionsAPI.EMPOWERER_RECIPES.clear ();
			ActuallyAdditionsAPI.COMPOST_RECIPES.clear ();
			ActuallyAdditionsAPI.STONE_ORES.clear ();
			ActuallyAdditionsAPI.NETHERRACK_ORES.clear ();
		}
	}

	@ScriptFunction
	public void addEmpowerer (String line) {
		String[] input = verify (line,line.split (" ").length == 8,"addEmpowerer('<output> <inputCenter> <input> <input2> <input3> <input4> <energyPerStand> <time>')");
		isValid (input[0],input[1],input[2],input[3],input[4],input[5]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		ActuallyAdditionsAPI.addEmpowererRecipe (convertS (input[1]),convertS (input[0]),convertS (input[2]),convertS (input[3]),convertS (input[4]),convertS (input[4]),convertNI (input[5]),convertNI (input[6]),new float[] {1F,91F / 255F,76F / 255F});
	}

	@ScriptFunction
	public void addReconstructor (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addReconstructor('<output> <input> <energy>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		ActuallyAdditionsAPI.addReconstructorLensConversionRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}

	@ScriptFunction (link = "crushing", linkSize = {4})
	public void addAACrusher (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addAACrusher('<output> <input> <secOutput> <secOutput%>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		ActuallyAdditionsAPI.addCrusherRecipe (convertS (input[1]),convertS (input[0]),convertS (input[2]),convertNI (input[3]));
	}


	@ScriptFunction (link = "laser", linkSize = {2})
	public void addMiningLensStone (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addMiningLensStone('<oreDict> <weight>')");
		isValid (EnumInputType.STRING,input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		ActuallyAdditionsAPI.addMiningLensStoneOre (input[0],convertNI (input[1]));
	}

	@ScriptFunction
	public void addMiningLensNether (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addMiningLensNether('<oreDict> <weight>')");
		isValid (EnumInputType.STRING,input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		ActuallyAdditionsAPI.addMiningLensNetherOre (input[0],convertNI (input[1]));
	}

	@ScriptFunction
	public void addComposter (String line) {
		String[] input = verify (line,line.split (" ").length == 2 || line.split (" ").length == 4,"addComposter('<output> <input> | <outputDisplay> <inputDisplay')");
		isValid (input[0],input[1]);
		if (line.split (" ").length == 2) {
			ActuallyAdditionsAPI.addCompostRecipe (convertS (input[1]),Block.getBlockFromItem (convertS (input[1]).getItem ()),convertS (input[0]),Block.getBlockFromItem (convertS (input[0]).getItem ()));
		} else {
			isValid (input[2],input[3]);
			ActuallyAdditionsAPI.addCompostRecipe (convertS (input[1]),Block.getBlockFromItem (convertS (input[2]).getItem ()),convertS (input[0]),Block.getBlockFromItem (convertS (input[3]).getItem ()));
		}
	}
}
