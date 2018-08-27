package com.wurmcraft.common.support;


import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import com.wurmcraft.common.support.utils.InvalidRecipe;
import com.wurmcraft.common.support.utils.RecipeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "minecraft", threaded = true)
public class Minecraft {

  public static NonBlockingHashSet<IRecipe> scriptRecipes;
  public static NonBlockingHashSet<FurnaceRecipe> scriptFurnace;
  public static NonBlockingHashSet<OreEntry> scriptOreEntry;
  public static NonBlockingHashSet<BrewingOreRecipe> scriptBrewing;

  @InitSupport
  public void init() {
    scriptRecipes = new NonBlockingHashSet<>();
    scriptFurnace = new NonBlockingHashSet<>();
    scriptOreEntry = new NonBlockingHashSet<>();
    scriptBrewing = new NonBlockingHashSet<>();
    if (ScriptExecutor.reload) {
      removeRecipes();
      scriptRecipes.clear();
      scriptOreEntry.clear();
      WurmTweaks.logger
          .warn("OreDictionary entries have not been reloaded. (Old ones will still exist)");
      // TODO Remove Furnace, OreDict, Brewing Recipes on Reload
      scriptFurnace.clear();
      scriptBrewing.clear();
    }
  }

  @FinalizeSupport()
  public void finalizeSupport() {
    if (ConfigHandler.removeAllRecipes || ScriptExecutor.reload) {
      removeRecipes();
    }
    recipeLock(false);
    ForgeRegistries.RECIPES.registerAll(scriptRecipes.toArray(new IRecipe[0]));
    scriptOreEntry.forEach(entry -> OreDictionary.registerOre(entry.values, entry.entry));
    for (FurnaceRecipe recipe : scriptFurnace) {
      for (ItemStack input : recipe.input.getMatchingStacks()) {
        FurnaceRecipes.instance().addSmeltingRecipe(input, recipe.output, recipe.exp);
      }
    }
    for (BrewingOreRecipe brewingOreRecipe : scriptBrewing) {
      BrewingRecipeRegistry.addRecipe(brewingOreRecipe);
    }
  }

  private void recipeLock(boolean lock) {
    ForgeRegistry recipes = (ForgeRegistry) ForgeRegistries.RECIPES;
    if (lock) {
      recipes.freeze();
    } else {
      recipes.unfreeze();
    }
  }

  @ScriptFunction(modid = "minecraft", inputFormat = "ItemStack ItemStack/OreDictionary ...")
  public void addShapeless(Converter converter, String[] line) {
    Object[] shapelessInputs = RecipeUtils
        .getShapelessItems(Arrays.copyOfRange(line, 1, line.length), converter);
    scriptRecipes.add(RecipeUtils
        .createShapelessRecipe((ItemStack) converter.convert(line[0], 1), shapelessInputs));
  }

  @ScriptFunction(modid = "minecraft", inputFormat = "ItemStack ***")
  public void addShaped(Converter converter, String[] line) {
    if (line.length >= 2) {
      scriptRecipes.add(RecipeUtils.createShapedRecipe((ItemStack) converter.convert(line[0], 1),
          RecipeUtils.getShapedRecipe(line).toArray()));
    } else {
      WurmTweaks.logger.error("Invalid Shaped Format '" + Strings.join(line, " ") + "'");
    }
  }

  @ScriptFunction(modid = "minecraft", precedence = true, inputFormat = "ItemStack String ...")
  public void addOreEntry(Converter converter, String[] input) {
    for (int index = 1; index < input.length; index++) {
      scriptOreEntry.add(new OreEntry((ItemStack) converter.convert(input[0], 0), input[index]));
    }
  }

  @ScriptFunction(modid = "minecraft", inputFormat = "ItemStack ItemStack/OreDictionary")
  public void addFurnace(Converter converter, String[] input) {
    scriptFurnace.add(new FurnaceRecipe((ItemStack) converter.convert(input[0], 0),
        (Ingredient) converter.convert(input[1], 1)));
  }

  @ScriptFunction(modid = "minecraft", inputFormat = "ItemStack ItemStack ItemStack")
  public void addBrewing(Converter converter, String[] input) {
    scriptBrewing.add(new BrewingOreRecipe((ItemStack) converter.convert(input[0], 1),
        createList((ItemStack) converter.convert(input[2])),
        (ItemStack) converter.convert(input[1])));
  }

  private static List<ItemStack> createList(ItemStack... stack) {
    List<ItemStack> list = new ArrayList<>();
    Collections.addAll(list, stack);
    return list;
  }

  private void removeRecipes() {
    recipeLock(false);
    ForgeRegistry<IRecipe> recipes = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
    if (ConfigHandler.removeAllRecipes) {
      for (IRecipe recipe : recipes.getValues()) {
        if (canRemove(Objects.requireNonNull(recipe.getRecipeOutput().getItem().getRegistryName())
            .getResourceDomain())) {
          recipes.remove(recipe.getRegistryName());
          recipes.register(new InvalidRecipe(recipe));
        }
      }
    } else {
      for (IRecipe recipe : recipes.getValues()) {
        if (canRemove(Objects.requireNonNull(recipe.getRecipeOutput().getItem().getRegistryName())
            .getResourceDomain()) && scriptRecipes.contains(recipe)) {
          recipes.remove(recipe.getRegistryName());
          recipes.register(new InvalidRecipe(recipe));
        }
      }
    }
    recipeLock(true);
  }

  private boolean canRemove(String modid) {
    for (String mod : ConfigHandler.recipeWhitelist) {
      if (mod.equalsIgnoreCase(modid)) {
        return false;
      }
    }
    return true;
  }

  public class FurnaceRecipe {

    ItemStack output;
    Ingredient input;
    float exp;

    FurnaceRecipe(ItemStack output, Ingredient input, float exp) {
      this.output = output;
      this.input = input;
      this.exp = exp;
    }

    FurnaceRecipe(ItemStack output, Ingredient input) {
      this.output = output;
      this.input = input;
      this.exp = 1;
    }
  }

  public class OreEntry {

    public ItemStack entry;
    public String values;

    public OreEntry(ItemStack entry, String values) {
      this.entry = entry;
      this.values = values;
    }
  }

}
