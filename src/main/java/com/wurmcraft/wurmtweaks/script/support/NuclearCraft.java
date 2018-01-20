package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import nc.recipe.NCRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class NuclearCraft implements IModSupport {

	@Override
	public String getModID () {
		return "nuclearcraft";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllRecipes) {
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
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						NCRecipes.MANUFACTORY_RECIPES.addRecipe (inputStack,output,time);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addManufactory('<output> <input> <time>')");
	}

	@ScriptFunction
	public void addIsotopeSeparator (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							NCRecipes.ISOTOPE_SEPARATOR_RECIPES.addRecipe (inputStack,output,output2,time);
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[1] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addIsotopeSeparator('<output> <output2> <input> <time>')");
	}

	@ScriptFunction
	public void addAlloyFurnace (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							NCRecipes.ISOTOPE_SEPARATOR_RECIPES.addRecipe (inputStack,inputStack2,output,time);
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[1] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAlloyFurnace('<output> <input> <input2> <time>')");
	}

	@ScriptFunction
	public void addSupercooler (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			FluidStack output = StackHelper.convertToFluid (input[0]);
			if (output != null) {
				FluidStack inputFluid = StackHelper.convertToFluid (input[1]);
				if (inputFluid != null) {
					try {
						int time = Integer.parseInt (input[2]);
						NCRecipes.SUPERCOOLER_RECIPES.addRecipe (inputFluid,output,time);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Fluid '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addSupercooler('<*output> <*input> <time>')");
	}

	@ScriptFunction
	public void addPressurizer (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						NCRecipes.PRESSURIZER_RECIPES.addRecipe (inputStack,output,time);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addPressurizer('<output> <input> <time>')");
	}
}
