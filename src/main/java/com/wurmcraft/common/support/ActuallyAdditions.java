package com.wurmcraft.common.support;

import com.wurmcraft.api.script.StackSettings;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.BallOfFurReturn;
import de.ellpeck.actuallyadditions.api.recipe.CompostRecipe;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import de.ellpeck.actuallyadditions.api.recipe.TreasureChestLoot;
import de.ellpeck.actuallyadditions.api.recipe.WeightedOre;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "actuallyadditions")
public class ActuallyAdditions {

  private static NonBlockingHashSet<CrusherRecipe> scriptCrusher;
  private static NonBlockingHashSet<BallOfFurReturn> scriptBallOfFur;
  private static NonBlockingHashSet<TreasureChestLoot> scriptTreasure;
  private static NonBlockingHashSet<LensConversionRecipe> scriptLensConversion;
  private static NonBlockingHashSet<EmpowererRecipe> scriptEmpowerer;
  private static NonBlockingHashSet<CompostRecipe> scriptCompost;
  private static NonBlockingHashSet<WeightedOre> scriptStone;
  private static NonBlockingHashSet<WeightedOre> scriptNether;

  @InitSupport(modid = "actuallyadditions")
  public void init() {
    scriptCrusher = new NonBlockingHashSet<>();
    scriptBallOfFur = new NonBlockingHashSet<>();
    scriptTreasure = new NonBlockingHashSet<>();
    scriptLensConversion = new NonBlockingHashSet<>();
    scriptEmpowerer = new NonBlockingHashSet<>();
    scriptCompost = new NonBlockingHashSet<>();
    scriptStone = new NonBlockingHashSet<>();
    scriptNether = new NonBlockingHashSet<>();
    if (ScriptExecutor.reload) {
      scriptCrusher.forEach(ActuallyAdditionsAPI.CRUSHER_RECIPES::remove);
      scriptCrusher.clear();
      scriptBallOfFur.forEach(ActuallyAdditionsAPI.BALL_OF_FUR_RETURN_ITEMS::remove);
      scriptBallOfFur.clear();
      scriptTreasure.forEach(ActuallyAdditionsAPI.TREASURE_CHEST_LOOT::remove);
      scriptTreasure.clear();
      scriptLensConversion.forEach(
          ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES::remove);
      scriptLensConversion.clear();
      scriptCompost.forEach(ActuallyAdditionsAPI.COMPOST_RECIPES::remove);
      scriptStone.forEach(ActuallyAdditionsAPI.STONE_ORES::remove);
      scriptNether.forEach(ActuallyAdditionsAPI.NETHERRACK_ORES::remove);
    }
  }

  @FinalizeSupport(modid = "actuallyadditions")
  public void finalizeSupport() {
    scriptCrusher.forEach(
        crusher ->
            ActuallyAdditionsAPI.addCrusherRecipe(
                crusher.getInput(),
                crusher.getOutputOne(),
                crusher.getOutputTwo(),
                crusher.getSecondChance()));
    scriptBallOfFur.forEach(
        fur -> ActuallyAdditionsAPI.addBallOfFurReturnItem(fur.returnItem, fur.itemWeight));
    scriptTreasure.forEach(
        loot ->
            ActuallyAdditionsAPI.addTreasureChestLoot(
                loot.returnItem, loot.itemWeight, loot.minAmount, loot.maxAmount));
    scriptLensConversion.forEach(
        recipe ->
            ActuallyAdditionsAPI.addReconstructorLensConversionRecipe(
                recipe.getInput(), recipe.getOutput(), recipe.getEnergyUsed()));
    scriptEmpowerer.forEach(
        recipe ->
            ActuallyAdditionsAPI.addEmpowererRecipe(
                recipe.getInput(),
                recipe.getOutput(),
                recipe.getStandOne(),
                recipe.getStandTwo(),
                recipe.getStandThree(),
                recipe.getStandFour(),
                recipe.getEnergyPerStand(),
                recipe.getTime(),
                recipe.getParticleColors()));
    scriptCompost.forEach(
        recipe ->
            ActuallyAdditionsAPI.addCompostRecipe(
                recipe.getInput(),
                recipe.getInputDisplay(),
                recipe.getOutput(),
                recipe.getOutputDisplay()));
    scriptStone.forEach(
        stone -> ActuallyAdditionsAPI.addMiningLensStoneOre(stone.name, stone.itemWeight));
    scriptNether.forEach(
        nether -> ActuallyAdditionsAPI.addMiningLensNetherOre(nether.name, nether.itemWeight));
  }

