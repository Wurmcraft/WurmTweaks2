package com.wurmcraft.wurmtweaks.script.support;

import cofh.thermalexpansion.util.managers.machine.*;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class ThermalExpansion implements IModSupport {

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
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						FurnaceManager.addRecipe (energy * 3000,inputStack,output);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Energy Amount '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addRedstoneFurnace('<output> <input> <energy * 3000>')");
	}

	@ScriptFunction (link = "crushing", linkSize = {3,5})
	public void addPulverizer (String line) {
		String[] input = line.split (" ");
		if (input.length == 3 | input.length == 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						if (input.length == 3) {
							PulverizerManager.addRecipe (energy,inputStack,output);
						} else {
							ItemStack secOutput = StackHelper.convert (input[3],null);
							if (secOutput != ItemStack.EMPTY) {
								try {
									int chance = Integer.parseInt (input[4]);
									PulverizerManager.addRecipe (energy,inputStack,output,secOutput,chance);
								} catch (NumberFormatException e) {
									WurmScript.info ("Invalid Number '" + input[4] + "'");
								}
							} else
								WurmScript.info ("Invalid Secendary Output '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addPulverizer('<output> <input> <energy> | <secOutput> <secOutput%>')");
	}

	@ScriptFunction (linkSize = 3, link = "saw")
	public void addTESawmill (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						SawmillManager.addRecipe (energy,inputStack,output);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Energy Amount '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addTESawmill('<output> <input> <energy>')");
	}

	@ScriptFunction
	public void addSmelter (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						try {
							int energy = Integer.parseInt (input[3]);
							SmelterManager.addAlloyRecipe (energy,inputStack,inputStack2,output);
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Input 2 '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addSmelter('<output> <input> <input2> <energy>')");
	}

	@ScriptFunction
	public void addCompactor (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						CompactorManager.Mode mode = getMode (input[3]);
						if (mode != null)
							CompactorManager.addRecipe (energy,inputStack,output,mode);
						else
							WurmScript.info ("Invalid Mode '" + input[3] + "'");
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCompactor('<output> <input> <energy> <mode>')");
	}

	@ScriptFunction
	public void addMagmaCrucible (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			FluidStack output = StackHelper.convertToFluid (input[0]);
			if (output != null) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						CrucibleManager.addRecipe (energy,inputStack,output);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Fluid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addMagmaCrucible('<*output> <input> <energy>')");
	}

	@ScriptFunction
	public void addCenterfuge (String line) {
		String[] input = line.split (" ");
		if (input.length == 7) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack output3 = StackHelper.convert (input[2],null);
					if (output3 != ItemStack.EMPTY) {
						ItemStack output4 = StackHelper.convert (input[3],null);
						if (output4 != ItemStack.EMPTY) {
							FluidStack outputFluid = StackHelper.convertToFluid (input[4]);
							if (outputFluid != null) {
								ItemStack inputStack = StackHelper.convert (input[5],null);
								if (inputStack != ItemStack.EMPTY) {
									try {
										int energy = Integer.parseInt (input[6]);
										CentrifugeManager.addRecipe (energy,inputStack,Arrays.asList (output,output2,output3,output4),outputFluid);
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} else
									WurmScript.info ("Invalid Input '" + input[5] + "'");
							} else
								WurmScript.info ("Invalid Fluid '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Output 4'" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Output 3'" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Output2 '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCenterfuge('<output> <output2> <output3> <output4> <*output> <input> <energy>')");
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
