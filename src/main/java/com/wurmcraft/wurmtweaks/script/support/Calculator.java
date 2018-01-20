package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import sonar.calculator.mod.common.recipes.*;

import java.util.ArrayList;
import java.util.List;

public class Calculator implements IModSupport {

	@Override
	public String getModID () {
		return "calculator";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllRecipes) {
			AlgorithmSeparatorRecipes.instance ().getRecipes ().clear ();
			AtomicCalculatorRecipes.instance ().getRecipes ().clear ();
			CalculatorRecipes.instance ().getRecipes ().clear ();
			ConductorMastRecipes.instance ().getRecipes ().clear ();
			ExtractionChamberRecipes.instance ().getRecipes ().clear ();
			FabricationChamberRecipes.instance ().getRecipes ().clear ();
			FlawlessCalculatorRecipes.instance ().getRecipes ().clear ();
			HealthProcessorRecipes.instance ().getRecipes ().clear ();
			PrecisionChamberRecipes.instance ().getRecipes ().clear ();
			ScientificRecipes.instance ().getRecipes ().clear ();
			StoneSeparatorRecipes.instance ().getRecipes ().clear ();
			ProcessingChamberRecipes.instance ().getRecipes ().clear ();
			RestorationChamberRecipes.instance ().getRecipes ().clear ();
			ReassemblyChamberRecipes.instance ().getRecipes ().clear ();
		}
	}

	@ScriptFunction
	public void addAlgorithmSeparator (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						AlgorithmSeparatorRecipes.instance ().addRecipe (inputStack,output,output2);
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAlgorithmSeparator('<output> <output2> <input>')");
	}

	@ScriptFunction
	public void addAtomicCalculator (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						ItemStack inputStack3 = StackHelper.convert (input[3],null);
						if (inputStack3 != ItemStack.EMPTY) {
							AtomicCalculatorRecipes.instance ().addRecipe (inputStack,inputStack2,inputStack3,output);
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAtomicCalculator('<output> <input> <input2> <input3>')");
	}

	@ScriptFunction
	public void addCalculator (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						CalculatorRecipes.instance ().addRecipe (inputStack,inputStack2,output);
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addCalculator('<output> <input> <input2>')");
	}

	@ScriptFunction
	public void addConductorMass (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						ConductorMastRecipes.instance ().addRecipe (inputStack,output,energy);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addConductorMass('<output> <input> <energy>')");
	}

	@ScriptFunction
	public void addExtractionChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						ExtractionChamberRecipes.instance ().addRecipe (inputStack,inputStack2,new ExtractionChamberRecipes.ExtractionChamberOutput (output));
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addExtractionChamber('<output> <input> <input2>')");
	}

	@ScriptFunction
	public void addFabracation (String line) {
		String[] input = line.split (" ");
		if (input.length > 1) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputItems = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputItems.add (StackHelper.convert (input[index],null));
					else {
						WurmScript.info ("Invalid Stack '" + input[index] + "'");
						return;
					}
				FabricationChamberRecipes.instance ().addRecipe (output,inputItems.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addFabracation('<output> <input>...");
	}

	@ScriptFunction
	public void addFlawlessCalculator (String line) {
		String[] input = line.split (" ");
		if (input.length == 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						ItemStack inputStack3 = StackHelper.convert (input[3],null);
						if (inputStack3 != ItemStack.EMPTY) {
							ItemStack inputStack4 = StackHelper.convert (input[4],null);
							if (inputStack4 != ItemStack.EMPTY) {
								FlawlessCalculatorRecipes.instance ().addRecipe (inputStack,inputStack2,inputStack3,inputStack4,output);
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
			WurmScript.info ("addFlawlessCalculator('<output> <input> <input2> <input3> <input4>')");
	}

	@ScriptFunction
	public void addHealthProccessor (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY) {
				try {
					int amount = Integer.parseInt (input[1]);
					HealthProcessorRecipes.instance ().addRecipe (stack,amount);
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalud Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addHealthProccessor('<item> <amount>')");
	}

	@ScriptFunction
	public void addPrecisionChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						PrecisionChamberRecipes.instance ().addRecipe (inputStack,inputStack2,new PrecisionChamberRecipes.PrecisionChamberOutput (output));
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addPrecisionChamber('<output> <input> <input2>')");
	}

	@ScriptFunction
	public void addScientific (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						ScientificRecipes.instance ().addRecipe (inputStack,inputStack2,output);
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addScientific('<output> <input> <input2>')");
	}

	@ScriptFunction
	public void addStoneSeperator (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						StoneSeparatorRecipes.instance ().addRecipe (inputStack,inputStack2,output);
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addStoneSeperator('<output> <input> <input2>')");
	}

	@ScriptFunction
	public void addProcessingChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ProcessingChamberRecipes.instance ().addRecipe (output,input);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addProcessingChamber('<output> <input')");
	}

	@ScriptFunction
	public void addRestorationChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					RestorationChamberRecipes.instance ().addRecipe (output,input);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addRestorationChamber('<output> <input')");
	}

	@ScriptFunction
	public void addReassemblyChamber (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ReassemblyChamberRecipes.instance ().addRecipe (output,input);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addReassemblyChamber('<output> <input')");
	}
}
