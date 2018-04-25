package com.wurmcraft.wurmtweaks.utils;

import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StackHelper {

	private static HashMap <String, ItemStack> cachedItems = new HashMap <> ();

	public static ItemStack convert (String input) {
		if (ConfigHandler.cache) {
			ItemStack stack = cachedItems.getOrDefault (input,ItemStack.EMPTY);
			if (stack != ItemStack.EMPTY)
				return stack;
		}
		if (input.equalsIgnoreCase (ConfigHandler.startChar + ConfigHandler.emptyStack + ConfigHandler.endChar))
			return ItemStack.EMPTY;
		String item = input.replaceAll (ConfigHandler.spaceChar," ");
		boolean isOreEntry = isOreDictionaryEntry (item);
		if (isOreEntry)
			return OreDictionary.getOres (item.substring (1,item.length () - 1)).get (0);
		else if (item != null && item.length () > 0 && item.contains (":") && item.startsWith (ConfigHandler.startChar) && item.endsWith (ConfigHandler.endChar)) {
			ResourceLocation itemLookup = new ResourceLocation (item.substring (item.indexOf (ConfigHandler.sizeChar) + 1,item.indexOf (":")),item.substring (item.indexOf (":") + 1,item.indexOf (ConfigHandler.metaChar)));
			Item validItem = ForgeRegistries.ITEMS.getValue (itemLookup);
			if (validItem != null) {
				int stackSize = Integer.valueOf (item.substring (1,item.indexOf (ConfigHandler.sizeChar)));
				int meta = Integer.valueOf (item.substring (item.indexOf (ConfigHandler.metaChar) + 1,item.contains (ConfigHandler.nbtChar) ? item.indexOf (ConfigHandler.nbtChar) : item.indexOf (ConfigHandler.endChar)));
				ItemStack stack = new ItemStack (validItem,stackSize,meta);
				if (item.contains (ConfigHandler.nbtChar))
					try {
						stack.setTagCompound (JsonToNBT.getTagFromJson (item.substring (item.indexOf (ConfigHandler.nbtChar) + 1,item.length () - 1)));
					} catch (NBTException e) {
						WurmScript.info ("Invalid NBT '" + item.substring (item.indexOf (ConfigHandler.nbtChar) + 1,item.length () - 1) + "'" + " for the item '" + item + "'");
					}
				if (ConfigHandler.cache)
					cachedItems.putIfAbsent (input,stack);
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	public static Ingredient convert (String item,Void empty) {
		if (isOreDictionaryEntry (item))
			return new IngredientHelper (OreDictionary.getOres (item.substring (1,item.length () - 1)).toArray (new ItemStack[0]));
		return new IngredientHelper (convert (item));
	}

	// Only Supports Simple / Ore Dict Ingredient's
	public static String convert (Ingredient ingredient) {
		if (ingredient.isSimple () && ingredient.getMatchingStacks ().length == 1)
			return convert (ingredient.getMatchingStacks ()[0],false);
		else if (ingredient.getMatchingStacks ().length > 1) {
			List <String> tempData = new ArrayList <> ();
			for (ItemStack item : ingredient.getMatchingStacks ())
				for (int id : OreDictionary.getOreIDs (item))
					if (tempData.size () == 0)
						tempData.add (OreDictionary.getOreName (id));
					else if (tempData.contains (OreDictionary.getOreName (id))) {
						tempData.clear ();
						tempData.add (OreDictionary.getOreName (id));
					} else
						return "Ingredient '" + ingredient.getValidItemStacksPacked ().toArray (new int[0]) + "' is complex and it's items have no known OreDictionary Entry!";
		} else
			return "WurmScript does not support WildCards for ItemStacks (Use the OreDictionary Instead)";
		return "A Unknown Error Has Occured With Ingredient Translation!";
	}

	public static String convert (ItemStack stack,int bucketCheck) {
		if (bucketCheck == 1 && stack.getItem () instanceof ItemBucket || bucketCheck == 1 && stack.getItem () == ForgeModContainer.getInstance ().universalBucket) {
			FluidStack fluid = new FluidBucketWrapper (stack).getFluid ();
			return convert (fluid);
		}
		return convert (stack,false);
	}

	public static String convert (FluidStack stack) {
		if (stack != null && stack.getUnlocalizedName () != null && stack.getUnlocalizedName ().length () > 0)
			return ConfigHandler.startChar + ConfigHandler.fluidChar + stack.amount + ConfigHandler.sizeChar + stack.getFluid ().getName () + ConfigHandler.endChar;
		else
			return "Invalid FluidStack " + stack.getUnlocalizedName ();
	}

	public static FluidStack convertToFluid (String fluidStack) {
		if (fluidStack.startsWith (ConfigHandler.startChar + ConfigHandler.fluidChar)) {
			int amount = Integer.parseInt (fluidStack.substring (fluidStack.indexOf (ConfigHandler.startChar + ConfigHandler.fluidChar) + 2,fluidStack.indexOf (ConfigHandler.sizeChar)));
			Fluid fluid = FluidRegistry.getFluid (fluidStack.substring (fluidStack.indexOf (ConfigHandler.sizeChar) + 1,fluidStack.indexOf (ConfigHandler.endChar)));
			if (fluid != null)
				return new FluidStack (fluid,amount);
		}
		return null;
	}

	public static String convert (Object stack,boolean ore) {
		if (ore && stack instanceof String)
			if (((String) stack).length () > 0 && OreDictionary.doesOreNameExist (((String) stack)))
				return ConfigHandler.startChar + stack + ConfigHandler.endChar;
			else
				return "Ore Name '" + stack + "' does not exist!";
		else if (!ore && stack instanceof ItemStack && !((ItemStack) stack).isEmpty ()) {
			ItemStack item = (ItemStack) stack;
			String temp = ConfigHandler.startChar + item.getCount () + ConfigHandler.sizeChar + item.getItem ().getRegistryName ().getResourceDomain () + ":" + item.getItem ().getRegistryName ().getResourcePath () + ConfigHandler.metaChar + ((ItemStack) stack).getItemDamage ();
			if (item.hasTagCompound ())
				return temp.replaceAll (" ",ConfigHandler.spaceChar) + ConfigHandler.nbtChar + item.getTagCompound () + ConfigHandler.endChar;
			return temp.replaceAll (" ",ConfigHandler.spaceChar) + ConfigHandler.endChar;
		}
		return "'" + stack + "' is a Invalid / Empty Item!";
	}

	private static boolean isOreDictionaryEntry (String stack) {
		return stack != null && stack.length () > 2 && stack.startsWith (ConfigHandler.startChar) && stack.endsWith (ConfigHandler.endChar) && stack.substring (1,stack.length () - 1).length () > 0 && OreDictionary.doesOreNameExist (stack.substring (1,stack.length () - 1));
	}

	public static boolean isSameIgnoreSize (ItemStack a,ItemStack b) {
		return a.getItem ().equals (b.getItem ()) && a.getTagCompound () == b.getTagCompound () && ((a.getItemDamage () == b.getItemDamage ()) || a.getItemDamage () == Short.MAX_VALUE);
	}
}