  @ScriptFunction(
    modid = "actuallyadditions",
    inputFormat = "ItemStack ItemStack ItemStack Integer",
    typeData = "Crusher",
    type = FunctionType.Linked
  )
  public void addAACrusher(Converter converter, String[] line) {
    scriptCrusher.add(
        new CrusherRecipe(
            (ItemStack) converter.convert(line[1], 1),
            (ItemStack) converter.convert(line[0], 1),
            (ItemStack) converter.convert(line[2], 1),
            Integer.parseInt(line[3])));
  }

  @ScriptFunction(
    modid = "actuallyadditions",
    inputFormat = "ItemStack Integer",
    typeData = "Scrap",
    type = FunctionType.Linked
  )
  public void addBallOfFur(Converter converter, String[] line) {
    scriptBallOfFur.add(
        new BallOfFurReturn((ItemStack) converter.convert(line[0]), Integer.parseInt(line[1])));
  }

  @ScriptFunction(modid = "actuallyadditions", inputFormat = "ItemStack Integer Integer")
  public void addTreasureChest(Converter converter, String[] line) {
    scriptTreasure.add(
        new TreasureChestLoot(
            (ItemStack) converter.convert(line[0]),
            Integer.parseInt(line[1]),
            ((ItemStack) converter.convert(line[0])).getCount(),
            Integer.parseInt(line[2])));
  }

  @ScriptFunction(modid = "actuallyadditions", inputFormat = "ItemStack ItemStack Integer")
  public void addReconstructor(Converter converter, String[] line) {
    scriptLensConversion.add(
        new LensConversionRecipe(
            (ItemStack) converter.convert(line[1]),
            (ItemStack) converter.convert(line[0]),
            Integer.parseInt(line[2]),
            ActuallyAdditionsAPI.lensDefaultConversion));
  }

  @ScriptFunction(
    modid = "actuallyadditions",
    inputFormat = "ItemStack ItemStack ItemStack ItemStack ItemStack ItemStack Integer Integer"
  )
  public void addEmpowerer(Converter converter, String[] line) {
    scriptEmpowerer.add(
        new EmpowererRecipe(
            (ItemStack) converter.convert(line[1]),
            (ItemStack) converter.convert(line[0]),
            (ItemStack) converter.convert(line[2]),
            (ItemStack) converter.convert(line[3]),
            (ItemStack) converter.convert(line[4]),
            (ItemStack) converter.convert(line[5]),
            Integer.parseInt(line[6]),
            Integer.parseInt(line[7]),
            new float[] {0xFF2F21, 158F / 255F, 43F / 255F, 39F / 255F}));
  }

  @ScriptFunction(modid = "actuallyadditions", inputFormat = "ItemStack Block ItemStack Block")
  public void addComposter(Converter converter, String[] line) {
    scriptCompost.add(
        new CompostRecipe(
            (ItemStack) converter.convert(line[2]),
            Block.getBlockFromItem(((ItemStack) converter.convert(line[3])).getItem()),
            (ItemStack) converter.convert(line[0]),
            Block.getBlockFromItem(((ItemStack) converter.convert(line[1])).getItem())));
  }

  @ScriptFunction(
    modid = "actuallyadditions",
    inputFormat = "String OreDictionary Integer",
    typeData = "Laser",
    type = FunctionType.Linked
  )
  public void addAALaser(Converter converter, String[] line) {
    if (line[0].equalsIgnoreCase("stone")) {
      scriptStone.add(
          new WeightedOre(
              line[1]
                  .replaceAll(StackSettings.START.getData(), "")
                  .replaceAll(StackSettings.END.getData(), ""),
              Integer.parseInt(line[2])));
    } else if (line[0].equalsIgnoreCase("nether")) {
      scriptNether.add(
          new WeightedOre(
              line[1]
                  .replaceAll(StackSettings.START.getData(), "")
                  .replaceAll(StackSettings.END.getData(), ""),
              Integer.parseInt(line[2])));
    }
  }
}
