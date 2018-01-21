package com.wurmcraft.wurmtweaks.script.support;

import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.bulk.manager.*;
import betterwithmods.util.InvUtils;
import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import scala.collection.mutable.StringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BetterWithMods implements IModSupport {

	@Override
	public String getModID () {
		return "betterwithmods";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			SawManager.WOOD_SAW.getRecipes ().clear ();
			SawManager.STEEL_SAW.getRecipes ().clear ();
			CauldronManager.getInstance ().getRecipes ().clear ();
			StokedCauldronManager.getInstance ().getRecipes ().clear ();
			CrucibleManager.getInstance ().getRecipes ().clear ();
			StokedCrucibleManager.getInstance ().getRecipes ().clear ();
			HopperInteractions.RECIPES.clear ();
			AnvilCraftingManager.ANVIL_CRAFTING.clear ();
			AnvilCraftingManager.RECIPE_CACHE.clear ();
			MillManager.getInstance ().getRecipes ().clear ();
		}
	}

	@ScriptFunction
	public void addCauldron (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					CauldronManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCauldron('<output> <input>...')");
	}

	@ScriptFunction
	public void addStokedCauldron (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					StokedCauldronManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addStokedCauldron('<output> <input>...')");
	}

	@ScriptFunction
	public void addCrucible (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					CrucibleManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCrucible('<output> <input>...')");
	}

	@ScriptFunction
	public void addStokedCrucible (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					StokedCrucibleManager.getInstance ().addRecipe (output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addStokedCrucible('<output> <input>...')");
	}

	@ScriptFunction
	public void addMill (String line) {
		String[] input = line.split (" ");
		if (input.length >= 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				List <ItemStack> inputStacks = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
						inputStacks.add (StackHelper.convert (input[index],null));
					else
						WurmScript.info ("Invalid Input '" + input[index]);
				if (inputStacks.size () > 0)
					MillManager.getInstance ().addRecipe (0,output,inputStacks.toArray (new ItemStack[0]));
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addMill('<output> <input>...')");
	}

	@ScriptFunction
	public void addWoodSaw (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					SawManager.WOOD_SAW.addRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addSaw('<output <input>')");
	}

	@ScriptFunction
	public void addSteelSaw (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					SawManager.STEEL_SAW.addRecipe (inputStack,output);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addSaw('<output <input>')");
	}

	@ScriptFunction
	public void addFilteredHopper (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			try {
				int type = Integer.parseInt (input[0]);
				ItemStack output = StackHelper.convert (input[1],null);
				if (output != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						HopperInteractions.addHopperRecipe (new HopperInteractions.HopperRecipe (type,inputStack,output) {
							@Override
							public void craft (EntityItem inputStack,World world,BlockPos pos) {
								InvUtils.ejectStackWithOffset (world,inputStack.getPosition (),output.copy ());
								TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity (pos);
								assert tile != null;
								ItemStackHandler inventory = tile.inventory;
								ItemStack sand = secondaryOutput.get (world.rand.nextInt (secondaryOutput.size ())).copy ();
								if (!InvUtils.insert (inventory,sand,false).isEmpty ()) {
									InvUtils.ejectStackWithOffset (world,inputStack.getPosition (),sand);
								}
								onCraft (world,pos,inputStack);
							}
						});
					} else
						WurmScript.info ("Invalid Input '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Output '" + input[1] + "'");
			} catch (NumberFormatException e) {
				LogHandler.info ("Invalid Number '" + input[0] + "'");
			}
		} else
			WurmScript.info ("addFilteredHopper('<type> <output> <input>')");
	}

	@ScriptFunction
	public void addAnvil (String line) {
		String[] input = line.split (" ");
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++) {
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
		}
		if (input.length > 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				int[] recipeSize = getRecipeSize (Arrays.copyOfRange (input,1,indexFirstVar));
				String[] recipeStyle = new String[recipeSize[1]];
				for (int index = 1; index < (recipeSize[1] + 1); index++) {
					StringBuilder temp = new StringBuilder (replaceLastTillDiff (input[index],WurmScript.SPACER));
					if (temp.length () < recipeSize[0])
						while (temp.length () < recipeSize[0])
							temp.append (" ");
					recipeStyle[index - 1] = temp.toString ().replaceAll (WurmScript.SPACER + ""," ");
				}
				HashMap <Character, Ingredient> recipeFormat = new HashMap <> ();
				HashMap <Character, String> invalidFormat = new HashMap <> ();
				for (int index = (recipeSize[1] + 1); index < input.length; index++)
					if (!input[index].startsWith ("<") && input[index].length () == 1) {
						if ((index + 1) < input.length) {
							Ingredient stack = StackHelper.convert (input[index + 1]);
							recipeFormat.put (input[index].charAt (0),stack);
							if (stack == Ingredient.EMPTY)
								invalidFormat.put (input[index].charAt (0),input[index + 1]);
							index++;
						} else
							recipeFormat.put (input[index].charAt (0),Ingredient.EMPTY);
					} else if (input[index].length () > 1) {
						WurmScript.info ("Invalid Format, '" + input[index] + " Should Be A Single Character!");
						return;
					}
				boolean valid = true;
				for (Character ch : recipeFormat.keySet ())
					if (recipeFormat.get (ch) == Ingredient.EMPTY) {
						WurmScript.info ("Invalid Stack For '" + ch + "' " + invalidFormat.getOrDefault (ch,""));
						valid = false;
					}
				if (valid) {
					List <Object> temp = new ArrayList <> ();
					for (Character ch : recipeFormat.keySet ()) {
						temp.add (ch);
						temp.add (recipeFormat.get (ch));
					}
					List <Object> finalRecipe = new ArrayList <> ();
					finalRecipe.addAll (Arrays.asList (recipeStyle));
					finalRecipe.addAll (temp);
					AnvilCraftingManager.ANVIL_CRAFTING.add (new ShapedAnvilRecipe (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + finalRecipe.hashCode ()),output,finalRecipe.toArray (new Object[0])));
				}
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addAnvil(<output> <style> <format>");
	}

	public int[] getRecipeSize (String[] possibleStyle) {
		int[] temp = new int[possibleStyle.length];
		for (int index = 0; index < possibleStyle.length; index++)
			if (possibleStyle[index] != null && possibleStyle[index].length () > 0)
				temp[index] = replaceLastTillDiff (possibleStyle[index],WurmScript.SPACER).length ();
		int[] size = new int[2];
		size[0] = findLargest (temp);
		int height = 0;
		for (int t : temp)
			if (t > 0)
				height++;
		size[1] = height;
		return size;
	}

	private String replaceLastTillDiff (String line,char ch) {
		StringBuilder build = new StringBuilder (line);
		for (int index = line.length () - 1; index == 0; index--)
			if (line.charAt (index) == ch)
				build.deleteCharAt (index);
			else
				break;
		return build.toString ();
	}

	private int findLargest (int[] num) {
		int highest = 0;
		for (int i : num)
			if (i > highest)
				highest = i;
		return highest;
	}
}
