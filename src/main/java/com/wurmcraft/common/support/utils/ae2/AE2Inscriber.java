package com.wurmcraft.common.support.utils.ae2;

import appeng.api.features.IInscriberRecipe;
import appeng.api.features.InscriberProcessType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;

public class AE2Inscriber implements IInscriberRecipe {

  @Nonnull private final List<ItemStack> inputs;
  @Nonnull private final ItemStack output;
  @Nonnull private final Optional<ItemStack> maybeTop;
  @Nonnull private final Optional<ItemStack> maybeBot;
  @Nonnull private final InscriberProcessType type;

  public AE2Inscriber(
      @Nonnull Collection<ItemStack> inputs,
      @Nonnull ItemStack output,
      @Nullable ItemStack top,
      @Nullable ItemStack bot,
      @Nonnull InscriberProcessType type) {
    this.inputs = new ArrayList(inputs.size());
    this.inputs.addAll(inputs);
    this.output = output;
    this.maybeTop = Optional.ofNullable(top);
    this.maybeBot = Optional.ofNullable(bot);
    this.type = type;
  }

  @Nonnull
  public final List<ItemStack> getInputs() {
    return this.inputs;
  }

  @Nonnull
  public final ItemStack getOutput() {
    return this.output;
  }

  @Nonnull
  public final Optional<ItemStack> getTopOptional() {
    return this.maybeTop;
  }

  @Nonnull
  public final Optional<ItemStack> getBottomOptional() {
    return this.maybeBot;
  }

  @Nonnull
  public final InscriberProcessType getProcessType() {
    return this.type;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof IInscriberRecipe)) {
      return false;
    } else {
      IInscriberRecipe that = (IInscriberRecipe) o;
      if (!this.inputs.equals(that.getInputs())) {
        return false;
      } else if (!this.output.equals(that.getOutput())) {
        return false;
      } else if (!this.maybeTop.equals(that.getTopOptional())) {
        return false;
      } else if (!this.maybeBot.equals(that.getBottomOptional())) {
        return false;
      } else {
        return this.type == that.getProcessType();
      }
    }
  }

  public int hashCode() {
    int result = this.inputs.hashCode();
    result = 31 * result + this.output.hashCode();
    result = 31 * result + this.maybeTop.hashCode();
    result = 31 * result + this.maybeBot.hashCode();
    result = 31 * result + this.type.hashCode();
    return result;
  }
}
