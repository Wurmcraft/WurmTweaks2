package com.wurmcraft.wurmtweaks2.common.script.data;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.common.reference.Global;
import com.wurmcraft.wurmtweaks2.common.script.jython.recipes.ShapedRecipe;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedRecipeWT extends ShapedOreRecipe {

  public static final ResourceLocation SHAPED_RECIPE = new ResourceLocation(
      Global.MODID, "shapeless");

  public boolean enabled = true;

  public ShapedRecipeWT(ShapedRecipe recipe) {
    super(SHAPED_RECIPE, (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack")
            .getData(recipe.output),
        RecipeUtils.convertShapedRecipeInputs(recipe.format, recipe.recipeData));
  }

  @Nonnull
  @Override
  public NonNullList<Ingredient> getIngredients() {
    if (enabled) {
      return super.getIngredients();
    }
    return NonNullList.create();
  }
}
