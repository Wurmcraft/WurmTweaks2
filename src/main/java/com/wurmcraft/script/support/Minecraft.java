package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.WurmScript;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
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
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.util.*;

import static java.lang.Float.parseFloat;

/**
 * Adds support for Vanilla
 */
public class Minecraft extends SupportBase {
 public static class FurnaceRecipe {
  public final ItemStack input, output;
  public final float experience;

  public FurnaceRecipe(ItemStack input, ItemStack output, float experience) {
   this.input = input;
   this.output = output;
   this.experience = experience;
  }
 }

 public static class BrewingRecipe {
  public final ItemStack input, output;
  public final List<ItemStack> ingredients;

  public BrewingRecipe(ItemStack input, ItemStack output, List<ItemStack> ingredients) {
   this.input = input;
   this.output = output;
   this.ingredients = ingredients;
  }

  public BrewingRecipe(ItemStack input, ItemStack output, ItemStack... ingredients) {
   this(input, output, Arrays.asList(ingredients));
  }
 }

 public static class OreEntry {
  public final String ore;
  public final ItemStack stack;

  public OreEntry(String ore, ItemStack stack) {
   this.ore = ore;
   this.stack = stack;
  }
 }

 public static Set <OreEntry> oreEntries = new NonBlockingHashSet <> ();
 private static Set<IRecipe> scriptRecipes = new NonBlockingHashSet<>();
 private static Set<FurnaceRecipe> furnaceRecipes = new NonBlockingHashSet<>();
 private static Set<BrewingRecipe> brewingRecipes = new NonBlockingHashSet<>();

 private final FurnaceRecipes instance;

 public Minecraft() {
  super("minecraft");
  this.instance = FurnaceRecipes.instance();
 }

 @Override
 public void init() {
  // TODO temp till find out how this is called just before finishSupport() causing them to get cleared
  if (WurmScript.reloading) {
   scriptRecipes.clear ();
   furnaceRecipes.clear ();
   brewingRecipes.clear ();
   oreEntries.clear ();
   WurmScript.reloading = false;
  }
  recipeLock(false);
  if (ConfigHandler.removeAllCraftingRecipes)
  removeRecipes();
  if (ConfigHandler.removeAllFurnaceRecipes)
  FurnaceRecipes.instance().getSmeltingList().clear();
 }

 @Override
 public void finishSupport() {
  oreEntries.forEach(dict -> OreDictionary.registerOre(dict.ore, dict.stack));
  ForgeRegistries.RECIPES.registerAll(scriptRecipes.toArray(new IRecipe[0]));
  furnaceRecipes.forEach(fr -> instance.addSmeltingRecipe(fr.input, fr.output, fr.experience));
  brewingRecipes.forEach(brew ->
   BrewingRecipeRegistry.addRecipe(new BrewingOreRecipe(brew.input, brew.ingredients, brew.output))
  );
//   BrewingRecipeRegistry.addRecipe(new BrewingOreRecipe((ItemStack) brewing[1], Collections.singletonList((ItemStack) brewing[2]), (ItemStack) brewing[0]));
  recipeLock(true);
 }

 private void recipeLock(boolean lock) {
  ForgeRegistry recipes = (ForgeRegistry)ForgeRegistries.RECIPES;
  if (lock) recipes.freeze();
  else recipes.unfreeze();
 }

 private void removeRecipes() {
  ForgeRegistry<IRecipe> recipes = (ForgeRegistry<IRecipe>)ForgeRegistries.RECIPES;
  for (IRecipe recipe : recipes.getValues())
   if (canRemove(Objects.requireNonNull(recipe.getRecipeOutput().getItem().getRegistryName()).getResourceDomain())) {
    recipes.remove(recipe.getRegistryName());
    recipes.register(new InvalidRecipe(recipe));
   }
 }

 private boolean canRemove(String modid) {
  for (String mod : ConfigHandler.recipeWhitelist) {
   if (mod.equalsIgnoreCase(modid)) return false;
  }
  return true;
 }

 @ScriptFunction
 public void addShapeless(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addShapeless('<output> [input]...')");
  isValid(helper, input[0]);
  List<Ingredient> recipeInputs = RecipeUtils.getShapelessItems(helper, Arrays.copyOfRange(input, 1, input.length));
  ItemStack output = convertStack(helper, input[0]);
  DynamicShapelessOreRecipe recipe = new DynamicShapelessOreRecipe(new ResourceLocation(Global.MODID, "Recipes"), output, recipeInputs.toArray(new Ingredient[0]));
  recipe.setRegistryName(RecipeUtils.generateRecipeName(output, recipeInputs));
  scriptRecipes.add(recipe);
 }

 @ScriptFunction
 public void addShaped(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length >= 2, "addShaped('<output> (style)...')");
  isValid(helper, input[0]);
  List<Object> recipeStyle = RecipeUtils.getShapedRecipe(helper, input);
  ItemStack output = convertStack(helper, input[0]);
  DynamicShapedOreRecipe recipe = new DynamicShapedOreRecipe(new ResourceLocation(Global.MODID, "Recipes"), output, recipeStyle.toArray(new Object[0]));
  recipe.setRegistryName(RecipeUtils.generateRecipeName(output, recipe));
  scriptRecipes.add(recipe);
 }

 @ScriptFunction
 public void addFurnace(StackHelper helper, String line) {
  String[] script = validateFormat(
   line,
   (line.split( " ").length == 2 || line.split( " ").length == 3),
   "addFurnace('<output> <input> | [float]')"
  );
  isValid(helper, script[0], script[1]);
  ItemStack
   input = convertStack(helper, script[0]),
   output = convertStack(helper, script[1]);
  if (script.length == 2) {
   furnaceRecipes.add(
    new FurnaceRecipe(input, output, 1f)
   );
  } else {
   isValid(EnumInputType.FLOAT, helper, script[2]);
   furnaceRecipes.add(
    new FurnaceRecipe(input, output, parseFloat(script[2]))
   );
  }
 }

 @ScriptFunction
 public void addBrewing(StackHelper helper, String line) {
  String[] script = validateFormat(line, line.split(" ").length == 3, "addBrewing('<output> <input> <bottom>')");
  isValid(helper, script[0], script[1], script[2]);
  ItemStack
   input = convertStack(helper, script[0]),
   output = convertStack(helper, script[1]),
   ingredients = convertStack(helper, script[2]);
  brewingRecipes.add(new BrewingRecipe(input, output, ingredients));
 }

 @ScriptFunction
 public void addOreEntry(StackHelper helper, String line) {
  String[] script = validateFormat(line, line.split(" ").length >= 2, "addOreEntry('<stack> <entry>')");
  isValid(helper, script[0]);
  for (int index = 1; index < script.length; index++) {
   isValid(EnumInputType.STRING, helper, script[index]);
   oreEntries.add(new OreEntry(script[index], convertStack(helper, script[0])));
  }
 }
}
