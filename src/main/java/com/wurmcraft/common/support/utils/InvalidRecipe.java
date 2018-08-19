package com.wurmcraft.common.support.utils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class InvalidRecipe implements IRecipe {

  private final IRecipe oldRecipe;

  public InvalidRecipe (IRecipe oldRecipe) {
    this.oldRecipe = oldRecipe;
  }

  @Override
  public boolean matches (InventoryCrafting inv, World worldIn) {
    return false;
  }

  @Override
  public ItemStack getCraftingResult (InventoryCrafting inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit (int width,int height) {
    return false;
  }

  @Override
  public ItemStack getRecipeOutput () {
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList <ItemStack> getRemainingItems (InventoryCrafting inv) {
    return NonNullList.create ();
  }

  @Override
  public IRecipe setRegistryName (ResourceLocation name) {
    return oldRecipe.setRegistryName (name);
  }

  @Override
  public ResourceLocation getRegistryName () {
    return oldRecipe.getRegistryName ();
  }

  @Override
  public Class <IRecipe> getRegistryType () {
    return oldRecipe.getRegistryType ();
  }

  @Override
  public boolean isDynamic () {
    return true;
  }
}
