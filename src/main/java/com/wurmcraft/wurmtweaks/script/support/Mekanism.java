package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import mekanism.api.MekanismAPI;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.recipe.RecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class Mekanism implements IModSupport {

	@Override
	public String getModID () {
		return "mekanism";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			RecipeHandler.Recipe.CHEMICAL_CRYSTALLIZER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_INFUSER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_INJECTION_CHAMBER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_WASHER.get ().clear ();
			RecipeHandler.Recipe.COMBINER.get ().clear ();
			RecipeHandler.Recipe.CRUSHER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_WASHER.get ().clear ();
			RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get ().clear ();
			RecipeHandler.Recipe.ENERGIZED_SMELTER.get ().clear ();
			RecipeHandler.Recipe.ENRICHMENT_CHAMBER.get ().clear ();
			RecipeHandler.Recipe.PURIFICATION_CHAMBER.get ().clear ();
			RecipeHandler.Recipe.PRECISION_SAWMILL.get ().clear ();
			RecipeHandler.Recipe.METALLURGIC_INFUSER.get ().clear ();
			RecipeHandler.Recipe.THERMAL_EVAPORATION_PLANT.get ().clear ();
			RecipeHandler.Recipe.SOLAR_NEUTRON_ACTIVATOR.get ().clear ();
			RecipeHandler.Recipe.OSMIUM_COMPRESSOR.get ().clear ();
		}
	}

	@ScriptFunction
	public void addEnrichmentChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addEnrichmentChamberRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addEnrichmentChamber('<output> <input')");
	}

	@ScriptFunction
	public void addOsmiumCompressor (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addOsmiumCompressorRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addOsmiumCompressor('<output> <input')");
	}

	@ScriptFunction
	public void addCombiner (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addCombinerRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addCombiner('<output> <input')");
	}

	@ScriptFunction (link = "crushing", linkSize = {2})
	public void addMCruser (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addCombinerRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addMCruser('<output> <input')");
	}

	@ScriptFunction
	public void addPurification (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addPurificationChamberRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addPurification('<output> <input')");
	}

	@ScriptFunction
	public void addMetallurgicInfuser (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				InfuseType type = InfuseRegistry.get (input[1]);
				if (type != null) {
					try {
						int amount = Integer.parseInt (input[2]);
						if (amount > 0) {
							ItemStack inputStack = StackHelper.convert (input[3],null);
							if (inputStack != ItemStack.EMPTY)
								MekanismAPI.recipeHelper ().addMetallurgicInfuserRecipe (type,amount,inputStack,output);
							else
								WurmScript.info ("Invalid Stack '" + input[3] + "'");
						} else
							WurmScript.info ("Number Must Be Greater Than 0!");
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info (input[1] + " is not a valid infusion type!");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addMetallurgicInfuser('<output> <infusionType> <amount> <input>')");
	}


	@ScriptFunction
	public void addChemicalInfuser (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			GasStack output = getGasStack (input[0]);
			if (output != null) {
				GasStack inputStack = getGasStack (input[1]);
				if (inputStack != null) {
					GasStack inputStack2 = getGasStack (input[2]);
					if (inputStack2 != null) {
						MekanismAPI.recipeHelper ().addChemicalInfuserRecipe (inputStack,inputStack2,output);
					} else
						WurmScript.info ("Invalid Gas '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Gas '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalInfuser('<%output> <%input1> <%input2>')");
	}

	@ScriptFunction
	public void addChemicalOxidizer (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			GasStack outputGas = getGasStack (input[0]);
			if (outputGas != null) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addChemicalOxidizerRecipe (inputStack,outputGas);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Gas '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalOxidizer('<%outout> <input>')");
	}

	@ScriptFunction
	public void addChemicalInjection (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				GasStack inputGas = getGasStack (input[1]);
				if (inputGas != null) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						MekanismAPI.recipeHelper ().addChemicalInjectionChamberRecipe (inputStack,inputGas.getGas (),output);
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalOxidizer('<output> <%input> <input>");
	}

	@ScriptFunction
	public void addElectrolyticSeparator (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			GasStack leftOutput = getGasStack (input[0]);
			if (leftOutput != null) {
				GasStack rightOutput = getGasStack (input[1]);
				if (rightOutput != null) {
					try {
						double energy = Double.parseDouble (input[2]);
						if (energy > 0) {
							FluidStack inputFluid = StackHelper.convertToFluid (input[3]);
							if (inputFluid != null) {
								MekanismAPI.recipeHelper ().addElectrolyticSeparatorRecipe (inputFluid,energy,leftOutput,rightOutput);
							} else
								WurmScript.info ("Invalid Fluid '" + input[3] + "'");
						} else
							WurmScript.info ("Number Must Be Greater Than 0!");
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Gas '" + input[0] + "'");
		} else
			WurmScript.info ("addElectrolyticSeparator(''<%leftOutput> <%rightOutput> <energy> <*input>");
	}

	@ScriptFunction
	public void addSawmill (String line) {
		String[] input = line.split (" ");
		if (input.length == 2 || input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					if (input.length == 2)
						MekanismAPI.recipeHelper ().addPrecisionSawmillRecipe (inputStack,output);
					else {
						ItemStack secOutput = StackHelper.convert (input[2],null);
						if (secOutput != ItemStack.EMPTY) {
							try {
								double chance = Double.parseDouble (input[3]);
								MekanismAPI.recipeHelper ().addPrecisionSawmillRecipe (inputStack,output,secOutput,chance);
							} catch (NumberFormatException e) {
								WurmScript.info ("Invalid Number '" + input[3] + "'");
							}
						} else
							WurmScript.info ("Invalid Stack '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addElectrolyticSeparator('<output> <input> | <secondaryOutput> <secondaryChance>')");
	}

	@ScriptFunction
	public void addChemicalDissolution (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			GasStack output = getGasStack (input[0]);
			if (output != null) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					MekanismAPI.recipeHelper ().addChemicalDissolutionChamberRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Gas '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalDissolution('<%output> <input>')");
	}

	@ScriptFunction
	public void addChemicalWasher (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			GasStack output = getGasStack (input[0]);
			if (output != null) {
				GasStack inputStack = getGasStack (input[1]);
				if (inputStack != null) {
					MekanismAPI.recipeHelper ().addChemicalWasherRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Gas '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalWasher('<%output> <%input>')");
	}

	@ScriptFunction
	public void addChemicalCrystallizer (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				GasStack inputStack = getGasStack (input[1]);
				if (inputStack != null) {
					MekanismAPI.recipeHelper ().addChemicalCrystallizerRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalCrystallizer('<output> <%input>')");
	}

	@ScriptFunction ()
	public void addPressureChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 7) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				GasStack outputGas = getGasStack (input[1]);
				if (outputGas != null) {
					GasStack inputGas = getGasStack (input[2]);
					if (inputGas != null) {
						FluidStack inputFluid = StackHelper.convertToFluid (input[3]);
						if (inputFluid != null) {
							ItemStack inputStack = StackHelper.convert (input[4],null);
							if (inputStack != ItemStack.EMPTY) {
								try {
									double energy = Double.parseDouble (input[5]);
									try {
										int time = Integer.parseInt (input[6]);
										if (time > 0) {
											MekanismAPI.recipeHelper ().addPRCRecipe (inputStack,inputFluid,inputGas,output,outputGas,energy,time);
										} else
											WurmScript.info ("Time Must Be Greater Than 0!");
									} catch (NumberFormatException f) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} catch (NumberFormatException e) {
									WurmScript.info ("Invalid Number '" + input[5] + "'");
								}
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Fluid '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Gas '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addPressureChamber('<output> <%outputGas> <%inputGas> <*inputFluid> <input> <extraEnergy> <time>')");
	}

	@ScriptFunction
	public void addThermalEvaporation (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack output = StackHelper.convertToFluid (input[0]);
			if (output != null) {
				FluidStack inputStack = StackHelper.convertToFluid (input[1]);
				if (inputStack != null) {
					MekanismAPI.recipeHelper ().addThermalEvaporationRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Fluid '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addThermalEvaporation('<*output> <*input>')");
	}

	@ScriptFunction
	public void addSolarNeutron (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			GasStack output = getGasStack (input[0]);
			if (output != null) {
				GasStack inputStack = getGasStack (input[1]);
				if (inputStack != null) {
					MekanismAPI.recipeHelper ().addSolarNeutronRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Gas '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Gas '" + input[0] + "'");
		} else
			WurmScript.info ("addSolarNeutron('<%output> <%input>')");
	}

	@ScriptFunction
	public void addBoxBlacklist (String line) {
		String[] input = line.split (" ");
		if (input.length == 1) {
			ItemStack block = StackHelper.convert (input[0],null);
			if (block != ItemStack.EMPTY && Block.getBlockFromItem (block.getItem ()) != Blocks.AIR) {
				MekanismAPI.addBoxBlacklist (Block.getBlockFromItem (block.getItem ()),block.getItemDamage ());
			} else
				WurmScript.info ("Invalid Block '" + input[0] + "'");
		} else
			WurmScript.info ("addBoxBlacklist('<block'>')");
	}

	private GasStack getGasStack (String stack) {
		if (stack.startsWith ("<%")) {
			String name = stack.substring (stack.indexOf ("x") + 1,stack.indexOf (">"));
			int amount = Integer.parseInt (stack.substring (stack.indexOf ("<%") + 2,stack.indexOf ("x")));
			if (GasRegistry.containsGas (name.toUpperCase ()))
				return new GasStack (GasRegistry.getGas (name.toUpperCase ()),amount);
		}
		return null;
	}
}
