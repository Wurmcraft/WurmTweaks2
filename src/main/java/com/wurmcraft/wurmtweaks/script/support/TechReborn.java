package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.StringUtils;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.RollingMachineRecipe;
import techreborn.api.ScrapboxList;
import techreborn.api.TechRebornAPI;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.machines.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TechReborn extends ModSupport {

	@Override
	public String getModID () {
		return "techreborn";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			RollingMachineRecipe.instance.getRecipeList ().clear ();
			ScrapboxList.stacks.clear ();
			FusionReactorRecipeHelper.reactorRecipes.clear ();
			RecipeHandler.recipeList.clear ();
		}
	}

	@ScriptFunction
	public void addShapelessRolling (String line) {
		String[] input = verify (line,(StringUtils.countMatches (line," ") >= 2 && StringUtils.countMatches (line," ") <= 10),"addShapeless('<output> <input>...')");
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		if (inputStacks.size () > 0)
			TechRebornAPI.addShapelessOreRollingMachinceRecipe (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + inputStacks.hashCode ()),output,inputStacks.toArray (new Ingredient[0]));
		else
			WurmScript.info ("Invalid Recipe, No Items Found!");
	}

	@ScriptFunction
	public void addShapedRolling (String line) {
		String[] input = verify (line,StringUtils.countMatches (line," ") > 4,"addShaped(<output> <style> <format>')");
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++) {
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
		}
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		int[] recipeSize = RecipeUtils.getRecipeSize (Arrays.copyOfRange (input,1,indexFirstVar));
		String[] recipeStyle = new String[recipeSize[1]];
		for (int index = 1; index < (recipeSize[1] + 1); index++) {
			StringBuilder temp = new StringBuilder (RecipeUtils.replaceLastTillDiff (input[index],WurmScript.SPACER));
			if (temp.length () < recipeSize[0])
				while (temp.length () < recipeSize[0])
					temp.append (" ");
			recipeStyle[index - 1] = temp.toString ().replaceAll (WurmScript.SPACER + ""," ");
		}
		HashMap <Character, Ingredient> recipeFormat = new HashMap <> ();
		HashMap <Character, String> invalidFormat = new HashMap <> ();
		for (int index = (recipeSize[1] + 1); index < input.length; index++)
			if (!input[index].startsWith ("<") && input[index].length () == 1) {
				if ((index + 1) < input.length) {
					Ingredient stack = convertI (input[index + 1]);
					recipeFormat.put (input[index].charAt (0),stack);
					if (stack == Ingredient.EMPTY)
						invalidFormat.put (input[index].charAt (0),input[index + 1]);
					index++;
				} else
					recipeFormat.put (input[index].charAt (0),Ingredient.EMPTY);
			} else if (input[index].length () > 1) {
				WurmScript.info ("Invalid Format, '" + input[index] + " Should Be A Single Character!");
				return;
			}
		boolean valid = true;
		for (Character ch : recipeFormat.keySet ())
			if (recipeFormat.get (ch) == Ingredient.EMPTY) {
				WurmScript.info ("Invalid Stack '" + ch + "' " + invalidFormat.getOrDefault (ch,""));
				valid = false;
			}
		if (valid) {
			List <Object> temp = new ArrayList <> ();
			for (Character ch : recipeFormat.keySet ()) {
				temp.add (ch);
				temp.add (recipeFormat.get (ch));
			}
			List <Object> finalRecipe = new ArrayList <> ();
			finalRecipe.addAll (Arrays.asList (recipeStyle));
			finalRecipe.addAll (temp);
			TechRebornAPI.addRollingOreMachinceRecipe (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + finalRecipe.hashCode ()),output,finalRecipe.toArray (new Object[0]));
		}
	}

	@ScriptFunction
	public void addScrapbox (String line) {
		String[] input = verify (line,line.split (" ").length == 1,"addScrapbox('<stack>')");
		isValid (input[0]);
		ScrapboxList.addItemStackToList (convertS (input[0]));
	}

	@ScriptFunction
	public void addThermalGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.THERMAL,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addThermalGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addDieselGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.DIESEL,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addDieselGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addGasGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.GAS,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addGasGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addPlasmaGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.PLASMA,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addPlasmaGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addSemiFluidGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.SEMIFLUID,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addSemiFluidGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addTechFusion (String line) {
		String[] input = verify (line,line.split (" ").length == 6,"addTechFusion('<output> <topInput> <bottomInput> <startEU> <euTick> <time>'");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4],input[5]);
		FusionReactorRecipeHelper.registerRecipe (new FusionReactorRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]),convertNI (input[4]),convertNI (input[5])));
	}

	@ScriptFunction
	public void addAlloySmelter (String line) {
		String[] input = verify (line,line.split (" ").length == 5,"addAlloySmelter('<output> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4]);
		RecipeHandler.addRecipe (new AlloySmelterRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]),convertNI (input[4])));
	}

	@ScriptFunction
	public void addAssemblingMachine (String line) {
		String[] input = verify (line,line.split (" ").length == 5,"addAssemblingMachine('<output> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4]);
		RecipeHandler.addRecipe (new AssemblingMachineRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]),convertNI (input[4])));
	}

	@ScriptFunction
	public void addIndustrialBlastFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialBlastFurnace('<output> <output2> <input> <input2> <time> <euTick> <heat>'");
		isValid (input[0],input[1],input[2],input[3]);
		isValid (EnumInputType.INTEGER,input[4],input[5],input[6]);
		RecipeHandler.addRecipe (new BlastFurnaceRecipe (convertS (input[2]),convertS (input[3]),convertS (input[0]),convertS (input[1]),convertNI (input[4]),convertNI (input[5]),convertNI (input[6])));
	}

	@ScriptFunction
	public void addCenterfuge (String line) {
		String[] input = verify (line,line.split (" ").length == 8,"addIndustrialElectrolyzer('<output> <output2> <output3> <output4> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2],input[3],input[4],input[5]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		RecipeHandler.addRecipe (new CentrifugeRecipe (convertS (input[4]),convertS (input[5]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[6]),convertNI (input[7])));
	}

	@ScriptFunction
	public void addChemicalReactor (String line) {
		String[] input = verify (line,line.split (" ").length == 5,"addChemicalReactor('<output> <input> <time> <euTick>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4]);
		RecipeHandler.addRecipe (new ChemicalReactorRecipe (convertS (input[2]),convertS (input[1]),convertS (input[0]),convertNI (input[3]),convertNI (input[4])));
	}

	@ScriptFunction
	public void addCompressor (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addGrinder('<output> <input> <time> <euTick>'");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		RecipeHandler.addRecipe (new CompressorRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]),convertNI (input[3])));
	}

	@ScriptFunction
	public void addDistillationTower (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addDistillationTower('<output> <output2> <output3> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2],input[3],input[4],input[5]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		RecipeHandler.addRecipe (new DistillationTowerRecipe (convertS (input[4]),convertS (input[5]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[6]),convertNI (input[7])));
	}

	@ScriptFunction
	public void addExtractor (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addExtractor('<output> <input> <time> <euTick>'");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		RecipeHandler.addRecipe (new ExtractorRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]),convertNI (input[3])));
	}

	@ScriptFunction (link = "crushing", linkSize = {4})
	public void addGrinder (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addGrinder('<output> <input> <time> <euTick>'");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		RecipeHandler.addRecipe (new GrinderRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]),convertNI (input[3]) * 10));
	}

	@ScriptFunction
	public void addImplosionCompressor (String line) {
		String[] input = verify (line,line.split (" ").length == 6,"addImplosionCompressor('<output> <output2> <input> <input2> <time> <euTick>')");
		isValid (input[0],input[1],input[2],input[3]);
		isValid (EnumInputType.INTEGER,input[4],input[5]);
		RecipeHandler.addRecipe (new ImplosionCompressorRecipe (convertS (input[2]),convertS (input[3]),convertS (input[0]),convertS (input[1]),convertNI (input[4]),convertNI (input[5])));
	}

	@ScriptFunction
	public void addIndustrialElectrolyzer (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialElectrolyzer('<output> <output2> <output3> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2],input[3],input[4],input[5]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		RecipeHandler.addRecipe (new IndustrialElectrolyzerRecipe (convertS (input[4]),convertS (input[5]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[6]),convertNI (input[7])));
	}

	@ScriptFunction
	public void addIndustrialGrinder (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialGrinder('<output> <output2> <output3> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2],input[3],input[4]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		isValid (EnumInputType.FLUID,input[5]);
		RecipeHandler.addRecipe (new IndustrialGrinderRecipe (convertS (input[4]),convertF (input[5]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[6]),convertNI (input[7])));
	}

	@ScriptFunction
	public void addIndustrialSawmill (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialSawmill('<output> <output2> <output3> <input> <input2> <time> <euTick>'");
		isValid (input[0],input[1],input[2],input[3]);
		isValid (EnumInputType.INTEGER,input[5],input[6]);
		isValid (EnumInputType.FLUID,input[4]);
		RecipeHandler.addRecipe (new IndustrialSawmillRecipe (convertS (input[3]),convertF (input[4]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertNI (input[5]),convertNI (input[6])));
	}

	@ScriptFunction
	public void addVacuumFreezer (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addVacuumFreezer('<output> <input> <time> <euTick')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		RecipeHandler.addRecipe (new VacuumFreezerRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]),convertNI (input[3]) * 10));
	}
}
