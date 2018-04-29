package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.Arrays;

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
	public String getModID () {
		return "thaumcraft";
	}

	@Override
	public void init () {
		// TODO Remove Recipes
	}

	@ScriptFunction
	public void addLootBagItem (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addLootBagItem('<output> <bag>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		ThaumcraftApi.addLootBagItem (convertS (input[0]),convertNI (input[1]));
	}

	@ScriptFunction
	public void addCrucible (String line) {
		String[] input = verify (line,line.split (" ").length >= 4,"addCrucible('<output> <researchKey> <input> <aspects>...')");
		isValid (input[0],input[2]);
		AspectList list = getAspectList (Arrays.copyOfRange (input,3,input.length));
		ItemStack output = StackHelper.convert (input[0]);
		ThaumcraftApi.addCrucibleRecipe (new ResourceLocation (Global.MODID,"thaumcraft_" + output.getUnlocalizedName () + output.getItemDamage ()),new CrucibleRecipe (input[1],output,convertS (input[2]),list));
	}

	@ScriptFunction
	public void addInfusion (String line) {
		// TODO Create possible way to add 2 infinite series in WurmScript
	}

	@ScriptFunction
	public void addShapedArcane (String line) {
		// TODO Create possible way to add 2 infinite series in WurmScript
	}

	@ScriptFunction
	public void addShapelessArcane (String line) {
		// TODO Create possible way to add 2 infinite series in WurmScript
	}

}
