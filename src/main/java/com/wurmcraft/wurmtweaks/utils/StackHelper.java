package com.wurmcraft.wurmtweaks.utils;

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
import net.minecraftforge.fml.common.asm.transformers.ItemStackTransformer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class StackHelper {

	public static Ingredient convert (String item) {
		boolean isOreEntry = isOreDictionaryEntry (item);
		if (isOreEntry)
			return new IngredientHelper (OreDictionary.getOres (item.substring (1,item.length () - 1)).toArray (new ItemStack[0]));
		else if (item != null && item.length () > 0 && item.contains (":") && item.startsWith ("<") && item.endsWith (">")) {
			ResourceLocation itemLookup = new ResourceLocation (item.substring (item.indexOf ("x") + 1,item.indexOf (":")),item.substring (item.indexOf (":") + 1,item.indexOf ("@")));
			Item validItem = ForgeRegistries.ITEMS.getValue (itemLookup);
			if (validItem != null) {
				int stackSize = Integer.valueOf (item.substring (1,item.indexOf ("x")));
				int meta = Integer.valueOf (item.substring (item.indexOf ("@") + 1,item.contains ("^") ? item.indexOf ("^") : item.indexOf (">")));
				ItemStack stack = new ItemStack (validItem,stackSize,meta);
				if (item.contains ("^"))
					try {
						stack.setTagCompound (JsonToNBT.getTagFromJson (item.substring (item.indexOf ("^") + 1,item.length () - 1)));
					} catch (NBTException e) {
						LogHandler.info ("Invalid NBT '" + item.substring (item.indexOf ("^") + 1,item.length () - 1) + "'" + " for the item '" + item + "'");
					}
				return new IngredientHelper (stack);
			} else
				LogHandler.info ("Invalid Item '" + validItem + "'");
		}
		return Ingredient.EMPTY;
	}

	public static ItemStack convert (String item,Void empty) {
		if(item.equalsIgnoreCase ("empty"))
			return ItemStack.EMPTY;
		Ingredient ingredient = convert (item);
		if (ingredient.getMatchingStacks ().length > 0)
			return ingredient.getMatchingStacks ()[0];
		return ItemStack.EMPTY;
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

	public static String convert (ItemStack stack) {
		if (stack.getItem () instanceof ItemBucket || stack.getItem () == ForgeModContainer.getInstance().universalBucket) {
			FluidStack fluid = new FluidBucketWrapper (stack).getFluid ();
			return convert (fluid);
		}
		return convert (stack,false);
	}

	public static String convert (FluidStack stack) {
		if (stack != null && stack.getUnlocalizedName () != null && stack.getUnlocalizedName ().length () > 0)
			return "<*" + stack.amount + "x" + stack.getFluid ().getName () + ">";
		else
			return "Invalid FluidStack " + stack.getUnlocalizedName ();
	}

	public static FluidStack convertToFluid (String fluidStack) {
		if (fluidStack.startsWith ("<*")) {
			int amount = Integer.parseInt (fluidStack.substring (fluidStack.indexOf ("<*")+2,fluidStack.indexOf ("x")));
			Fluid fluid = FluidRegistry.getFluid (fluidStack.substring (fluidStack.indexOf ("x") + 1,fluidStack.indexOf (">")));
			if (fluid != null)
				return new FluidStack (fluid,amount);
		}
		return null;
	}

	public static String convert (Object stack,boolean ore) {
		if (ore && stack instanceof String)
			if (((String) stack).length () > 0 && OreDictionary.doesOreNameExist (((String) stack)))
				return "<" + stack + ">";
			else
				return "Ore Name '" + stack + "' does not exist!";
		else if (!ore && stack instanceof ItemStack && !((ItemStack) stack).isEmpty ()) {
			ItemStack item = (ItemStack) stack;
			String temp = "<" + item.getCount () + "x" + item.getItem ().getRegistryName ().getResourceDomain () + ":" + item.getItem ().getRegistryName ().getResourcePath () + "@" + ((ItemStack) stack).getItemDamage ();
			if (item.hasTagCompound ())
				return temp + "^" + item.getTagCompound () + ">";
			return temp + ">";
		}
		return "'" + stack + "' is a Invalid / Empty Item!";
	}

	private static boolean isOreDictionaryEntry (String stack) {
		return stack != null && stack.length () > 2 && stack.startsWith ("<") && stack.endsWith (">") && stack.substring (1,stack.length () - 1).length () > 0 && OreDictionary.doesOreNameExist (stack.substring (1,stack.length () - 1));
	}

	public static boolean isSameIgnoreSize (ItemStack a,ItemStack b) {
		return a.getItem ().equals (b.getItem ()) && a.getTagCompound () == b.getTagCompound () && a.getItemDamage () == b.getItemDamage ();
	}
}
