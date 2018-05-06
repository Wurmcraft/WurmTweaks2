package com.wurmcraft.wurmtweaks.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
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
		List <Ingredient> inputs = RecipeUtils.getShapelessRecipeItems (input,null,1);
		ItemStack output = convertS (input[0]);
		Preconditions.checkArgument (!inputs.isEmpty (),"Invalid Inputs!");
		TechRebornAPI.addShapelessOreRollingMachinceRecipe (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + inputs.hashCode ()),output,inputs.toArray (new Ingredient[0]));
	}

	@ScriptFunction
	public void addShapedRolling (String line) {
		String[] input = verify (line,StringUtils.countMatches (line," ") > 4,"addShaped(<output> <style> <format>')");
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		List <Object> recipe = RecipeUtils.getShapedRecipe (input);
		Preconditions.checkNotNull (recipe);
		TechRebornAPI.addRollingOreMachinceRecipe (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + recipe.hashCode ()),output,recipe.toArray (new Object[0]));
	}

	@ScriptFunction
	public void addScrapbox (String line) {
		String[] input = verify (line,line.split (" ").length == 1,"addScrapbox('<stack>')");
		isValid (input[0]);
		ScrapboxList.addItemStackToList (convertS (input[0]));
	}

	@ScriptFunction
	public void addGeneratorFluid (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addGeneratorFluid('<type> <*fluid> <energy>')");
		Preconditions.checkNotNull (getGeneratorType (input[0]));
		isValid (EnumInputType.FLUID,input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		GeneratorRecipeHelper.registerFluidRecipe (getGeneratorType (input[0]),convertF (input[1]).getFluid (),convertNI (input[2]));
	}

	private EFluidGenerator getGeneratorType (String name) {
		return EFluidGenerator.valueOf (name.toUpperCase ());
	}

	@ScriptFunction
	public void addTechFusion (String line) {
		String[] input = verify (line,line.split (" ").length == 6,"addTechFusion('<output> <topInput> <bottomInput> <startEU> <euTick> <time>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4],input[5]);
		FusionReactorRecipeHelper.registerRecipe (new FusionReactorRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]),convertNI (input[4]),convertNI (input[5])));
	}

	@ScriptFunction
	public void addAlloySmelter (String line) {
		String[] input = verify (line,line.split (" ").length == 5,"addAlloySmelter('<output> <input> <input2> <time> <euTick>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4]);
		RecipeHandler.addRecipe (new AlloySmelterRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]),convertNI (input[4])));
	}

	@ScriptFunction
	public void addAssemblingMachine (String line) {
		String[] input = verify (line,line.split (" ").length == 5,"addAssemblingMachine('<output> <input> <input2> <time> <euTick>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3],input[4]);
		RecipeHandler.addRecipe (new AssemblingMachineRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]),convertNI (input[4])));
	}

	@ScriptFunction
	public void addIndustrialBlastFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialBlastFurnace('<output> <output2> <input> <input2> <time> <euTick> <heat>')");
		isValid (input[0],input[1],input[2],input[3]);
		isValid (EnumInputType.INTEGER,input[4],input[5],input[6]);
		RecipeHandler.addRecipe (new BlastFurnaceRecipe (convertS (input[2]),convertS (input[3]),convertS (input[0]),convertS (input[1]),convertNI (input[4]),convertNI (input[5]),convertNI (input[6])));
	}

	@ScriptFunction(link = "centerfuge", linkSize = {8})
	public void addTRCenterfuge (String line) {
		String[] input = verify (line,line.split (" ").length == 8,"addCenterfuge('<output> <output2> <output3> <input> <input2> <time> <euTick> <output4>')");
		isValid (input[0],input[1],input[2],input[3],input[4],input[7]);
		isValid (EnumInputType.INTEGER,input[5],input[6]);
		RecipeHandler.addRecipe (new CentrifugeRecipe (convertS (input[4]),convertS (input[7]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[5]),convertNI (input[6])));
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
		String[] input = verify (line,line.split (" ").length == 4,"addGrinder('<output> <input> <time> <euTick>')");
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
		String[] input = verify (line,line.split (" ").length == 4,"addExtractor('<output> <input> <time> <euTick>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		RecipeHandler.addRecipe (new ExtractorRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]),convertNI (input[3])));
	}

	@ScriptFunction (link = "crushing", linkSize = {4})
	public void addGrinder (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addGrinder('<output> <input> <time> <euTick>')");
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
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialElectrolyzer('<output> <output2> <output3> <input> <input2> <time> <euTick>')");
		isValid (input[0],input[1],input[2],input[3],input[4],input[5]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		RecipeHandler.addRecipe (new IndustrialElectrolyzerRecipe (convertS (input[4]),convertS (input[5]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[6]),convertNI (input[7])));
	}

	@ScriptFunction
	public void addIndustrialGrinder (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialGrinder('<output> <output2> <output3> <input> <input2> <time> <euTick>')");
		isValid (input[0],input[1],input[2],input[3],input[4]);
		isValid (EnumInputType.INTEGER,input[6],input[7]);
		isValid (EnumInputType.FLUID,input[5]);
		RecipeHandler.addRecipe (new IndustrialGrinderRecipe (convertS (input[4]),convertF (input[5]),convertS (input[0]),convertS (input[1]),convertS (input[2]),convertS (input[3]),convertNI (input[6]),convertNI (input[7])));
	}

	@ScriptFunction
	public void addIndustrialSawmill (String line) {
		String[] input = verify (line,line.split (" ").length == 7,"addIndustrialSawmill('<output> <output2> <output3> <input> <input2> <time> <euTick>')");
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
