package com.wurmcraft.wurmtweaks.script.support;

import WayofTime.bloodmagic.apibutnotreally.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarTier;
import WayofTime.bloodmagic.apibutnotreally.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.apibutnotreally.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.apibutnotreally.registry.TartaricForgeRecipeRegistry;
import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;

public class BloodMagic extends ModSupport {

	@Override
	public String getModID () {
		return "bloodmagic";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes)
			for (AltarRecipeRegistry.AltarRecipe recipe : AltarRecipeRegistry.getRecipes ().values ())
				AltarRecipeRegistry.removeRecipe (recipe);
	}

	@ScriptFunction
	public void addAltar (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addAltar('<output> <input> <tier> <syphon> <consume> <drain> <fillable>");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3],input[4],input[5]);
		EnumAltarTier tier = getTier (convertNI (input[2]));
		Preconditions.checkNotNull (tier);
		isValid (EnumInputType.BOOLEAN,input[6]);
		AltarRecipeRegistry.registerRecipe (new AltarRecipeRegistry.AltarRecipe (convertS (input[1]),convertS (input[0]),tier,convertNI (input[3]),convertNI (input[4]),convertNI (input[5]),convertB (input[6])));
	}

	private EnumAltarTier getTier (int tier) {
		switch (tier) {
			case (0):
			case (1):
				return EnumAltarTier.ONE;
			case (2):
				return EnumAltarTier.TWO;
			case (3):
				return EnumAltarTier.THREE;
			case (4):
				return EnumAltarTier.FOUR;
			case (5):
				return EnumAltarTier.FIVE;
			case (6):
				return EnumAltarTier.SIX;
			default:
				return null;
		}
	}

	@ScriptFunction
	public void addAlchemyArray (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addAlchemyArray('<output> <catalyst> <input')");
		AlchemyArrayRecipeRegistry.registerRecipe (convertS (input[2]),convertS (input[1]),new AlchemyArrayEffectCrafting (convertS (input[0])));
	}

	public void addSoulForge (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addSoulForge('<output> <input> <souls> <drain>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		TartaricForgeRecipeRegistry.registerRecipe (convertS (input[0]),convertNF (input[2]),convertNF (input[3]),convertS (input[1]));
	}
}
