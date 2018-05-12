package com.wurmcraft.script.utils;

import com.google.common.base.Preconditions;
import com.wurmcraft.api.IModSupport;
import com.wurmcraft.api.Types;
import com.wurmcraft.script.exception.InvalidStackException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

/**
 Simplify creation of IModSupport

 @see IModSupport */
public abstract class SupportHelper implements IModSupport {

	protected String modid;

	/**
	 @param modid ID used by the mod and forge
	 */
	public SupportHelper (String modid) {
		this.modid = modid;
	}

	/**
	 @see IModSupport#finishSupport()
	 */
	@Override
	public abstract void finishSupport ();

	/**
	 Verifiys if the script line is valid and ready to proccess

	 @param line Script Line
	 @param condition Used to check if the script line is valid
	 @param errorMessage Message to display if condition is not met.

	 @return a String[] if the line is valid
	 */
	protected String[] validate (String line,boolean condition,String errorMessage) {
		if (condition)
			return line.split (" ");
		System.out.println (errorMessage);
		return new String[0];
	}

	/**
	 Converts a Scripts String into a Item

	 @param helper StackHelper Object from the thread
	 @param stack Script ItemStack String

	 @see StackHelper#convert(String)
	 */
	protected ItemStack convertStack (StackHelper helper,String stack) {
		return helper.convert (stack);
	}

	/**
	 Converts a Scripts String into a Ingedient

	 @param helper StackHelper Object from the thread
	 @param stack Script Ingredient String
	 */
	protected Ingredient convertIngredient (StackHelper helper,String stack) {
		return helper.convertIngredient (stack);
	}

	/**
	 Converts a Scripts String into a FluidStack

	 @param helper StackHelper Object from the thread
	 @param stack Script FluidStack String
	 */
	protected FluidStack convertFluidStack (StackHelper helper,String stack) {
		return helper.convertFluid (stack);
	}

	/**
	 Converts a Scripts String into a Float

	 @param stack StackHelper Object from the thread
	 */
	protected float convertFloat (String stack) {
		return Float.parseFloat (stack);
	}

	/**
	 Converts a Scripts String into a Float

	 @param stack StackHelper Object from the thread
	 */
	protected int convertInteger (String stack) {
		return Integer.parseInt (stack);
	}

	/**
	 Checks if a Script OreDict entry is valid

	 @param helper StackHelper Object from the thread
	 @param oreEntry Script oreDict Entry
	 */
	protected boolean isOre (StackHelper helper,String oreEntry) {
		return helper.isOreEntry (oreEntry);
	}

	/**
	 Checks if a script object is valid at a certain type

	 @param type Type of Object to test for
	 @param helper StackHelper Object from thread
	 @param item possible script Item
	 */
	protected void isValid (Types type,StackHelper helper,String... item) {
		for (String i : item)
			if (type.equals (Types.ITEMSTACK))
				checkNotNull (type,convertStack (helper,i),"Invalid ItemStack '" + i + "'");
			else if (type.equals (Types.INGREDENT))
				checkNotNull (type,convertIngredient (helper,i),"Invalid Ingredient '" + i + "'");
			else if (type.equals (Types.FLUIDSTACK))
				checkNotNull (type,convertFluidStack (helper,i),"Invalid FluidStack '" + i + "'");
			else if (type.equals (Types.ORE))
				Preconditions.checkArgument (isOre (helper,i),"Invalid Ore Entry '" + i + "'");
			else if (type.equals (Types.STRING))
				Preconditions.checkArgument (i.length () >= 3,"Invalid String '" + i + "'");
			else if (type.equals (Types.INTEGER))
				try {
					Integer.parseInt (i);
				} catch (NumberFormatException e) {
					System.out.println ("Invalid Integer '" + i + "'");
				}
			else if (type.equals (Types.FLOAT))
				try {
					Float.parseFloat (i);
				} catch (NumberFormatException e) {
					System.out.println ("Invalid Float '" + i + "'");
				}
	}

	/**
	 Checks if the Script Object is null and if so throw an exception
	 */
	private void checkNotNull (Types type,Object object,String errorMessage) throws InvalidStackException {
		if (object == null)
			throw new InvalidStackException (errorMessage);
	}

	/**
	 Simplifiyed version of

	 @param helper StackHelper Object from thread
	 @param item ItemStack Script Object

	 @see SupportHelper#isValid(Types,StackHelper,String...)
	 */
	protected void isValid (StackHelper helper,String... item) {
		isValid (Types.ITEMSTACK,helper,item);
	}

	/**
	 Helps the constructor by setting the supports modid

	 @return modid
	 */
	@Override
	public String modid () {
		return modid;
	}

	/**
	 @see IModSupport#init() ()
	 */
	@Override
	public abstract void init ();
}
