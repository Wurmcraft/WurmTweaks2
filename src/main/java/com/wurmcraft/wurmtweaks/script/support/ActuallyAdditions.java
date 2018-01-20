package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ActuallyAdditions implements IModSupport {

	@Override
	public String getModID () {
		return "actuallyadditions";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllRecipes) {
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
		String[] input = line.split (" ");
		if (input.length == 8) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputCenter = StackHelper.convert (input[1],null);
				if (inputCenter != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						ItemStack input2 = StackHelper.convert (input[3],null);
						if (input2 != ItemStack.EMPTY) {
							ItemStack input3 = StackHelper.convert (input[4],null);
							if (input3 != ItemStack.EMPTY) {
								ItemStack input4 = StackHelper.convert (input[5],null);
								if (input4 != ItemStack.EMPTY) {
									try {
										int energy = Integer.parseInt (input[6]);
										try {
											int time = Integer.parseInt (input[7]);
											ActuallyAdditionsAPI.addEmpowererRecipe (inputCenter,output,inputStack2,input3,input3,input4,energy,time,new float[] {1F,91F / 255F,76F / 255F});
										} catch (NumberFormatException f) {
											WurmScript.info ("Invalid Number '" + input[7] + "'");
										}
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} else
									WurmScript.info ("Invalid Stack '" + input[5] + "'");
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addEmpowerer('<output> <inputCenter> <input> <input2> <input3> <input4> <energyPerStand> <time>')");
	}

	@ScriptFunction
	public void addReconstructor (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						ActuallyAdditionsAPI.addReconstructorLensConversionRecipe (inputStack,output,energy);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addReconstructor('<output> <input> <energy>')");
	}

	// TODO Possible Cross- Mod Support
	// 'Macerator Registry'?
	@ScriptFunction
	public void addAACrusher (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack secOutput = StackHelper.convert (input[2],null);
					if (secOutput != ItemStack.EMPTY) {
						try {
							int chance = Integer.parseInt (input[3]);
							ActuallyAdditionsAPI.addCrusherRecipe (inputStack,output,secOutput,chance);
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAACrusher('<output> <input> <secendaryOutput> <output%>')");
	}


	// TODO Possible Cross- Mod Support
	// 'Ore-Gen Registry'?
	@ScriptFunction
	public void addMiningLensStone (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			String oreDict = input[0];
			if (OreDictionary.doesOreNameExist (oreDict)) {
				try {
					int weight = Integer.parseInt (input[1]);
					ActuallyAdditionsAPI.addMiningLensStoneOre (oreDict,weight);
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Ore Dict Entry '" + input[0] + "'");
		} else
			WurmScript.info ("addMiningLensStone('<oreDict> <weight>'))");
	}

	// TODO Possible Cross- Mod Support
	// 'Ore-Gen Registry'?
	@ScriptFunction
	public void addMiningLensNether (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			String oreDict = input[0];
			if (OreDictionary.doesOreNameExist (oreDict)) {
				try {
					int weight = Integer.parseInt (input[1]);
					ActuallyAdditionsAPI.addMiningLensNetherOre (oreDict,weight);
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Ore Dict Entry '" + input[0] + "'");
		} else
			WurmScript.info ("addMiningLensNether('<oreDict> <weight>'))");
	}

	@ScriptFunction
	public void addComposter (String line) {
		String[] input = line.split (" ");
		if (input.length == 2 || input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY && input.length == 4 || input.length == 2 && output != ItemStack.EMPTY && Block.getBlockFromItem (output.getItem ()) != Blocks.AIR) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY && input.length == 4 || input.length == 2 && inputStack != ItemStack.EMPTY && Block.getBlockFromItem (inputStack.getItem ()) != Blocks.AIR) {
					if (input.length == 2)
						ActuallyAdditionsAPI.addCompostRecipe (inputStack,Block.getBlockFromItem (inputStack.getItem ()),output,Block.getBlockFromItem (output.getItem ()));
					else {
						ItemStack displayOutput = StackHelper.convert (input[2],null);
						if (displayOutput != ItemStack.EMPTY && Block.getBlockFromItem (displayOutput.getItem ()) != Blocks.AIR) {
							ItemStack displayInput = StackHelper.convert (input[3],null);
							if (displayInput != ItemStack.EMPTY && Block.getBlockFromItem (displayInput.getItem ()) != Blocks.AIR) {
								ActuallyAdditionsAPI.addCompostRecipe (inputStack,Block.getBlockFromItem (displayInput.getItem ()),output,Block.getBlockFromItem (displayOutput.getItem ()));
							} else
								WurmScript.info ("Invalid Block '" + input[3] + "'");
						} else
							WurmScript.info ("Invalid Block '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack/Block '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack/Block '" + input[0] + "'");
		} else
			WurmScript.info ("addComposter('<output> <input> | <displayOutput> <displayInput>')");
	}
}
