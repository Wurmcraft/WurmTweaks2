package com.wurmcraft.wurmtweaks.script;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.WurmTweaks;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class ModSupport implements IModSupport {

	protected WurmScript script = WurmTweaks.dl.wurmScript;

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
						Preconditions.checkArgument (StackHelper.convert (input) != ItemStack.EMPTY,"Invalid Item '%s'",input);
					} catch (IllegalArgumentException e) {
						script.info (e.getMessage ());
					}
					break;
				case FLUID:
					try {
						Preconditions.checkArgument (StackHelper.convertToFluid (input) != null,"Invalid Fluid '%s'",input);
					} catch (IllegalArgumentException e) {
						script.info (e.getMessage ());

					}
					break;
				case INTEGER:
					try {
						Integer.parseInt (input);
					} catch (NumberFormatException f) {
						script.info ("Invalid Number '" + input + "'");
					}
					break;
				case FLOATNG:
					try {
						Float.parseFloat (input);
					} catch (NumberFormatException f) {
						script.info ("Invalid Number '" + input + "'");
					}
					break;
				case BOOLEAN:
					try {
						Boolean.parseBoolean (input);
					} catch (NumberFormatException f) {
						script.info ("Invalid Boolean (True/False) '" + input + "'");
					}
					break;
				case STRING:
				default:
					try {
						Preconditions.checkNotNull (input,"Invalid String '%s'",input);
					} catch (IllegalArgumentException e) {
						script.info (e.getMessage ());
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
			script.info (e.getMessage ());
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
			script.info ("Invalid Number '" + num + "'");
		}
		return -1;
	}

	protected Boolean convertB (String bol) {
		try {
			return Boolean.parseBoolean (bol);
		} catch (NumberFormatException e) {
			script.info ("Invalid Boolean '" + bol + "'");
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
			script.info ("Invalid Number '" + num + "'");
		}
		return -1;
	}
}
