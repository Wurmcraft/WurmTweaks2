package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.item.ItemStack;
import sonar.calculator.mod.common.recipes.*;

import java.util.ArrayList;
import java.util.List;

public class Calculator extends ModSupport {

	@Override
	public String getModID () {
		return "calculator";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
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
		String[] input = verify (line,line.split (" ").length == 3,"addCalculator('<output> <output2>  <input2>')");
		isValid (input[0],input[1],input[2]);
		AlgorithmSeparatorRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}

	@ScriptFunction
	public void addAtomicCalculator (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addCalculator('<output> <input>  <input2> <input3>')");
		isValid (input[0],input[1],input[2]);
		CalculatorRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]));
	}

	@ScriptFunction
	public void addCalculator (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addCalculator('<output> <input>  <input2>')");
		isValid (input[0],input[1],input[2]);
		CalculatorRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}

	@ScriptFunction
	public void addConductorMass (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addConductorMass('<output> <input> <energy>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		ConductorMastRecipes.instance ().addRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}

	@ScriptFunction
	public void addExtractionChamber (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addStoneSeperator('<output> <input>  <input2>')");
		isValid (input[0],input[1],input[2]);
		StoneSeparatorRecipes.instance ().addRecipe (convertS (input[1]),convertS (input[2]),new ExtractionChamberRecipes.ExtractionChamberOutput (convertS (input[0])));
	}

	@ScriptFunction
	public void addFabrication (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addFabrication('<output> <input>...')");
		isValid (input[0]);
		List <ItemStack> inputItems = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputItems.add (convertS (input[index]));
		}
		FabricationChamberRecipes.instance ().addRecipe (convertS (input[0]),inputItems.toArray (new ItemStack[0]));
	}

	@ScriptFunction
	public void addFlawlessCalculator (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addPrecisionChamber('<output> <input>  <input2> <input3> <input4>')");
		isValid (input[0],input[1],input[2],input[3],input[4],input[5]);
		FlawlessCalculatorRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertS (input[4]),convertS (input[5]));
	}

	@ScriptFunction
	public void addHealthProccessor (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addHealthProccessor('<stack> <amount>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		HealthProcessorRecipes.instance ().addRecipe (convertS (input[0]),convertNI (input[1]));
	}

	@ScriptFunction
	public void addPrecisionChamber (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addPrecisionChamber('<output> <input>  <input2>')");
		isValid (input[0],input[1],input[2]);
		PrecisionChamberRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}

	@ScriptFunction
	public void addScientific (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addScientific('<output> <input>  <input2>')");
		isValid (input[0],input[1],input[2]);
		ScientificRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}

	@ScriptFunction
	public void addStoneSeperator (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addStoneSeperator('<output> <input>  <input2>')");
		isValid (input[0],input[1],input[2]);
		StoneSeparatorRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}

	@ScriptFunction
	public void addProcessingChamber (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addProcessingChamber('<output> <input>')");
		isValid (input[0],input[1]);
		ProcessingChamberRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]));
	}

	@ScriptFunction
	public void addRestorationChamber (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addRestorationChamber('<output> <input>')");
		isValid (input[0],input[1]);
		RestorationChamberRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]));
	}

	@ScriptFunction
	public void addReassemblyChamber (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addReassemblyChamber('<output> <input>')");
		isValid (input[0],input[1]);
		ReassemblyChamberRecipes.instance ().addRecipe (convertS (input[0]),convertS (input[1]));
	}
}
