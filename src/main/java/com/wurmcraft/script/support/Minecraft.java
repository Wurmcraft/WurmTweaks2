package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import com.wurmcraft.script.utils.recipe.DynamicShapedOreRecipe;
import com.wurmcraft.script.utils.recipe.DynamicShapelessOreRecipe;
import com.wurmcraft.script.utils.recipe.InvalidRecipe;
import com.wurmcraft.script.utils.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.*;

/**
 Adds support for Vanilla
 */
public class Minecraft extends SupportHelper {

	// Stores all the values generated on the worker threads
	private static List <IRecipe> scriptRecipes = Collections.synchronizedList (new ArrayList <> ());
	private static List <Object[]> furnaceRecipes = Collections.synchronizedList (new ArrayList <> ());
	private static List <Object[]> brewingRecipes = Collections.synchronizedList (new ArrayList <> ());
	private static List <Object[]> oreEntries = Collections.synchronizedList (new ArrayList <> ());

	public Minecraft () {
		super ("minecraft");
	}

	@ScriptFunction
	public void addShapeless (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length >= 2,"addShapeless('<output> [input]...')");
		isValid (helper,input[0]);
		List <Ingredient> recipeInputs = RecipeUtils.getShapelessItems (helper,Arrays.copyOfRange (input,1,input.length));
		ItemStack output = convertStack (helper,input[0]);
		DynamicShapelessOreRecipe recipe = new DynamicShapelessOreRecipe (new ResourceLocation (Global.MODID,"Recipes"),output,recipeInputs.toArray (new Ingredient[0]));
		recipe.setRegistryName (RecipeUtils.generateRecipeName (output,recipeInputs));
		scriptRecipes.add (recipe);
	}

	@ScriptFunction
	public void addShaped (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length >= 2,"addShaped('<output> (style)...')");
		isValid (helper,input[0]);
		List <Object> recipeStyle = RecipeUtils.getShapedRecipe (helper,input);
		ItemStack output = convertStack (helper,input[0]);
		DynamicShapedOreRecipe recipe = new DynamicShapedOreRecipe (new ResourceLocation (Global.MODID,"Recipes"),output,recipeStyle.toArray (new Object[0]));
		recipe.setRegistryName (RecipeUtils.generateRecipeName (output,recipe));
		scriptRecipes.add (recipe);
	}

	@ScriptFunction
	public void addFurnace (StackHelper helper,String line) {
		String[] input = validate (line,line.length () == 2 || line.length () == 3,"addFurnace('<output> <input> | [float]')");
		isValid (helper,input[0],input[1]);
		if (line.split (" ").length == 2) {
			// TODO Default furnace EXP
			furnaceRecipes.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),1});
		} else {
			isValid (Types.FLOAT,helper,input[2]);
			furnaceRecipes.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),Float.parseFloat (input[2])});
		}
	}

	@ScriptFunction
	public void addBrewing (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 3,"addBrewing('<output> <input> <bottom>')");
		isValid (helper,input[0],input[1],input[2]);
		brewingRecipes.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2])});
	}

	@ScriptFunction
	public void addOreEntry (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length >= 2,"addOreEntry('<stack> <entry>')");
		isValid (helper,input[0]);
		for (int index = 1; index < input.length; index++) {
			isValid (Types.STRING,helper,input[index]);
			oreEntries.add (new Object[] {input[index],convertStack (helper,input[0])});
		}
	}

	@Override
	public void finishSupport () {
		for (Object[] dict : oreEntries)
			OreDictionary.registerOre ((String) dict[0],(ItemStack) dict[1]);
		ForgeRegistries.RECIPES.registerAll (scriptRecipes.toArray (new IRecipe[0]));
		for (Object[] smelting : furnaceRecipes)
			FurnaceRecipes.instance ().addSmeltingRecipe ((ItemStack) smelting[0],(ItemStack) smelting[1],(float) smelting[2]);
		for (Object[] brewing : brewingRecipes)
			BrewingRecipeRegistry.addRecipe (new BrewingOreRecipe ((ItemStack) brewing[1],Collections.singletonList ((ItemStack) brewing[2]),(ItemStack) brewing[0]));
		recipeLock (true);
	}

	@Override
	public void init () {
		recipeLock (false);
//		if (ConfigHandler.Script.removeAllCraftingRecipes)
			removeRecipes ();
//		if (ConfigHandler.Script.removeAllFurnaceRecipes)
			FurnaceRecipes.instance ().getSmeltingList ().clear ();
	}

	private void recipeLock (boolean lock) {
		ForgeRegistry recipes = (ForgeRegistry) ForgeRegistries.RECIPES;
		if (lock)
			recipes.freeze ();
		else
			recipes.unfreeze ();
	}

	private void removeRecipes () {
		ForgeRegistry <IRecipe> recipes = (ForgeRegistry <IRecipe>) ForgeRegistries.RECIPES;
		for (IRecipe recipe : recipes.getValues ())
			if (canRemove (Objects.requireNonNull (recipe.getRecipeOutput ().getItem ().getRegistryName ()).getResourceDomain ())) {
				recipes.remove (recipe.getRegistryName ());
				recipes.register (new InvalidRecipe (recipe));
			}
	}

	private boolean canRemove (String modid) {
		for (String mod : ConfigHandler.Script.recipeWhitelist)
			if (mod.equalsIgnoreCase (modid))
				return false;
		return true;
	}
}
