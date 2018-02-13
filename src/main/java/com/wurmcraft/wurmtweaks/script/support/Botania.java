package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Botania implements IModSupport {

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
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <Object> inputItems = new ArrayList <> ();
				for (String l : Arrays.copyOfRange (input,1,input.length)) {
					ItemStack inputStack = StackHelper.convert (l,null);
					if (inputStack != ItemStack.EMPTY)
						inputItems.add (inputItems);
					else if (l.startsWith ("<") && l.endsWith (">") && OreDictionary.doesOreNameExist (l.substring (1,l.length () - 1)))
						inputItems.add (l.substring (1,l.length () - 1));
					else
						WurmScript.info ("Invalid Input '" + l + "'");
				}
				BotaniaAPI.registerPetalRecipe (output,inputItems.toArray (new Object[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addApothecary('<output> <input...>')");
	}

	@ScriptFunction
	public void addPureDaisy (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				if (Block.getBlockFromItem (output.getItem ()) != Blocks.AIR) {
					ItemStack inputStack = StackHelper.convert (input[1],null);
					if (inputStack != ItemStack.EMPTY) {
						if (Block.getBlockFromItem (inputStack.getItem ()) != Blocks.AIR) {
							BotaniaAPI.registerPureDaisyRecipe (Block.getBlockFromItem (inputStack.getItem ()),Block.getBlockFromItem (output.getItem ()).getDefaultState ());
						} else
							WurmScript.info ("Input is not a block!");
					} else
						WurmScript.info ("Invalid Input '" + input[1] + "'");
				} else
					WurmScript.info ("Output is not a block!");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addPureDaisy('<outputBlock> <inputBlock>')");
	}

	@ScriptFunction
	public void addRune (String line) {
		String[] input = line.split (" ");
		if (input.length >= 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				try {
					int mana = Integer.parseInt (input[1]);
					List <Object> inputItems = new ArrayList <> ();
					for (String l : Arrays.copyOfRange (input,2,input.length)) {
						ItemStack inputStack = StackHelper.convert (l,null);
						if (inputStack != ItemStack.EMPTY)
							inputItems.add (inputItems);
						else if (l.startsWith ("<") && l.endsWith (">") && OreDictionary.doesOreNameExist (l.substring (1,l.length () - 1)))
							inputItems.add (l.substring (1,l.length () - 1));
						else
							WurmScript.info ("Invalid Input '" + l + "'");
					}
					BotaniaAPI.registerRuneAltarRecipe (output,mana,inputItems.toArray (new Object[0]));
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number " + input[1]);
				}
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addRune('<output> <mana> <inputs>')");
	}

	@ScriptFunction
	public void addElven (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					BotaniaAPI.registerElvenTradeRecipe (new ItemStack[] {output},inputStack);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addElven('<output> <input>')");
	}
}
