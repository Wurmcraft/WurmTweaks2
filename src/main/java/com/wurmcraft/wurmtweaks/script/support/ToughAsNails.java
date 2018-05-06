package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.event.ScriptEvents;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.tan.ArmorTemp;
import toughasnails.api.temperature.TemperatureHelper;

public class ToughAsNails extends ModSupport {

	@Override
	public String getModID () {
		return "toughasnails";
	}

	@Override
	public void init () {
		TemperatureHelper.registerTemperatureModifier (new ArmorTemp ().new ArmorModifier ());
	}

	@ScriptFunction
	public void addArmorTemp (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addArmorTemp('<armor> <amount>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		ArmorTemp.setArmorTemp (convertS (input[0]),convertNI (input[1]));
		ScriptEvents.addToolTipEntry (convertS (input[0]), new String[] {"&c+" + input[1] + " Heat Resistance"});
	}
}
