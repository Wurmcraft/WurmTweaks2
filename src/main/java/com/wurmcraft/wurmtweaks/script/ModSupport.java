package com.wurmcraft.wurmtweaks.script;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class ModSupport implements IModSupport {

	@Override
	public String getModID () {
		return "invalid";
	}

	@Override
	public void init () {

	}

	protected void isValid (EnumInputType type,String... i) {
		for (String input : i)
			switch (type) {
				case ITEM:
					try {
						Preconditions.checkArgument (StackHelper.convert (input) != ItemStack.EMPTY || input.equalsIgnoreCase ("<empty>"),"Invalid Item '%s'",input);
					} catch (IllegalArgumentException e) {
						WurmScript.info (e.getMessage (),null);
					}
					break;
				case FLUID:
					try {
						Preconditions.checkArgument (StackHelper.convertToFluid (input) != null,"Invalid Fluid '%s'",input);
					} catch (IllegalArgumentException e) {
						WurmScript.info (e.getMessage (),null);
					}
					break;
				case INTEGER:
					try {
						Integer.parseInt (input);
					} catch (NumberFormatException f) {
						WurmScript.info ("Invalid Number '" + input + "'",null);
					}
					break;
				case FLOATNG:
					try {
						Float.parseFloat (input);
					} catch (NumberFormatException f) {
						WurmScript.info ("Invalid Number '" + input + "'",null);
					}
					break;
				case BOOLEAN:
					try {
						Boolean.parseBoolean (input);
					} catch (NumberFormatException f) {
						WurmScript.info ("Invalid Boolean (True/False) '" + input + "'",null);
					}
					break;
				case STRING:
				default:
					try {
						Preconditions.checkNotNull (input,"Invalid String '%s'",input,null);
					} catch (IllegalArgumentException e) {
						WurmScript.info (e.getMessage (),null);
						break;
					}
			}
	}

	protected void isValid (String... stack) {
		isValid (EnumInputType.ITEM,stack);
	}

	protected String[] verify (String line,boolean test,String msg) {
		try {
			Preconditions.checkArgument (test,msg);
		} catch (IllegalArgumentException e) {
			WurmScript.info (e.getMessage (),null);
		}
		return line.split (" ");
	}

	protected ItemStack convertS (String stack) {
		return StackHelper.convert (stack);
	}

	protected float convertNF (String num) {
		try {
			return Float.parseFloat (num);
		} catch (NumberFormatException e) {
			WurmScript.info ("Invalid Number '" + num + "'",null);
		}
		return -1;
	}

	protected Boolean convertB (String bol) {
		try {
			return Boolean.parseBoolean (bol);
		} catch (NumberFormatException e) {
			WurmScript.info ("Invalid Boolean '" + bol + "'",null);
		}
		return null;
	}

	protected FluidStack convertF (String fluid) {
		return StackHelper.convertToFluid (fluid);
	}

	protected Ingredient convertI (String item) {
		return StackHelper.convert (item,null);
	}

	protected int convertNI (String num) {
		try {
			return Integer.parseInt (num);
		} catch (NumberFormatException e) {
			WurmScript.info ("Invalid Number '" + num + "'",null);
		}
		return -1;
	}
}
