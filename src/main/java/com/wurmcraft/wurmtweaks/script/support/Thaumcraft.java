package com.wurmcraft.wurmtweaks.script.support;


import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.internal.WeightedRandomLoot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thaumcraft extends ModSupport {

	private static AspectList getAspectList (String[] aspect) {
		AspectList list = new AspectList ();
		for (String spect : aspect)
			if (spect.startsWith (ConfigHandler.startChar + ConfigHandler.aspectChar)) {
				Aspect aspe = getAspect (spect.substring (spect.indexOf (ConfigHandler.startChar.charAt (0)) + 1,spect.indexOf (ConfigHandler.endChar.charAt (0))));
				int amount = Integer.parseInt (spect.substring (spect.indexOf (ConfigHandler.startChar + ConfigHandler.aspectChar) + 2,spect.indexOf (ConfigHandler.sizeChar)));
				list.add (aspe,amount);
			}
		return list;
	}

	private static Aspect getAspect (String aspect) {
		return Aspect.getAspect (aspect);
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			WeightedRandomLoot.lootBagCommon.clear ();
			WeightedRandomLoot.lootBagUncommon.clear ();
			WeightedRandomLoot.lootBagRare.clear ();
		}
	}

	@ScriptFunction
	public void addLootBagItem (WurmScript script,String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addLootBagItem('<output> <bag>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		ThaumcraftApi.addLootBagItem (convertS (input[0]),convertNI (input[1]));
	}

	@ScriptFunction
	public void addCrucible (WurmScript script,String line) {
		String[] input = verify (line,line.split (" ").length >= 4,"addCrucible('<output> <researchKey> <input> <aspects>...')");
		isValid (input[0],input[2]);
		AspectList list = getAspectList (Arrays.copyOfRange (input,3,input.length));
		ItemStack output = convertS (input[0]);
		ThaumcraftApi.addCrucibleRecipe (new ResourceLocation (Global.MODID,"thaumcraft_" + output.getUnlocalizedName () + output.getItemDamage ()),new CrucibleRecipe (input[1],output,convertS (input[2]),list));
	}

	@ScriptFunction
	public void addInfusion (WurmScript script,String line) {
		String[] input = verify (line,line.split (" ").length >= 7,"addInfusion(<output> <research> <centerStack> <instability> <items>... <aspects...'");
		isValid (input[0],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		List <ItemStack> items = new ArrayList <> ();
		for (int index = 4; index < input.length; index++)
			if (!input[index].startsWith (ConfigHandler.startChar + ConfigHandler.aspectChar)) {
				isValid (input[index]);
				items.add (convertS (input[index]));

			} else
				break;
		List <String> aspectList = new ArrayList <> ();
		for (int index = 5; index < input.length - 1; index++)
			if (input[index].startsWith (ConfigHandler.startChar + ConfigHandler.aspectChar))
				aspectList.add (input[index]);
		InfusionRecipe recipe = new InfusionRecipe (input[1],convertS (input[0]),convertNI (input[3]),getAspectList (aspectList.toArray (new String[0])),convertS (input[2]),items.toArray (new ItemStack[0]));
		ThaumcraftApi.addInfusionCraftingRecipe (new ResourceLocation (Global.MODID,recipe.getRecipeOutput ().hashCode () + "_" + recipe.research),recipe);
	}
}
