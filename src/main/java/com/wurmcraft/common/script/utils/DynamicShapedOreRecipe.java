package com.wurmcraft.common.script.utils;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;

// TODO NBT Support
public class DynamicShapedOreRecipe extends ShapedOreRecipe {


  public DynamicShapedOreRecipe(ResourceLocation group,
      Block result, Object... recipe) {
    super(group, result, recipe);
  }

  public DynamicShapedOreRecipe(ResourceLocation group,
      Item result, Object... recipe) {
    super(group, result, recipe);
  }

  public DynamicShapedOreRecipe(ResourceLocation group,
      @Nonnull ItemStack result, Object... recipe) {
    super(group, result, recipe);
  }

  public DynamicShapedOreRecipe(ResourceLocation group,
      @Nonnull ItemStack result,
      ShapedPrimer primer) {
    super(group, result, primer);
  }

  @Override
  public boolean isDynamic() {
    return true;
  }
}
