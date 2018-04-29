package com.wurmcraft.wurmtweaks.script.support;

import com.buuz135.industrial.api.IndustrialForegoingHelper;
import com.buuz135.industrial.api.recipe.BioReactorEntry;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import com.buuz135.industrial.api.recipe.ProteinReactorEntry;
import com.buuz135.industrial.api.recipe.SludgeEntry;
import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;

public class IndustrialForegoing extends ModSupport {

	@Override
	public String getModID () {
		return "industrialforegoing";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			BioReactorEntry.BIO_REACTOR_ENTRIES.clear ();
			SludgeEntry.SLUDGE_RECIPES.clear ();
			ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES.clear ();
			LaserDrillEntry.LASER_DRILL_ENTRIES.clear ();
		}
	}

	@ScriptFunction
	public void addBioReactor (String line) {
		String[] input = verify (line,line.split (" ").length == 1,"addBioReactor('<stack>')");
		isValid (input[0]);
		IndustrialForegoingHelper.addBioReactorEntry (new BioReactorEntry (convertS (input[0])));
	}

	@ScriptFunction
	public void addSludgeRefiner (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addSludgeRefiner('<stack> <weight>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		IndustrialForegoingHelper.addSludgeRefinerEntry (new SludgeEntry (convertS (input[0]),convertNI (input[1])));
	}

	@ScriptFunction
	public void addProteinReactor (String line) {
		String[] input = verify (line,line.split (" ").length == 1,"addProteinReactor('<stack>')");
		isValid (input[0]);
		IndustrialForegoingHelper.addProteinReactorEntry (new ProteinReactorEntry (convertS (input[0])));
	}

	@ScriptFunction (link = "laser", linkSize = {3})
	public void addLaser (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addLaser('<stack> <color> <weight>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1],input[2]);
		IndustrialForegoingHelper.addLaserDrillEntry (new LaserDrillEntry (convertNI (input[1]),convertS (input[0]),convertNI (input[2])));
	}
}
