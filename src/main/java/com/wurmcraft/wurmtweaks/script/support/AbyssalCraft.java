package com.wurmcraft.wurmtweaks.script.support;

import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.shinoow.abyssalcraft.common.items.ItemCrystal;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AbyssalCraft implements IModSupport {

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
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != null) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						try {
							float exp = Float.parseFloat (input[3]);
							CrystallizerRecipes.instance ().crystallize (inputStack,output,output2,exp);
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Input '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Output2 '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCrystallizer('<output> <output2> <input> <xp>')");
	}

	@ScriptFunction
	public void addTransmutator (String line) {
		String input[] = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						float exp = Float.parseFloat (input[2]);
						TransmutatorRecipes.instance ().transmute (inputStack,output,exp);
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addTransmutator('<output> <input> <exp>')");
	}

	@ScriptFunction
	public void addMaterializer (String line) {
		String[] input = line.split (" ");
		if (input.length > 1) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++) {
					ItemStack possInput = StackHelper.convert (input[index],null);
					if (possInput != ItemStack.EMPTY && possInput.getItem () instanceof ItemCrystal)
						inputStacks.add (possInput);
					else {
						WurmScript.info ("Invalid Input '" + input[index] + "' it must be a crystal!");
						return;
					}
				}
				if (inputStacks.size () > 0)
					MaterializerRecipes.instance ().materialize (inputStacks.toArray (new ItemStack[0]),output);
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addMaterializer('<output> <input...>')");
	}
}
