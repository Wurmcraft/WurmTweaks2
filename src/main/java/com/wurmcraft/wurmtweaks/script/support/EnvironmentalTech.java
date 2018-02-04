package com.wurmcraft.wurmtweaks.script.support;

import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_botanic.VMBotanic;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_ore.VMOre;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_res.VMRes;
import com.valkyrieofnight.valkyrielib.lib.stack.WeightedItemStack;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

public class EnvironmentalTech implements IModSupport {


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
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY) {
				try {
					int weight = Integer.parseInt (input[1]);
					if (weight > 0) {
						try {
							int tier = Integer.parseInt (input[2]);
							if (tier > 0 && tier < 7) {
								String color = input[3];
								switch (tier) {
									case (1):
										VMBotanic.getInstance ().T1.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (2):
										VMBotanic.getInstance ().T2.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (3):
										VMBotanic.getInstance ().T3.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (4):
										VMBotanic.getInstance ().T4.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (5):
										VMBotanic.getInstance ().T5.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (6):
										VMBotanic.getInstance ().T6.addResource (new WeightedItemStack (stack,weight),color);
										break;
								}
							} else
								WurmScript.info ("Number Must Be At Least 0 And A Maximum Of 7!");
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Stack '" + input[2] + "'");
						}
					} else
						WurmScript.info ("Number Must Be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[0] + "'");
				}
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addBotanicMiner('<stack> <weight> <tier> <color>)");
	}

	@ScriptFunction
	public void addResourceMiner (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY) {
				try {
					int weight = Integer.parseInt (input[1]);
					if (weight > 0) {
						try {
							int tier = Integer.parseInt (input[2]);
							if (tier > 0 && tier < 7) {
								String color = input[3];
								switch (tier) {
									case (1):
										VMRes.getInstance ().T1.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (2):
										VMRes.getInstance ().T2.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (3):
										VMRes.getInstance ().T3.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (4):
										VMRes.getInstance ().T4.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (5):
										VMRes.getInstance ().T5.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (6):
										VMRes.getInstance ().T6.addResource (new WeightedItemStack (stack,weight),color);
										break;
								}
							} else
								WurmScript.info ("Number Must Be At Least 0 And A Maximum Of 7!");
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Stack '" + input[2] + "'");
						}
					} else
						WurmScript.info ("Number Must Be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[0] + "'");
				}
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addResourceMiner('<stack> <weight> <tier> <color>)");
	}

	@ScriptFunction (link = "laser", linkSize = {4})
	public void addOreMiner (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY) {
				try {
					int weight = Integer.parseInt (input[2]);
					if (weight > 0) {
						try {
							int tier = Integer.parseInt (input[1]);
							if (tier > 0 && tier < 7) {
								String color = input[3];
								switch (tier) {
									case (1):
										VMOre.getInstance ().VOM_T1.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (2):
										VMOre.getInstance ().VOM_T2.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (3):
										VMOre.getInstance ().VOM_T3.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (4):
										VMOre.getInstance ().VOM_T4.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (5):
										VMOre.getInstance ().VOM_T5.addResource (new WeightedItemStack (stack,weight),color);
										break;
									case (6):
										VMOre.getInstance ().VOM_T6.addResource (new WeightedItemStack (stack,weight),color);
										break;
								}
							} else
								WurmScript.info ("Number Must Be At Least 0 And A Maximum Of 7!");
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[1] + "'");
						}
					} else
						WurmScript.info ("Number Must Be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[2] + "'");
				}
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addOreMiner('<stack> <weight> <color> tier>)");
	}
}
