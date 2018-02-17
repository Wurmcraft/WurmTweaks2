package com.wurmcraft.wurmtweaks.script.support;

import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.bulk.manager.*;
import betterwithmods.util.InvUtils;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import com.wurmcraft.wurmtweaks.script.WurmScript;
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

public class BetterWithMods extends ModSupport {

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
		String[] input = verify (line,line.split (" ").length >= 2,"addCauldron('<output> <input>...");
		isValid (input[0]);
		List <ItemStack> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertS (input[index]));
		}
		CauldronManager.getInstance ().addRecipe (convertS (input[0]),inputStacks.toArray (new ItemStack[0]));
	}

	@ScriptFunction
	public void addStokedCauldron (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addStokedCauldron('<output> <input>...");
		isValid (input[0]);
		List <ItemStack> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertS (input[index]));
		}
		StokedCauldronManager.getInstance ().addRecipe (convertS (input[0]),inputStacks.toArray (new ItemStack[0]));
	}

	@ScriptFunction
	public void addCrucible (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addCrucible('<output> <input>...");
		isValid (input[0]);
		List <ItemStack> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertS (input[index]));
		}
		CrucibleManager.getInstance ().addRecipe (convertS (input[0]),inputStacks.toArray (new ItemStack[0]));
	}

	@ScriptFunction
	public void addStokedCrucible (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addStokedCrucible('<output> <input>...");
		isValid (input[0]);
		List <ItemStack> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertS (input[index]));
		}
		StokedCrucibleManager.getInstance ().addRecipe (convertS (input[0]),inputStacks.toArray (new ItemStack[0]));
	}

	@ScriptFunction
	public void addMill (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addMill('<output> <input>...')");
		List <ItemStack> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertS (input[index]));
		}
		MillManager.getInstance ().addRecipe (0,convertS (input[0]),inputStacks.toArray (new ItemStack[0]));

	}

	@ScriptFunction
	public void addWoodSaw (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addWoodSaw('<output> <input>')");
		isValid (input[0],input[1]);
		SawManager.WOOD_SAW.addRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addSteelSaw (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addSteelSaw('<output> <input>')");
		isValid (input[0],input[1]);
		SawManager.STEEL_SAW.addRecipe (convertS (input[1]),convertS (input[0]));
	}

	@ScriptFunction
	public void addFilteredHopper (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addFilteredHopper('<output> <input> <type>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		HopperInteractions.addHopperRecipe (new HopperInteractions.HopperRecipe (convertNI (input[2]),convertS (input[1]),convertS (input[0])) {
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
	}

	@ScriptFunction
	public void addAnvil (String line) {
		String[] input = verify (line,line.split (" ").length > 4,"addAnvil(<output> <style> <format>')");
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++) {
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
		}
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		int[] recipeSize = RecipeUtils.getRecipeSize (Arrays.copyOfRange (input,1,indexFirstVar));
		String[] recipeStyle = new String[recipeSize[1]];
		for (int index = 1; index < (recipeSize[1] + 1); index++) {
			StringBuilder temp = new StringBuilder (RecipeUtils.replaceLastTillDiff (input[index],WurmScript.SPACER));
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
					Ingredient stack = convertI (input[index + 1]);
					recipeFormat.put (input[index].charAt (0),stack);
					if (stack == Ingredient.EMPTY)
						invalidFormat.put (input[index].charAt (0),input[index + 1]);
					index++;
				} else
					recipeFormat.put (input[index].charAt (0),Ingredient.EMPTY);
			} else if (input[index].length () > 1) {
				script.info ("Invalid Format, '" + input[index] + " Should Be A Single Character!");
				return;
			}
		boolean valid = true;
		for (Character ch : recipeFormat.keySet ())
			if (recipeFormat.get (ch) == Ingredient.EMPTY) {
				script.info ("Invalid Stack '" + ch + "' " + invalidFormat.getOrDefault (ch,""));
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
	}
}
