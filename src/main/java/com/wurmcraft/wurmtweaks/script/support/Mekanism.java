package com.wurmcraft.wurmtweaks.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import mekanism.api.MekanismAPI;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.recipe.RecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class Mekanism extends ModSupport {

	public static String getGases () {
		StringBuilder builder = new StringBuilder ();
		for (Gas stack : GasRegistry.getRegisteredGasses ())
			if (stack != null && stack.getFluid () != null)
				builder.append (stack.getFluid ().getUnlocalizedName () + "\n");
		return builder.toString ();
	}

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
		String[] input = verify (line,line.split (" ").length == 2,"addEnrichmentChamber('<output> <input>')");
		isValid (input[0],input[1]);
		MekanismAPI.recipeHelper ().addEnrichmentChamberRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addOsmiumCompressor (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addOsmiumCompressor('<output> <input>')");
		isValid (input[0],input[1]);
		MekanismAPI.recipeHelper ().addOsmiumCompressorRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addCombiner (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addCombiner('<output> <input>')");
		isValid (input[0],input[1]);
		MekanismAPI.recipeHelper ().addCombinerRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction (link = "crushing", linkSize = {2})
	public void addMCrusher (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addMCrusher('<output> <input>')");
		isValid (input[0],input[1]);
		MekanismAPI.recipeHelper ().addCrusherRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addPurification (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addPurification('<output> <input>')");
		isValid (input[0],input[1]);
		MekanismAPI.recipeHelper ().addPurificationChamberRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addMetallurgicInfuser (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addMetallurgicInfuser('<output> <input> <infusionType> <amount>')");
		isValid (input[0],input[1]);
		InfuseType type = InfuseRegistry.get (input[2]);
		Preconditions.checkNotNull (type);
		isValid (EnumInputType.INTEGER,input[3]);
		MekanismAPI.recipeHelper ().addMetallurgicInfuserRecipe (type,convertNI (input[3]),convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addChemicalInfuser (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addChemicalInfuser('<%output> <%input> <%input2>')");
		GasStack output = getGasStack (input[0]);
		Preconditions.checkNotNull (output);
		GasStack inputStack = getGasStack (input[1]);
		Preconditions.checkNotNull (inputStack);
		GasStack inputStack2 = getGasStack (input[2]);
		Preconditions.checkNotNull (inputStack2);
		MekanismAPI.recipeHelper ().addChemicalInfuserRecipe (inputStack,inputStack2,output);
	}

	@ScriptFunction
	public void addChemicalOxidizer (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addChemicalOxidizer('<%output> <input>')");
		GasStack output = getGasStack (input[0]);
		Preconditions.checkNotNull (output);
		isValid (input[1]);
		MekanismAPI.recipeHelper ().addChemicalOxidizerRecipe (convertS (input[1]),output);
	}

	@ScriptFunction
	public void addChemicalInjection (String line) {
		String[] input = verify (line,line.split ("").length == 3,"addChemicalInjection('<output> <input> <%input>')");
		isValid (input[0],input[1]);
		GasStack inputStack = getGasStack (input[2]);
		Preconditions.checkNotNull (inputStack);
		MekanismAPI.recipeHelper ().addChemicalInjectionChamberRecipe (convertS (input[1]),inputStack.getGas (),convertS (input[0]));
	}

	@ScriptFunction
	public void addElectrolyticSeparator (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addElectrolyticSeparator('<leftOutput> <rightOutput> <*input> <energy>')");
		GasStack leftOutput = getGasStack (input[0]);
		Preconditions.checkNotNull (leftOutput);
		GasStack rightOutput = getGasStack (input[1]);
		Preconditions.checkNotNull (rightOutput);
		isValid (EnumInputType.FLUID,input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		MekanismAPI.recipeHelper ().addElectrolyticSeparatorRecipe (convertF (input[2]),convertNF (input[3]),leftOutput,rightOutput);

	}

	@ScriptFunction (link = "saw", linkSize = {2,4})
	public void addSawmill (String line) {
		String[] input = verify (line,line.split (" ").length == 2 || line.split (" ").length == 4,"addSawmill('<output> <input> | <secOutput> <secOutput%>')");
		isValid (input[0],input[1]);
		if (line.split (" ").length == 2) {
			MekanismAPI.recipeHelper ().addPrecisionSawmillRecipe (convertS (input[1]),convertS (input[0]));
		} else {
			isValid (input[2]);
			isValid (EnumInputType.FLOATNG,input[3]);
			MekanismAPI.recipeHelper ().addPrecisionSawmillRecipe (convertS (input[1]),convertS (input[0]),convertS (input[2]),convertNF (input[3]));
		}
	}

	@ScriptFunction
	public void addChemicalDissolution (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addChemicalDissolution('<%output> <input>')");
		GasStack output = getGasStack (input[0]);
		Preconditions.checkNotNull (output);
		isValid (input[1]);
		MekanismAPI.recipeHelper ().addChemicalDissolutionChamberRecipe (convertS (input[1]),output);
	}

	@ScriptFunction
	public void addChemicalWasher (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addChemicalWasher('<%output> <%input>')");
		GasStack output = getGasStack (input[0]);
		Preconditions.checkNotNull (output);
		GasStack inputStack = getGasStack (input[1]);
		Preconditions.checkNotNull (inputStack);
		MekanismAPI.recipeHelper ().addChemicalWasherRecipe (inputStack,output);
	}

	@ScriptFunction
	public void addChemicalCrystallizer (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addChemicalCrystallizer('<output> <%input>')");
		isValid (input[0]);
		GasStack inputStack = getGasStack (input[1]);
		Preconditions.checkNotNull (inputStack);
		MekanismAPI.recipeHelper ().addChemicalCrystallizerRecipe (inputStack,convertS (input[0]));
	}

	@ScriptFunction ()
	public void addPressureChamber (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addPressureChamber('<output> <%outputGas> <%inputGas> <*inputFluid> <input> <extraEnergy> <time>')");
		isValid (input[0],input[4]);
		isValid (EnumInputType.FLOATNG,input[5]);
		isValid (EnumInputType.INTEGER,input[6]);
		GasStack outputStack = getGasStack (input[1]);
		Preconditions.checkNotNull (outputStack);
		GasStack inputStack = getGasStack (input[2]);
		Preconditions.checkNotNull (inputStack);
		isValid (EnumInputType.FLUID,input[3]);
		MekanismAPI.recipeHelper ().addPRCRecipe (convertS (input[4]),convertF (input[3]),inputStack,convertS (input[0]),outputStack,(double) convertNF (input[5]),convertNI (input[6]));
	}

	@ScriptFunction
	public void addThermalEvaporation (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addThermalEvaporation('<*output> <*input>')");
		MekanismAPI.recipeHelper ().addThermalEvaporationRecipe (convertF (input[0]),convertF (input[1]));
	}

	@ScriptFunction
	public void addSolarNeutron (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addSolarNeutron('<%output> <%input>')");
		GasStack output = getGasStack (input[0]);
		Preconditions.checkNotNull (output);
		GasStack inputStack = getGasStack (input[1]);
		Preconditions.checkNotNull (inputStack);
		MekanismAPI.recipeHelper ().addSolarNeutronRecipe (inputStack,output);
	}

	@ScriptFunction
	public void addBoxBlacklist (String line) {
		String[] input = line.split (" ");
		isValid (input[0]);
		Block block = Block.getBlockFromItem (convertS (input[0]).getItem ());
		Preconditions.checkArgument (block != Blocks.AIR);
		MekanismAPI.addBoxBlacklist (block,convertS (input[0]).getItemDamage ());
	}

	private GasStack getGasStack (String stack) {
		if (stack.startsWith (ConfigHandler.startChar + ConfigHandler.gasChar)) {
			String name = stack.substring (stack.indexOf (ConfigHandler.startChar.charAt (0)) + 1,stack.indexOf (ConfigHandler.endChar.charAt (0)));
			int amount = Integer.parseInt (stack.substring (stack.indexOf (ConfigHandler.startChar + ConfigHandler.gasChar) + 2,stack.indexOf (ConfigHandler.sizeChar)));
			if (GasRegistry.containsGas (name.toUpperCase ()))
				return new GasStack (GasRegistry.getGas (name.toUpperCase ()),amount);
		}
		return null;
	}
}
