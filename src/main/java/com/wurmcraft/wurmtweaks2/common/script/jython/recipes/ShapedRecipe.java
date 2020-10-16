package com.wurmcraft.wurmtweaks2.common.script.jython.recipes;

import static com.wurmcraft.wurmtweaks2.common.script.data.RecipeUtils.recipeLock;

import com.wurmcraft.wurmtweaks2.common.reference.Global;
import com.wurmcraft.wurmtweaks2.common.script.data.ShapedRecipeWT;
import joptsimple.internal.Strings;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ShapedRecipe {

  public String output;
  public String format;
  public String recipeData;

  public ShapedRecipe(String output, String format, String recipeData) {
    this.output = output;
    this.format = format;
    this.recipeData = recipeData;
    try {
      ShapedRecipeWT recipe = new ShapedRecipeWT(this);
      recipe.setRegistryName(Global.MODID, output + "_" + recipeData);
      register(recipe);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ShapedRecipe(String output, String[] format, String recipeData) {
    this.output = output;
    this.format = Strings.join(format, ";");
    this.recipeData = recipeData;
    try {
      ShapedRecipeWT recipe = new ShapedRecipeWT(this);
      recipe.setRegistryName(Global.MODID, output + "_" + recipeData);
      register(recipe);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ShapedRecipe(String output, String recipeData) {
    this.output = output;
    this.format = recipeData.substring(0, recipeData.indexOf(">"));
    this.recipeData = recipeData.substring(recipeData.indexOf(">") + 1);
    ShapedRecipeWT recipe = new ShapedRecipeWT(this);
    recipe.setRegistryName(Global.MODID, output + "_" + recipeData);
    register(recipe);
  }

  public ShapedRecipe(String output, String[] recipeData) {
    this.output = output;
    this.recipeData = Strings.join(recipeData, "*");
    ShapedRecipeWT recipe = new ShapedRecipeWT(this);
    recipe.setRegistryName(Global.MODID, output + "_" + recipeData);
    register(recipe);
  }

  private void register(ShapedRecipeWT r) {
    recipeLock(false);
    ForgeRegistries.RECIPES.register(r);
    recipeLock(true);
  }
}
