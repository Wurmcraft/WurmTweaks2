package com.wurmcraft.wurmtweaks.script.support;

import com.rwtema.extrautils2.api.machine.XUMachineCrusher;
import com.rwtema.extrautils2.tile.TileResonator;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

public class ExtraUtils2 implements IModSupport {

	@Override
	public String getModID () {
		return "extrautils2";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			TileResonator.resonatorRecipes.clear ();
			while (XUMachineCrusher.INSTANCE.recipes_registry.iterator ().hasNext ())
				XUMachineCrusher.INSTANCE.recipes_registry.removeRecipe (XUMachineCrusher.INSTANCE.recipes_registry.iterator ().next ());
		}
	}

	@ScriptFunction
	public void addResonator (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						if (energy > 0)
							TileResonator.register (inputStack,output,energy);
						else
							WurmScript.info ("Number Must Be Greater Than 0!");
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addResonator('<output> <input> <energy>')");
	}

	@ScriptFunction (link = "crushing", linkSize = {2,4})
	public void addXUCrusher (String line) {
		String[] input = line.split (" ");
		if (input.length == 2 || input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					if (input.length == 2)
						XUMachineCrusher.addRecipe (inputStack,output);
					else {
						ItemStack secOutput = StackHelper.convert (input[2],null);
						if (secOutput != ItemStack.EMPTY) {
							try {
								Float chance = Float.parseFloat (input[3]);
								XUMachineCrusher.addRecipe (inputStack,output,secOutput,chance);
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
			WurmScript.info ("addCrusher('<output> <input> |<secondaryOutput> <secondary%>')");
	}
}
