package com.wurmcraft.common.support.utils.ae2;

import appeng.api.features.IGrinderRecipe;
import java.util.Optional;
import net.minecraft.item.ItemStack;

public class AE2Grinder implements IGrinderRecipe {
  private final ItemStack in;
  private final ItemStack out;
  private final float optionalChance;
  private final Optional<ItemStack> optionalOutput;
  private final float optionalChance2;
  private final Optional<ItemStack> optionalOutput2;
  private final int turns;

  public AE2Grinder(ItemStack input, ItemStack output, int cost) {
    this(input, output, (ItemStack)null, (ItemStack)null, 0.0F, 0.0F, cost);
  }

  public AE2Grinder(ItemStack input, ItemStack output, ItemStack optional, float chance, int cost) {
    this(input, output, optional, (ItemStack)null, chance, 0.0F, cost);
  }

  public AE2Grinder(ItemStack input, ItemStack output, ItemStack optional1, ItemStack optional2, float chance1, float chance2, int cost) {
    this.in = input;
    this.out = output;
    this.optionalOutput = Optional.ofNullable(optional1);
    this.optionalChance = chance1;
    this.optionalOutput2 = Optional.ofNullable(optional2);
    this.optionalChance2 = chance2;
    this.turns = cost;
  }

  public ItemStack getInput() {
    return this.in;
  }

  public ItemStack getOutput() {
    return this.out;
  }

  public Optional<ItemStack> getOptionalOutput() {
    return this.optionalOutput;
  }

  public Optional<ItemStack> getSecondOptionalOutput() {
    return this.optionalOutput2;
  }

  public float getOptionalChance() {
    return this.optionalChance;
  }

  public float getSecondOptionalChance() {
    return this.optionalChance2;
  }

  public int getRequiredTurns() {
    return this.turns;
  }
}
