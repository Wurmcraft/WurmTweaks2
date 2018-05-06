package com.wurmcraft.wurmtweaks.script.support;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import com.google.common.collect.Sets;
import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.utils.ReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BloodMagic extends ModSupport {

	@Override
	public String getModID () {
		return "bloodmagic";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes)
			for (RecipeBloodAltar altar : BloodMagicAPI.INSTANCE.getRecipeRegistrar ().getAltarRecipes ())
				BloodMagicAPI.INSTANCE.getRecipeRegistrar ().removeBloodAltar (altar.getInput ().getMatchingStacks ()[0]);
		// Due to Blood Magic having horrably designed API
		try {
			Field field = BloodMagicAPI.INSTANCE.getRecipeRegistrar ().getClass ().getDeclaredField ("tartaricForgeRecipes");
			ReflectionHelper.setFinalStatic (field,Sets.newHashSet ());
			Field field2 = BloodMagicAPI.INSTANCE.getRecipeRegistrar ().getClass ().getDeclaredField ("alchemyArrayRecipes");
			ReflectionHelper.setFinalStatic (field2,Sets.newHashSet ());
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	@ScriptFunction
	public void addAltar (String line) {
		String[] input = verify (line,line.split (" ").length == 6,"addAltar('<output> <input> <tier> <syphon> <consume> <drain>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2],input[3],input[4],input[5]);
		BloodMagicAPI.INSTANCE.getRecipeRegistrar ().addBloodAltar (convertI (input[1]),convertS (input[0]),convertNI (input[2]),convertNI (input[3]),convertNI (input[4]),convertNI (input[5]));
	}

	@ScriptFunction
	public void addAlchemyArray (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addAlchemyArray('<output> <catalyst> <input')");
		BloodMagicAPI.INSTANCE.getRecipeRegistrar ().addAlchemyArray (convertI (input[2]),convertI (input[1]),convertS (input[0]),null);
	}

	public void addSoulForge (String line) {
		String[] input = verify (line,line.split (" ").length >= 4,"addSoulForge('<output> <souls> <drain> <input>...')");
		isValid (input[0]);
		isValid (EnumInputType.FLOATNG,input[1],input[2]);
		List <ItemStack> inputs = new ArrayList <> ();
		for (int index = 3; index < input.length; index++) {
			isValid (input[index]);
			inputs.add (convertS (input[index]));
		}
		BloodMagicAPI.INSTANCE.getRecipeRegistrar ().addTartaricForge (convertS (input[0]),convertNF (input[0]),convertNF (input[1]),inputs.toArray (new ItemStack[0]));
	}

	public void addTable (String line) {
		String[] input = verify (line,line.split (" ").length >= 5,"addTable('<output> <syphon> <ticks> <tier> <input>...')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1],input[2],input[3]);
		List <Ingredient> inputs = new ArrayList <> ();
		for (int index = 4; index < input.length; index++) {
			isValid (input[index]);
			inputs.add (convertI (input[index]));
		}
		BloodMagicAPI.INSTANCE.getRecipeRegistrar ().addAlchemyTable (convertS (input[0]),convertNI (input[1]),convertNI (input[2]), convertNI (input[3]),inputs.toArray ());
	}
}
