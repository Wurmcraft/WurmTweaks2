package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import com.wurmcraft.script.utils.tan.ArmorTemp;
import toughasnails.api.temperature.TemperatureHelper;

public class ToughAsNails extends SupportBase {

 public ToughAsNails() {
  super("toughasnails");
 }

 @Override
 public void init() {
  TemperatureHelper.registerTemperatureModifier(new ArmorTemp().new ArmorModifier());
 }

 @ScriptFunction
 public void addArmorTemp(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addArmorTemp('<armor> <amount>')");
  isValid(helper, input[0]);
  isValid(EnumInputType.INTEGER, helper, input[1]);
  ArmorTemp.setArmorTemp(convertStack(helper, input[0]), convertInteger(input[1]));
 }

 @Override
 public void finishSupport() {

 }
}
