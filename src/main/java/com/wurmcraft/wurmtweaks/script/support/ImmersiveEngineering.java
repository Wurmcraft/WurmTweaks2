package com.wurmcraft.wurmtweaks.script.support;

import blusunrize.immersiveengineering.api.crafting.*;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ImmersiveEngineering implements IModSupport {

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
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack input1 = StackHelper.convert (input[1],null);
				if (input1 != ItemStack.EMPTY) {
					ItemStack input2 = StackHelper.convert (input[2],null);
					if (input2 != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							AlloyRecipe.addRecipe (output,input1,input2,time);
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[0] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[0] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAlloyRecipe('<output> <input1> <input2> <time>')");
	}

	@ScriptFunction
	public void addArcFurnace (String line) {
		String[] input = line.split (" ");
		if (input.length >= 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack slag = StackHelper.convert (input[2],null);
					if (slag != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							try {
								int energyTick = Integer.parseInt (input[4]);
								List <ItemStack> additives = new ArrayList <> ();
								if (input.length > 5)
									for (int index = 6; index < input.length; index++)
										if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
											additives.add (StackHelper.convert (input[index],null));
								ArcFurnaceRecipe.addRecipe (output,input,slag,time,energyTick,additives);
							} catch (NumberFormatException f) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[0] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[0] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addArcFurnace('<output> <input> <slag> <time> <energyTick> <additives>...')");
	}

	@ScriptFunction
	public void addBlastFurnace (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack slagStack = StackHelper.convert (input[2],null);
					if (slagStack != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							BlastFurnaceRecipe.addRecipe (output,inputStack,time,slagStack);
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
			WurmScript.info ("addBlastFurnace('<output> <input> <slag> <time>')");
	}

	@ScriptFunction
	public void addBlastFuel (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack fuel = StackHelper.convert (input[0],null);
			if (fuel != ItemStack.EMPTY) {
				try {
					int time = Integer.parseInt (input[1]);
					if (time > 0)
						BlastFurnaceRecipe.addBlastFuel (fuel,time);
					else
						WurmScript.info ("Number Must Be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addBlastFuel('<output> <time>')");
	}

	@ScriptFunction
	public void addCokeOven (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						try {
							int creosote = Integer.parseInt (input[3]);
							CokeOvenRecipe.addRecipe (output,inputStack,time,creosote);
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addCokeOven('<output> <input> <time> <creosote>");
	}

	// TODO Possible Cross- Mod Support
	// 'Macerator Registry'?
	@ScriptFunction
	public void addCrusher (String line) {
		String[] input = line.split (" ");
		if (input.length >= 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						if (energy > 0) {
							if (input.length == 3)
								CrusherRecipe.addRecipe (output,input,energy);
							else if (input.length == 5) {
								ItemStack second = StackHelper.convert (input[3],null);
								if (second != ItemStack.EMPTY) {
									try {
										CrusherRecipe recipe = CrusherRecipe.addRecipe (output,input,energy);
										Float chance = Float.parseFloat (input[4]);
										recipe.addToSecondaryOutput (second,chance);
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[4] + "'");
									}
								} else
									WurmScript.info ("Invalid Stack '" + input[3] + "'");
							}
						} else
							WurmScript.info ("Number Must Be Greater Than 0!");
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addCrusher('<output> <input> <energy> | <secondary> <chance>");
	}

	@ScriptFunction
	public void addFermenter (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			FluidStack outputFluid = StackHelper.convertToFluid (input[0]);
			if (outputFluid != null) {
				ItemStack output = StackHelper.convert (input[1],null);
				if (output != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						try {
							int energy = Integer.parseInt (input[3]);
							if (energy > 0)
								FermenterRecipe.addRecipe (outputFluid,output,inputStack,energy);
							else
								WurmScript.info ("Number Must Be Greater Than 0!");
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("");
		} else
			WurmScript.info ("addFermenter('<*output> <output> <input> <energy>')");
	}

	@ScriptFunction
	public void addMetalPress (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack mold = StackHelper.convert (input[2],null);
					if (mold != ItemStack.EMPTY) {
						try {
							int energy = Integer.parseInt (input[3]);
							if (energy > 0)
								MetalPressRecipe.addRecipe (output,inputStack,mold,energy);
							else
								WurmScript.info ("Number Must Be Greater Than 0!");
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
			WurmScript.info ("addMetalPress('<output> <input> <mold> <energy>')");
	}

	@ScriptFunction
	public void addRefinery (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			FluidStack outputFluid = StackHelper.convertToFluid (input[0]);
			if (outputFluid != null) {
				FluidStack inputFluid = StackHelper.convertToFluid (input[1]);
				if (inputFluid != null) {
					FluidStack inputFluid2 = StackHelper.convertToFluid (input[2]);
					if (inputFluid2 != null) {
						try {
							int energy = Integer.parseInt (input[3]);
							if (energy > 0)
								RefineryRecipe.addRecipe (outputFluid,inputFluid,inputFluid2,energy);
							else
								WurmScript.info ("Number Must Be Greater Than 0!");
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Fluid '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Fluid '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addRefinery('<*output> <*input> <*input2> <energy>')");
	}

	@ScriptFunction
	public void addSqueezer (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			FluidStack fluidOutput = StackHelper.convertToFluid (input[0]);
			if (fluidOutput != null) {
				ItemStack output = StackHelper.convert (input[1],null);
				if (output != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						try {
							int energy = Integer.parseInt (input[3]);
							if (energy > 0)
								SqueezerRecipe.addRecipe (fluidOutput,output,input,energy);
							else
								WurmScript.info ("Number Must Be Greater Than 0!");
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addSqueezer('<*output> <output> <input> <energy>')");
	}
}
