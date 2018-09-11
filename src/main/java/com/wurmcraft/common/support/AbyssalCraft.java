package com.wurmcraft.common.support;

import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "abyssalcraft")
public class AbyssalCraft {

  private static NonBlockingHashSet<Crystallizer> crystallizerRecipes;
  private static NonBlockingHashSet<Transmutator> transmutatorRecipes;

  @InitSupport
  public void init() {
    crystallizerRecipes = new NonBlockingHashSet<>();
    transmutatorRecipes = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      CrystallizerRecipes.instance().getCrystallizationList().clear();
      TransmutatorRecipes.instance().getTransmutationList().clear();
      MaterializerRecipes.instance().getMaterializationList().clear();
    } else if (ScriptExecutor.reload) {
      crystallizerRecipes.forEach(
          recipe -> CrystallizerRecipes.instance().getCrystallizationList().remove(recipe.input));
      crystallizerRecipes.clear();
      transmutatorRecipes.forEach(
          recipe -> TransmutatorRecipes.instance().getTransmutationList().remove(recipe.input));
      transmutatorRecipes.clear();
    }
  }

  @ScriptFunction(modid = "abyssalcraft", inputFormat = "ItemStack ItemStack ItemStack Float")
  public void addCrystallizer(Converter converter, String[] line) {
    crystallizerRecipes.add(
        new Crystallizer(
            (ItemStack) converter.convert(line[0], 1),
            (ItemStack) converter.convert(line[1], 1),
            (ItemStack) converter.convert(line[2], 1),
            Float.parseFloat(line[3])));
  }

  @ScriptFunction(modid = "abyssalcraft", inputFormat = "ItemStack ItemStack Float")
  public void addTransmutator(Converter converter, String[] line) {
    transmutatorRecipes.add(
        new Transmutator(
            (ItemStack) converter.convert(line[0]),
            (ItemStack) converter.convert(line[1]),
            Float.parseFloat(line[2])));
  }

  @FinalizeSupport
  public void finishSupport() {
    crystallizerRecipes.forEach(
        crystallizerRecipe ->
            CrystallizerRecipes.instance()
                .crystallize(
                    crystallizerRecipe.input,
                    crystallizerRecipe.output,
                    crystallizerRecipe.output2,
                    crystallizerRecipe.exp));
    transmutatorRecipes.forEach(
        recipe ->
            TransmutatorRecipes.instance().transmute(recipe.input, recipe.output, recipe.exp));
  }

  public class Crystallizer {

    public ItemStack output;
    public ItemStack output2;
    public ItemStack input;
    public float exp;

    public Crystallizer(ItemStack output, ItemStack output2, ItemStack input, float exp) {
      this.output = output;
      this.output2 = output2;
      this.input = input;
      this.exp = exp;
    }
  }

  public class Transmutator {

    public ItemStack output;
    public ItemStack input;
    public float exp;

    public Transmutator(ItemStack output, ItemStack input, float exp) {
      this.output = output;
      this.input = input;
      this.exp = exp;
    }
  }
}
