package com.wurmcraft.wurmtweaks.script.support;

import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DraconicEvolution implements IModSupport {

	@Override
	public String getModID () {
		return "draconicevolution";
	}

	@Override
	public void init () {
	}

	@ScriptFunction
	public void addFusion (String line) {
		String[] input = line.split (" ");
		if (input.length >= 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack catalyst = StackHelper.convert (input[1],null);
				if (catalyst != ItemStack.EMPTY) {
					try {
						int energy = Integer.parseInt (input[2]);
						try {
							int tier = Integer.parseInt (input[3]);
							List <ItemStack> inputs = new ArrayList <> ();
							for (int index = 4; index < input.length; index++)
								if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
									inputs.add (StackHelper.convert (input[index],null));
								else {
									WurmScript.info ("Invalid Stack '" + input[index] + "'");
									return;
								}
							if (inputs.size () > 0)
								FusionRecipeAPI.addRecipe (new SimpleFusionRecipe (output,catalyst,energy,tier,inputs.toArray (new ItemStack[0])));
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addFusion('<output> <catalyst> <energy> <tier> <inputs>...");
	}
}
