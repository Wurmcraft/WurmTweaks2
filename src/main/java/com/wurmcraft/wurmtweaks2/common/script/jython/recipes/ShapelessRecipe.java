package com.wurmcraft.wurmtweaks2.common.script.jython.recipes;

import static com.wurmcraft.wurmtweaks2.common.script.data.RecipeUtils.recipeLock;

import com.wurmcraft.wurmtweaks2.common.reference.Global;
import com.wurmcraft.wurmtweaks2.common.script.data.ShapelessRecipeWT;
import joptsimple.internal.Strings;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.python.icu.impl.InvalidFormatException;

public class ShapelessRecipe {

  public String output;
  public String inputs;
  private ShapelessRecipeWT recipe;

  public ShapelessRecipe(String output, String inputs) throws InvalidFormatException {
    this.output = output;
    this.inputs = inputs;
    recipe = new ShapelessRecipeWT(this);
    recipe.setRegistryName(Global.MODID, output + inputs);
    register(recipe);
  }

  public ShapelessRecipe(String output, String... inputs) throws InvalidFormatException {
    this.output = output;
    this.inputs = Strings.join(inputs, " ");
    recipe = new ShapelessRecipeWT(this);
    register(recipe);
  }

  public String getOutput() {
    return output;
  }

  public String getInputs() {
    return inputs;
  }

  private void register(ShapelessRecipeWT r) {
    recipeLock(false);
    ForgeRegistries.RECIPES.register(r);
    recipeLock(true);
  }
}
