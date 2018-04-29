package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.event.ScriptEvents;
import com.wurmcraft.wurmtweaks.script.ModSupport;

import java.util.ArrayList;
import java.util.List;

public class Events extends ModSupport {

	@Override
	public String getModID () {
		return "events";
	}

	@Override
	public void init () {
	}

	@ScriptFunction
	public void disablePickup (String line) {
		String[] input = verify (line,line.split (" ").length == 1,"disablePickup('<stack>')");
		isValid (input[0]);
		ScriptEvents.addThrowCancelEvent (convertS (input[0]));
	}

	@ScriptFunction
	public void convertPickup (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"convertPickup('<from> <to>')");
		isValid (input[0],input[1]);
		ScriptEvents.addPickupConversion (convertS (input[0]),convertS (input[1]));
	}

	@ScriptFunction
	public void addTooltip (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addTooltip('<stack> <tooltipA> <tooltipB>...')");
		isValid (input[0]);
		List <String> tooltip = new ArrayList <> ();
		for (int index = 1; index < input.length; index++)
			tooltip.add (input[index].replaceAll ("&","\u00A7").replaceAll ("_"," "));
		ScriptEvents.addToolTipEntry (convertS (input[0]),tooltip.toArray (new String[0]));
	}
}
