package com.wurmcraft.wurmtweaks.script.support;

import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_botanic.VMBotanic;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_ore.VMOre;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_res.VMRes;
import com.valkyrieofnight.valkyrielib.lib.stack.WeightedItemStack;
import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;

public class EnvironmentalTech extends ModSupport {

	@Override
	public String getModID () {
		return "environmentaltech";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			VMBotanic.getInstance ().T1.getList ().clear ();
			VMBotanic.getInstance ().T1.getTargeters ().clear ();
			VMBotanic.getInstance ().T2.getList ().clear ();
			VMBotanic.getInstance ().T2.getTargeters ().clear ();
			VMBotanic.getInstance ().T3.getList ().clear ();
			VMBotanic.getInstance ().T3.getTargeters ().clear ();
			VMBotanic.getInstance ().T4.getList ().clear ();
			VMBotanic.getInstance ().T4.getTargeters ().clear ();
			VMBotanic.getInstance ().T5.getList ().clear ();
			VMBotanic.getInstance ().T5.getTargeters ().clear ();
			VMBotanic.getInstance ().T6.getList ().clear ();
			VMBotanic.getInstance ().T6.getTargeters ().clear ();
			VMRes.getInstance ().T1.getList ().clear ();
			VMRes.getInstance ().T1.getTargeters ().clear ();
			VMRes.getInstance ().T2.getList ().clear ();
			VMRes.getInstance ().T2.getTargeters ().clear ();
			VMRes.getInstance ().T3.getList ().clear ();
			VMRes.getInstance ().T3.getTargeters ().clear ();
			VMRes.getInstance ().T4.getList ().clear ();
			VMRes.getInstance ().T4.getTargeters ().clear ();
			VMRes.getInstance ().T5.getList ().clear ();
			VMRes.getInstance ().T5.getTargeters ().clear ();
			VMRes.getInstance ().T6.getList ().clear ();
			VMRes.getInstance ().T6.getTargeters ().clear ();
			VMOre.getInstance ().VOM_T1.getList ().clear ();
			VMOre.getInstance ().VOM_T1.getTargeters ().clear ();
			VMOre.getInstance ().VOM_T2.getList ().clear ();
			VMOre.getInstance ().VOM_T2.getTargeters ().clear ();
			VMOre.getInstance ().VOM_T3.getList ().clear ();
			VMOre.getInstance ().VOM_T3.getTargeters ().clear ();
			VMOre.getInstance ().VOM_T4.getList ().clear ();
			VMOre.getInstance ().VOM_T4.getTargeters ().clear ();
			VMOre.getInstance ().VOM_T5.getList ().clear ();
			VMOre.getInstance ().VOM_T5.getTargeters ().clear ();
			VMOre.getInstance ().VOM_T6.getList ().clear ();
			VMOre.getInstance ().VOM_T6.getTargeters ().clear ();
		}
	}

	@ScriptFunction
	public void addBotanicMiner (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addBotanicMiner('<stack> <color> <weight> <tier>'");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1],input[2]);
		switch (convertNI (input[3])) {
			case (1):
				VMBotanic.getInstance ().T1.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (2):
				VMBotanic.getInstance ().T2.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (3):
				VMBotanic.getInstance ().T3.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (4):
				VMBotanic.getInstance ().T4.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (5):
				VMBotanic.getInstance ().T5.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (6):
				VMBotanic.getInstance ().T6.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
		}
	}

	@ScriptFunction
	public void addResourceMiner (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addResourceMiner('<stack> <color> <weight> <tier>'");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1],input[2]);
		switch (convertNI (input[3])) {
			case (1):
				VMRes.getInstance ().T1.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (2):
				VMRes.getInstance ().T2.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (3):
				VMRes.getInstance ().T3.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (4):
				VMRes.getInstance ().T4.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (5):
				VMRes.getInstance ().T5.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (6):
				VMRes.getInstance ().T6.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
		}
	}

	@ScriptFunction (link = "laser", linkSize = {4})
	public void addOreMiner (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addOreMiner('<stack> <color> <weight> <tier>'");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1],input[2]);
		switch (convertNI (input[3])) {
			case (1):
				VMOre.getInstance ().VOM_T1.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (2):
				VMOre.getInstance ().VOM_T2.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (3):
				VMOre.getInstance ().VOM_T3.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (4):
				VMOre.getInstance ().VOM_T4.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (5):
				VMOre.getInstance ().VOM_T5.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
			case (6):
				VMOre.getInstance ().VOM_T6.addResource (new WeightedItemStack (convertS (input[0]),convertNI (input[2])),input[1]);
				break;
		}
	}
}
