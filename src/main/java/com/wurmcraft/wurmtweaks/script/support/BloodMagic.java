package com.wurmcraft.wurmtweaks.script.support;

import WayofTime.bloodmagic.apibutnotreally.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarTier;
import WayofTime.bloodmagic.apibutnotreally.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.apibutnotreally.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.apibutnotreally.registry.TartaricForgeRecipeRegistry;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

public class BloodMagic implements IModSupport {

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
		String[] input = line.split (" ");
		if (input.length >= 6) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				try {
					EnumAltarTier tier = getTier (Integer.parseInt (input[1]));
					try {
						int syphon = Integer.parseInt (input[2]);
						try {
							int comsume = Integer.parseInt (input[3]);
							try {
								int drain = Integer.parseInt (input[4]);
								boolean fillable = Boolean.getBoolean (input[5]);
								ItemStack inputStack = StackHelper.convert (input[6],null);
								AltarRecipeRegistry.registerRecipe (new AltarRecipeRegistry.AltarRecipe (inputStack,output,tier,syphon,comsume,drain,fillable));
							} catch (NumberFormatException e) {
								WurmScript.info ("Invalid Drain Rate '" + input[4] + "'");
							}
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Consume Rate '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Syphon Amount '" + input[2] + "'");
					}
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Altar Tier '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addAltar('<output> <tier> <syphon> <consume> <drain> <fillable> <input>')");
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
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack catalyst = StackHelper.convert (input[1],null);
				if (catalyst != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY)
						AlchemyArrayRecipeRegistry.registerRecipe (inputStack,catalyst,new AlchemyArrayEffectCrafting (output));
					WurmScript.info ("Invalid Input '" + input[2] + "'");
				}
				WurmScript.info ("Invalid Catalyst '" + input[1] + "'");
			}
			WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addAlchemyArray('<output> <catalyst> <input>')");
	}

	public void addSoulForge (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY)
				try {
					double souls = Double.parseDouble (input[1]);
					try {
						double drain = Double.parseDouble (input[2]);
						ItemStack inputStack = StackHelper.convert (input[3],null);
						if (inputStack != ItemStack.EMPTY)
							TartaricForgeRecipeRegistry.registerRecipe (output,souls,drain,inputStack);
						else
							WurmScript.info ("Invalid Input '" + input[3] + "'");
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Drain " + input[2] + "'");
					}
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Souls '" + input[1] + "'");
				}
			else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addSoulForge('<output> <souls> <drain> <input>')");
	}
}
