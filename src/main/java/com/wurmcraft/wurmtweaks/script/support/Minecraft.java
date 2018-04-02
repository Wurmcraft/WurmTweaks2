package com.wurmcraft.wurmtweaks.script.support;


import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Random;

public class Minecraft extends ModSupport {

	private static final Random RAND = new Random (System.nanoTime ());

	@Override
	public String getModID () {
		return "minecraft";
	}

	@Override
	public void init () {
	}

	@ScriptFunction
	public void addShapeless (String line) {
		String[] input = verify (line,(line.split (" ").length >= 2),"addShapeless('<output> <input>...')");
		isValid (input[0]);
		List <Ingredient> inputs = RecipeUtils.getShapelessRecipeItems (input,null,1);
		Preconditions.checkArgument (!inputs.isEmpty (),"Invalid Inputs!");
		RecipeUtils.addShapeless (convertS (input[0]),inputs.toArray (new Ingredient[0]));
	}

	@ScriptFunction
	public void addShaped (String line) {
		String[] input = verify (line,line.split (" ").length > 4,"addShaped(<output> <style> <format>')");
		isValid (input[0]);
		List <Object> recipe = RecipeUtils.getShapedRecipe (input);
		Preconditions.checkNotNull (recipe);
		RecipeUtils.addShaped (convertS (input[0]),recipe.toArray (new Object[0]));
	}

	@ScriptFunction (linkSize = {2,3}, link = "smelting")
	public void addFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 2 || line.split (" ").length == 3,"addFurnace('<output> <input> <exp>')");
		isValid (input[0],input[1]);
		if (line.length () == 3) {
			isValid (EnumInputType.FLOATNG,input[2]);
			RecipeUtils.addFurnace (convertS (input[0]),convertS (input[1]),convertNF (input[2]));
		} else
			RecipeUtils.addFurnace (convertS (input[0]),convertS (input[1]),1);
	}

	@ScriptFunction
	public void addOreEntry (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addOreEntry('<stack> entry')");
		isValid (input[0]);
		for (int index = 1; index < input.length; index++) {
			isValid (EnumInputType.STRING,input[index]);
			OreDictionary.registerOre (input[index].replaceAll ("[<>]",""),convertS (input[0]));
		}
	}

	@ScriptFunction
	public void addBrewing (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addBrewing('<output> <input> <bottom>')");
		for (int index = 0; index < 2; index++)
			isValid (input[index]);
		RecipeUtils.addBrewing (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}

	@ScriptFunction
	public void addTrade (String line) {
		String[] input = verify (line,line.split ("").length == 3,"addTrade('<villager> <buy> <sell>')");
		ResourceLocation loc = input[0].contains (":") ? new ResourceLocation (input[0].substring (0,input[0].indexOf (":")),input[0].substring (input[0].indexOf (":"),input[0].length ())) : new ResourceLocation ("minecraft",input[0]);
		VillagerRegistry.VillagerProfession villager = ForgeRegistries.VILLAGER_PROFESSIONS.getValue (loc);
		villager.getCareer (100 + RAND.nextInt (100)).addTrade (1,new VillagerTrade (convertS (input[1]),convertS (input[2])));
	}

	public static String getFluids () {
		StringBuilder builder = new StringBuilder ();
		for (Fluid stack : FluidRegistry.getRegisteredFluids ().values ())
			builder.append (stack.getUnlocalizedName () + "\n");
		return builder.toString ();
	}

	public class VillagerTrade implements EntityVillager.ITradeList {

		private ItemStack buy;
		private ItemStack sell;

		public VillagerTrade (ItemStack buy,ItemStack sell) {
			this.buy = buy;
			this.sell = sell;
		}

		@Override
		public void addMerchantRecipe (IMerchant merchant,MerchantRecipeList recipeList,Random random) {
			recipeList.add (new MerchantRecipe (buy,sell));
		}
	}
}
