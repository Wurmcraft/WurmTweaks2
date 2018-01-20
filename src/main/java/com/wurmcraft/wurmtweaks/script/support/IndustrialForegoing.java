package com.wurmcraft.wurmtweaks.script.support;

import com.buuz135.industrial.api.IndustrialForegoingHelper;
import com.buuz135.industrial.api.recipe.BioReactorEntry;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import com.buuz135.industrial.api.recipe.ProteinReactorEntry;
import com.buuz135.industrial.api.recipe.SludgeEntry;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

public class IndustrialForegoing implements IModSupport {

	@Override
	public String getModID () {
		return "industrialforegoing";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllRecipes) {
			BioReactorEntry.BIO_REACTOR_ENTRIES.clear ();
			SludgeEntry.SLUDGE_RECIPES.clear ();
			ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES.clear ();
			LaserDrillEntry.LASER_DRILL_ENTRIES.clear ();
		}
	}

	@ScriptFunction
	public void addBioReactor (String line) {
		String[] input = line.split (" ");
		if (input.length == 1) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY)
				IndustrialForegoingHelper.addBioReactorEntry (new BioReactorEntry (stack));
			else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addBioReactor('<stack>')");
	}

	@ScriptFunction
	public void addSludgeRefiner (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY) {
				try {
					int weight = Integer.parseInt (input[1]);
					IndustrialForegoingHelper.addSludgeRefinerEntry (new SludgeEntry (stack,weight));
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addSludgeRefiner('<stack> <weight>')");
	}

	@ScriptFunction
	public void addProteinReactor (String line) {
		String[] input = line.split (" ");
		if (input.length == 1) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY)
				IndustrialForegoingHelper.addProteinReactorEntry (new ProteinReactorEntry (stack));
			else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addProteinReactor('<stack>')");
	}

	// TODO Possible Cross- Mod Support
	// 'Ore-Gen Registry'?
	@ScriptFunction
	public void addLaser (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			try {
				int colorMeta = Integer.parseInt (input[0]);
				ItemStack stack = StackHelper.convert (input[1],null);
				if (stack != ItemStack.EMPTY) {
					try {
						int weight = Integer.parseInt (input[2]);
						IndustrialForegoingHelper.addLaserDrillEntry (new LaserDrillEntry (colorMeta,stack,weight));
					} catch (NumberFormatException f) {
						WurmScript.info ("Invalid Number '" + input[2]);
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} catch (NumberFormatException e) {
				WurmScript.info ("Invalid Number '" + input[0] + "'");
			}
		} else
			WurmScript.info ("addLaser('<color> <stack> <weight>')");
	}
}
