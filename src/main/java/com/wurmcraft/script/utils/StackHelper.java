package com.wurmcraft.script.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.wurmcraft.script.utils.StackSettings.*;

/**
 Converts Items, Ingredents and Ore Dictionary into a usable form
 */
public class StackHelper {

	private final Thread mainThread;
	public HashMap <String, ItemStack> cacheItems = new HashMap <> ();
	private boolean cache;

	/**
	 @param mainThread Main Minecraft thread (Used by threads to sync)
	 @param useCache Create a cache to improve preformance
	 */
	public StackHelper (Thread mainThread,boolean useCache) {
		this.mainThread = mainThread;
		this.cache = useCache;
	}

	/**
	 Converts an String item into an ItemStack for use in recipes

	 @param item String to convert back into an Item

	 @return ItemStack version of the item
	 */
	public ItemStack convert (String item) {
		if (cache && cacheItems.containsKey (item))
			return cacheItems.get (item);
		if (item.equals (EMPTY_STACK.toString ()))
			return ItemStack.EMPTY;
		if (!item.isEmpty () && item.startsWith (FRONT.toString ()) & item.endsWith (BACK.toString ()))
			if (isOreEntry (item)) {
				System.out.println ("[Debug]: OreDictionary was converted to ItemStack, this may cause issues with the recipe!");
				return getOreItems (item).get (0); // Use an Ingredent instead of an ItemStack to allow for full OreDict Support
			} else if (item.contains (":") && item.contains (STACK_SIZE.toString ()) && item.length () >= 9) {
				item = item.replaceAll (SPACE.toString ()," ");
				ResourceLocation itemLookup = new ResourceLocation (item.substring (item.indexOf (STACK_SIZE.toString ()) + 1,item.indexOf (":")),item.contains (META.toString ()) ? item.substring (item.indexOf (":")+1,item.indexOf (META.toString ())) : item.substring (item.indexOf (":")+1,item.indexOf (BACK.toString ())));
				Item validItem = getItem (itemLookup);
				if (validItem != null) {
					int stackSize = Integer.valueOf (item.substring (1,item.indexOf (STACK_SIZE.toString ())));
					int meta = item.contains ("@") ? Integer.valueOf (item.substring (item.indexOf (META.toString ()) + 1,(item.contains (NBT.toString ()) ? item.indexOf (NBT.toString ()) : item.indexOf (BACK.toString ())))) : 0;
					ItemStack stack = new ItemStack (validItem,stackSize,meta);
					if (item.contains (NBT.toString ()))
						try {
							stack.setTagCompound (JsonToNBT.getTagFromJson (item.substring (item.indexOf (NBT.toString ()) + 1,item.length () - 1)));
						} catch (NBTException e) {
							System.out.println ("Invalid NBT '" + item.substring (item.indexOf (NBT.toString ()) + 1,item.length () - 1));
						}
					if (cache)
						cacheItems.put (item,stack);
					return stack;
				}
			}
		return null;
	}

	/**
	 Converts an String item into an Ingredient for use in recipes

	 @param item Item / OreEntry to convert to Ingredent

	 @return Ingredent form of the item
	 */
	public Ingredient convertIngredient (String item) {
		if (isOreEntry (item))
			return new IngredientWrapper (getOreItems (item).toArray (new ItemStack[0]));
		return new IngredientWrapper (convert (item));
	}

	/**
	 Converts an String FluidStack into one for use in recipes

	 @param fluid String FluidStack to convert

	 @return FluidStack version of the String
	 */
	public FluidStack convertFluid (String fluid) {
		if (fluid.startsWith (FRONT.toString () + FLUID.toString ()) && fluid.endsWith (BACK.toString ())) {
			Fluid fluidType = getFluid (fluid.substring (1,fluid.length () - 1));
			int amount = Integer.parseInt (fluid.substring (fluid.indexOf (STACK_SIZE.toString ()) + 1,fluid.length () - 1));
			return new FluidStack (fluidType,amount);
		}
		return null;
	}

	/**
	 Converts / Parses to get ItemStack or String ready for use within the scripting system

	 @param stack ItemStack or String to convert to be used within the scripting system
	 */
	public String convert (Object stack) {
		if (stack instanceof String) {
			String item = (String) stack;
			if (item.length () >= 3 && isOreEntry (item))
				return item.contains (FRONT.toString ()) ? item : FRONT + item + BACK;
			return item;
		} else if (stack instanceof ItemStack)
			return convertToString ((ItemStack) stack);
		else if (((Ingredient) stack).getMatchingStacks ().length > 0)
			return convertToString (((Ingredient) stack).getMatchingStacks ()[0]);
		return "";
	}

	/**
	 Converts a ItemStack into its String version

	 @param stack Stack to convert to a String

	 @return String version of the ItemStack
	 */
	private String convertToString (ItemStack stack) {
		String temp = FRONT.toString () + stack.getCount () + STACK_SIZE.toString () + stack.getItem ().getRegistryName ().getResourceDomain () + ":" + stack.getItem ().getRegistryName ().getResourcePath ();
		if (stack.getItemDamage () > 0)
			temp = temp + META + stack.getItemDamage ();
		if (stack.hasTagCompound ())
			return temp.replaceAll (" ",SPACE.toString ()) + NBT.toString () + stack.getTagCompound () + BACK.toString ();
		return temp.replaceAll (" ",SPACE.toString ()) + BACK.toString ();
	}


	/**
	 Checks if a String is a valid OreDict entry

	 @param entry Entry to check

	 @return If entry is a valid OreDict entry
	 */
	public boolean isOreEntry (String entry) {
		return !entry.isEmpty () && entry.length () > 3 && entry.startsWith (FRONT.toString ()) && entry.endsWith (BACK.toString ()) && oreExists (entry);
	}

	/**
	 Gets a list of Item's based on a OreDict Entry

	 @param entry Entry to find Items

	 @return List of Items for the OreDict Entry
	 */
	private NonNullList <ItemStack> getOreItems (String entry) {
		synchronized (mainThread) {
			return OreDictionary.getOres (entry.substring (1,entry.length () - 1));
		}
	}

	/**
	 Create a list of Ore Entries based on the ItemStack

	 @param stack Stack to get OreEntries

	 @return Array of OReDict Entries
	 */
	private String[] getOreEntrys (ItemStack stack) {
		synchronized (mainThread) {
			int[] oreIds = OreDictionary.getOreIDs (stack);
			List <String> oreEntries = new ArrayList <> ();
			for (int oreID : oreIds)
				oreEntries.add (OreDictionary.getOreName (oreID));
			return oreEntries.toArray (new String[0]);
		}
	}

	/**
	 Checks if a OreEntry exists

	 @param entry Entry to check if it exists

	 @return If the entry exists (May not be loaded yet, causes issues)
	 */
	private boolean oreExists (String entry) {
		synchronized (mainThread) {
			return OreDictionary.doesOreNameExist (entry.substring (1,entry.length () - 1));
		}
	}

	/**
	 Looks up a Item based on its modid:name

	 @param item Item to lookup

	 @return Item from ResourceLocation
	 */
	private Item getItem (ResourceLocation item) {
		synchronized (mainThread) {
			return ForgeRegistries.ITEMS.getValue (item);
		}
	}

	/**
	 Looks up a Fluid based on its name

	 @param name Fluid to lookup

	 @return Fluid from String
	 */
	private Fluid getFluid (String name) {
		synchronized (mainThread) {
			return FluidRegistry.getFluid (name);
		}
	}
}
