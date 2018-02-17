package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Botania extends ModSupport {

	@Override
	public String getModID () {
		return "botania";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			BotaniaAPI.manaInfusionRecipes.clear ();
			BotaniaAPI.elvenTradeRecipes.clear ();
		}
	}

	@ScriptFunction
	public void addApothecary (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addApothecary('<output> <input>...')");
		List <Object> inputItems = new ArrayList <> ();
		for (String l : Arrays.copyOfRange (input,1,input.length)) {
			ItemStack inputStack = convertS (l);
			if (inputStack != ItemStack.EMPTY)
				inputItems.add (inputItems);
			else if (l.startsWith ("<") && l.endsWith (">") && OreDictionary.doesOreNameExist (l.substring (1,l.length () - 1)))
				inputItems.add (l.substring (1,l.length () - 1));
		}
		BotaniaAPI.registerPetalRecipe (convertS (input[0]),inputItems.toArray (new Object[0]));
	}

	@ScriptFunction
	public void addPureDaisy (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addPureDaisy('<output> <input>')");
		isValid (input[0],input[1]);
		BotaniaAPI.registerPureDaisyRecipe (Block.getBlockFromItem (convertS (input[1]).getItem ()),Block.getBlockFromItem (convertS (input[0]).getItem ()).getDefaultState ());
	}

	@ScriptFunction
	public void addRune (String line) {
		String[] input = verify (line,line.split (" ").length >= 3,"addRune('<output> <mana> <input>...')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		List <Object> inputItems = new ArrayList <> ();
		for (String l : Arrays.copyOfRange (input,2,input.length)) {
			ItemStack inputStack = convertS (l);
			if (inputStack != ItemStack.EMPTY)
				inputItems.add (inputItems);
			else if (l.startsWith ("<") && l.endsWith (">") && OreDictionary.doesOreNameExist (l.substring (1,l.length () - 1)))
				inputItems.add (l.substring (1,l.length () - 1));
			BotaniaAPI.registerRuneAltarRecipe (convertS (input[0]),convertNI (input[1]),inputItems.toArray (new Object[0]));
		}
	}

	@ScriptFunction
	public void addElven (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addElven('<output> <input>')");
		isValid (input[0],input[1]);
		BotaniaAPI.registerElvenTradeRecipe (new ItemStack[] {convertS (input[0])},convertS (input[1]));
	}
}
