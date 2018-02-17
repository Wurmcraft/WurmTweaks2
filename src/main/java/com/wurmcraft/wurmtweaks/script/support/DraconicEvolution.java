package com.wurmcraft.wurmtweaks.script.support;

import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.brandon3055.draconicevolution.lib.RecipeManager;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DraconicEvolution extends ModSupport {

	@Override
	public String getModID () {
		return "draconicevolution";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes)
			RecipeManager.FUSION_REGISTRY.getRecipes ().clear ();
	}

	@ScriptFunction
	public void addFusion (String line) {
		String[] input = verify (line,line.split (" ").length >= 5,"addFusion('<output> <catalyst> <tier> <energy> <input>...')");
		isValid (input[0],input[1],input[4]);
		isValid (EnumInputType.INTEGER,input[2],input[3]);
		List <ItemStack> inputs = new ArrayList <> ();
		for (int index = 4; index < input.length; index++) {
			isValid (input[index]);
			inputs.add (convertS (input[index]));
		}
		FusionRecipeAPI.addRecipe (new SimpleFusionRecipe (convertS (input[0]),convertS (input[1]),convertNI (input[3]),convertNI (input[2]),inputs.toArray (new ItemStack[0])));
	}
}
