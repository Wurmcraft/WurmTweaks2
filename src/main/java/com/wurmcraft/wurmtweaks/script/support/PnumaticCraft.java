package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.common.recipes.AmadronOffer;
import me.desht.pneumaticcraft.common.recipes.AmadronOfferManager;
import me.desht.pneumaticcraft.common.recipes.AssemblyRecipe;
import me.desht.pneumaticcraft.common.recipes.PressureChamberRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PnumaticCraft extends ModSupport {

	@Override
	public String getModID () {
		return "pneumaticcraft";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			AssemblyRecipe.drillRecipes.clear ();
			AssemblyRecipe.laserRecipes.clear ();
			PressureChamberRecipe.chamberRecipes.clear ();
			AmadronOfferManager.getInstance ().getStaticOffers ().clear ();
			AmadronOfferManager.getInstance ().getPeriodicOffers ().clear ();
			AmadronOfferManager.getInstance ().getAllOffers ().clear ();
		}
	}

	@ScriptFunction
	public void addAssemblyDrill (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addAssemblyDrill('<output> <input')");
		isValid (input[0],input[1]);
		PneumaticRegistry.getInstance ().getRecipeRegistry ().addAssemblyDrillRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addAssemblyLaser (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addAssemblyLaser('<output> <input')");
		isValid (input[0],input[1]);
		PneumaticRegistry.getInstance ().getRecipeRegistry ().addAssemblyLaserRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addPressureChamber (String line) {
		String[] input = verify (line,line.split (" ").length >= 3,"addPressureChamber('<output> <pressure> <input>...')");
		isValid (input[0]);
		isValid (EnumInputType.FLOATNG,input[1]);
		List <ItemStack> inputs = new ArrayList <> ();
		for (int index = 2; index < input.length; index++) {
			isValid (input[index]);
			inputs.add (convertS (input[index]));
		}
		PneumaticRegistry.getInstance ().getRecipeRegistry ().registerPressureChamberRecipe (inputs.toArray (new ItemStack[0]),convertNF (input[1]),new ItemStack[] {convertS (input[0])});
	}

	@ScriptFunction
	public void addDefaultAmadron (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addDefaultAmadron('<output> <input>')");
		isValid (input[0],input[1]);
		PneumaticRegistry.getInstance ().getRecipeRegistry ().registerDefaultStaticAmadronOffer (convertS (input[0]),convertS (input[1]));
	}

	@ScriptFunction
	public void addAmadron (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addDefaultAmadron('<output> <input>')");
		isValid (input[0],input[1]);
		PneumaticRegistry.getInstance ().getRecipeRegistry ().registerDefaultPeriodicAmadronOffer (convertS (input[0]),convertS (input[1]));
	}
}
