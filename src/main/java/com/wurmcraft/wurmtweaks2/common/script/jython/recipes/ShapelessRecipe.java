package com.wurmcraft.wurmtweaks2.common.script.jython.recipes;

import com.wurmcraft.wurmtweaks2.common.reference.Global;
import com.wurmcraft.wurmtweaks2.common.script.data.ShapelessRecipeWT;
import com.wurmcraft.wurmtweaks2.common.script.jython.Item;
import joptsimple.internal.Strings;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.python.icu.impl.InvalidFormatException;

public class ShapelessRecipe {

  public String output;
  public String inputs;
  private ShapelessRecipeWT recipe;

  public ShapelessRecipe(String output, String inputs) throws InvalidFormatException {
    this.output = output;
    this.inputs = inputs;
    recipe = new ShapelessRecipeWT(this);
    recipe.setRegistryName(Global.MODID,output + inputs);
    register(recipe);
  }

  public ShapelessRecipe(Item output, String format, String inputs)
      throws InvalidFormatException {
    this.output = output.toString();
    this.inputs = inputs;
    recipe = new ShapelessRecipeWT(this);
    register(recipe);

  }

  public ShapelessRecipe(String output, String... inputs) throws InvalidFormatException {
    this.output = output;
    this.inputs = Strings.join(inputs, " ");
    recipe = new ShapelessRecipeWT(this);
    register(recipe);
  }

  public ShapelessRecipe(Item output, String format, String... inputs)
      throws InvalidFormatException {
    this.output = output.toString();
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

  private void recipeLock(boolean lock) {
    ForgeRegistry<IRecipe> recipes = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
    if (lock) {
      recipes.freeze();
    } else {
      recipes.unfreeze();
    }
  }

  private void register(ShapelessRecipeWT r) {
    recipeLock(false);
    ForgeRegistries.RECIPES.register(r);
    recipeLock(true);
  }
}
