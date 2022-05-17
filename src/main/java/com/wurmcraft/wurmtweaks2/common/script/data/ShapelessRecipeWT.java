package com.wurmcraft.wurmtweaks2.common.script.data;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.common.reference.Global;
import com.wurmcraft.wurmtweaks2.common.script.jython.recipes.ShapelessRecipe;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.python.icu.impl.InvalidFormatException;

public class ShapelessRecipeWT extends ShapelessOreRecipe {

  public static final ResourceLocation SHAPELESS_RECIPE = new ResourceLocation(
      Global.MODID, "shapeless");

  public boolean enabled = true;


  public ShapelessRecipeWT(ShapelessRecipe recipe) throws InvalidFormatException {
    super(SHAPELESS_RECIPE, RecipeUtils.createIngredientList(recipe.inputs),
        (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack")
            .getData(recipe.output));
  }

  @Override
  public boolean isDynamic() {
    return true;
  }

  @Nonnull
  @Override
  public NonNullList<Ingredient> getIngredients() {
    if (enabled) {
      return super.getIngredients();
    } else {
      return NonNullList.create();
    }
  }
}
