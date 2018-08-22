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
import java.util.Arrays;
import java.util.Objects;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "minecraft", threaded = true)
public class Minecraft {

  public static NonBlockingHashSet<IRecipe> scriptRecipes = new NonBlockingHashSet<>();
  public static NonBlockingHashSet<FurnaceRecipe> scriptFurnace = new NonBlockingHashSet<>();
  public static NonBlockingHashSet<OreEntry> scriptOreEntry = new NonBlockingHashSet<>();

  @InitSupport
  public void init() {
    if (ScriptExecutor.reload) {
      removeRecipes();
      scriptRecipes.clear();
    }
  }

  private void removeRecipes() {
    ForgeRegistry<IRecipe> recipes = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
    for (IRecipe recipe : recipes.getValues()) {
      if (canRemove(Objects.requireNonNull(recipe.getRecipeOutput().getItem().getRegistryName())
          .getResourceDomain()) && scriptRecipes.contains(recipe)) {
        recipes.remove(recipe.getRegistryName());
        recipes.register(new InvalidRecipe(recipe));
      }
    }
  }

  private boolean canRemove(String modid) {
    for (String mod : ConfigHandler.recipeWhitelist) {
      if (mod.equalsIgnoreCase(modid)) {
        return false;
      }
    }
    return true;
  }

  @FinalizeSupport()
  public void finalizeSupport() {
    recipeLock(false);
    if (ConfigHandler.removeAllRecipes) {
      removeRecipes();
    }
    ForgeRegistries.RECIPES.registerAll(scriptRecipes.toArray(new IRecipe[0]));
    recipeLock(true);
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

  @ScriptFunction(modid = "minecraft")
  public void addShaped(Converter converter, String[] line) {
    if (line.length >= 2) {
      scriptRecipes.add(RecipeUtils.createShapedRecipe((ItemStack) converter.convert(line[0], 1),RecipeUtils.getShapedRecipe(Arrays.copyOfRange(line,1,line.length)).toArray()));
    } else {
      WurmTweaks.logger.error("Invalid Shaped Format '" + Strings.join(line, " ") + "'");
    }
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
