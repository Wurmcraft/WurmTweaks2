package com.wurmcraft.wurmtweaks.script.support;

import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AbyssalCraft extends ModSupport {

	@Override
	public String getModID () {
		return "abyssalcraft";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			CrystallizerRecipes.instance ().getCrystallizationList ().clear ();
			TransmutatorRecipes.instance ().getTransmutationList ().clear ();
			MaterializerRecipes.instance ().getMaterializationList ().clear ();
		}
	}

	@ScriptFunction
	public void addCrystallizer (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addCrystallizer('<output> <output2> <input> <exp>')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		CrystallizerRecipes.instance ().crystallize (convertS (input[2]),convertS (input[0]),convertS (input[1]),convertNF (input[3]));
	}

	@ScriptFunction
	public void addTransmutator (String line) {
		String input[] = verify (line,line.split (" ").length == 3,"addTransmutator('<output> <input> <energy>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		TransmutatorRecipes.instance ().transmute (convertS (input[1]),convertS (input[0]),convertNF (input[2]));
	}

	@ScriptFunction
	public void addMaterializer (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addMaterializer('<output> <input>...')");
		isValid (input);
		List <ItemStack> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++)
			inputStacks.add (convertS (input[index]));
		MaterializerRecipes.instance ().materialize (inputStacks.toArray (new ItemStack[0]),convertS (input[0]));
	}
}
