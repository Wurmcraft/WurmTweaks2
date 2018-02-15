package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.event.ScriptEvents;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Events implements IModSupport {

	@Override
	public String getModID () {
		return "events";
	}

	@Override
	public void init () {
	}

	@ScriptFunction
	public void disablePickup (String line) {
		String[] input = line.split (" ");
		if (input.length == 1) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY) {
				ScriptEvents.addThrowCancelEvent (stack);
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("disablePickup('<stack>')");
	}

	@ScriptFunction
	public void convertPickup (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack from = StackHelper.convert (input[0],null);
			if (from != ItemStack.EMPTY) {
				ItemStack to = StackHelper.convert (input[1],null);
				if (to != ItemStack.EMPTY) {
					ScriptEvents.addPickupConversion (from,to);
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("convertPickup('<from> <to>')");
	}

	@ScriptFunction
	public void addTooltip (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack item = StackHelper.convert (input[0],null);
			List <String> tooltip = new ArrayList <> ();
			for (int index = 1; index < input.length; index++)
				tooltip.add (input[index].replaceAll ("&","§").replaceAll ("_"," "));
			ScriptEvents.addToolTipEntry (item,tooltip.toArray (new String[0]));
		} else
			WurmScript.info ("addTooltip('<stack> <tipA> <tipB>...");
	}
}
