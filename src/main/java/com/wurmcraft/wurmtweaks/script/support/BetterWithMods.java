package com.wurmcraft.wurmtweaks.script.support;


import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.gameplay.SawRecipes;
import betterwithmods.util.InvUtils;
import cofh.thermalexpansion.util.managers.machine.CrucibleManager;
import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class BetterWithMods extends ModSupport {

	@Override
	public String getModID () {
		return "betterwithmods";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			BWRegistry.WOOD_SAW.getRecipes ().clear ();
			BWRegistry.CAULDRON.getRecipes ().clear ();
			BWRegistry.CRUCIBLE.getRecipes ().clear ();
			HopperInteractions.RECIPES.clear ();
			AnvilCraftingManager.ANVIL_CRAFTING.clear ();
			AnvilCraftingManager.RECIPE_CACHE.clear ();
			BWRegistry.MILLSTONE.getRecipes ().clear ();
		}
	}

	@ScriptFunction
	public void addCauldron (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addCauldron('<output> <input>...");
		isValid (input[0]);
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		List <ItemStack> outputStacks = new ArrayList <> ();
		outputStacks.add (convertS (input[0]));
		BWRegistry.CAULDRON.addUnstokedRecipe (inputStacks,outputStacks);
	}

	@ScriptFunction
	public void addStokedCauldron (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addStokedCauldron('<output> <input>...");
		isValid (input[0]);
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		List <ItemStack> outputStacks = new ArrayList <> ();
		outputStacks.add (convertS (input[0]));
		BWRegistry.CAULDRON.addStokedRecipe (inputStacks,outputStacks);
	}

	@ScriptFunction
	public void addCrucible (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addCrucible('<output> <input>...");
		isValid (input[0]);
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		List <ItemStack> outputStacks = new ArrayList <> ();
		outputStacks.add (convertS (input[0]));
		BWRegistry.CRUCIBLE.addUnstokedRecipe (inputStacks,outputStacks);
	}

	@ScriptFunction
	public void addStokedCrucible (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addStokedCrucible('<output> <input>...')");
		isValid (input[0]);
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		List <ItemStack> outputStacks = new ArrayList <> ();
		outputStacks.add (convertS (input[0]));
		BWRegistry.CRUCIBLE.addStokedRecipe (inputStacks,outputStacks);
	}

	@ScriptFunction
	public void addMill (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addMill('<output> <input>...')");
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		List <ItemStack> outputStacks = new ArrayList <> ();
		outputStacks.add (convertS (input[0]));
		BWRegistry.MILLSTONE.addMillRecipe (inputStacks,outputStacks);
	}

	@ScriptFunction
	public void addWoodSaw (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addWoodSaw('<output> <input>')");
		isValid (input[0],input[1]);
		BWRegistry.WOOD_SAW.addRecipe (convertS (input[0]),convertS (input[1]));
	}

	@ScriptFunction
	public void addAnvil (String line) {
		String[] input = verify (line,line.split (" ").length > 4,"addAnvil(<output> <style> <format>')");
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		List <Object> recipe = RecipeUtils.getShapedRecipe (input);
		Preconditions.checkNotNull (recipe);
		AnvilCraftingManager.ANVIL_CRAFTING.add (new ShapedAnvilRecipe (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + recipe.hashCode ()),output,recipe.toArray (new Object[0])));
	}

	@ScriptFunction
	public void addBlockHeat (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addBlcokHeat(<block> <heat>')");
		isValid (input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		Block heatBlock = Block.getBlockFromItem (convertS (input[0]).getItem ());
		BWMHeatRegistry.addHeatSource (heatBlock,convertNI (input[1]));
	}
}
