package com.wurmcraft.wurmtweaks.script.support;


import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Minecraft extends ModSupport {

	@Override
	public String getModID () {
		return "minecraft";
	}

	@Override
	public void init () {

	}

	@ScriptFunction
	public void addShapeless (String line) {
		String[] input = verify (line,(StringUtils.countMatches (line," ") >= 2 && StringUtils.countMatches (line," ") <= 10),"addShapeless('<output> <input>...')");
		isValid (input[0]);
		List <Ingredient> inputs = RecipeUtils.getShapelessRecipeItems (input,null,1);
		Preconditions.checkArgument (!inputs.isEmpty (),"Invalid Inputs!");
		RecipeUtils.addShapeless (convertS (input[0]),inputs.toArray (new Ingredient[0]));
	}

	@ScriptFunction
	public void addShaped (String line) {
		String[] input = verify (line,line.split (" ").length > 4,"addShaped(<output> <style> <format>')");
		isValid (input[0]);
		List <Object> recipe = RecipeUtils.getShapedRecipe (input);
		Preconditions.checkNotNull (recipe);
		RecipeUtils.addShaped (convertS (input[0]),recipe.toArray (new Object[0]));
	}

	@ScriptFunction (linkSize = 3, link = "furnace")
	public void addFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 2 || line.split (" ").length == 3,"addFurnace('<output> <input> <exp>')");
		isValid (input[0],input[1]);
		if (line.length () == 3) {
			isValid (EnumInputType.FLOATNG,input[2]);
			RecipeUtils.addFurnace (convertS (input[0]),convertS (input[1]),convertNF (input[2]));
		} else
			RecipeUtils.addFurnace (convertS (input[0]),convertS (input[1]),1);
	}

	@ScriptFunction
	public void addOreEntry (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addOreEntry('<stack> entry')");
		isValid (input[0]);
		for (int index = 1; index < input.length; index++) {
			isValid (EnumInputType.STRING,input[index]);
			OreDictionary.registerOre (input[index].replaceAll ("[<>]",""),convertS (input[0]));
		}
	}

	@ScriptFunction
	public void addBrewing (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addBrewing('<output> <input> <bottom>')");
		for (int index = 0; index < 2; index++)
			isValid (input[index]);
		RecipeUtils.addBrewing (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}
}
