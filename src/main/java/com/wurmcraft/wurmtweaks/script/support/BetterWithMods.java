package com.wurmcraft.wurmtweaks.script.support;

import betterwithmods.common.registry.bulk.manager.*;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BetterWithMods implements IModSupport {

	@Override
	public String getModID () {
		return "betterwithmods";
	}

	@Override
	public void init () {
	}

	@ScriptFunction
	public void addCauldron (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					CauldronManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCauldron('<output> <input>...')");
	}

	@ScriptFunction
	public void addStokedCauldron (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					StokedCauldronManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addStokedCauldron('<output> <input>...')");
	}

	@ScriptFunction
	public void addCrucible (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					CrucibleManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCrucible('<output> <input>...')");
	}

	@ScriptFunction
	public void addStokedCrucible (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					StokedCrucibleManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addStokedCrucible('<output> <input>...')");
	}

	@ScriptFunction
	public void addMill (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					MillManager.getInstance ().addRecipe (0,output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addMill('<output> <input>...')");
	}
}
