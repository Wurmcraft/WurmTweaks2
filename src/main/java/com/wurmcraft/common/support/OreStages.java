package com.wurmcraft.common.support;

import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "orestages")
public class OreStages {

  private static NonBlockingHashSet<OreStage> oreStages;
  private static NonBlockingHashSet<Unlock> unlockStages;

  public static NonBlockingHashMap<ItemStack, String> itemCache;

  @InitSupport
  public void init() {
    oreStages = new NonBlockingHashSet<>();
    unlockStages = new NonBlockingHashSet<>();
    itemCache = new NonBlockingHashMap<>();
    if (ScriptExecutor.reload) {
      for (OreStage stage : oreStages) {
        OreTiersAPI.removeReplacement(stage.block);
      }
      oreStages.clear();
      unlockStages.clear();
      itemCache.clear();
    } else {
      MinecraftForge.EVENT_BUS.register(new OreStages());
    }
  }

  @FinalizeSupport
  public void finalizeSupport() {
    for (OreStage stage : oreStages) {
      OreTiersAPI.addReplacement(stage.stage, stage.block, stage.replacement, true);
    }
    for (Unlock unlock : unlockStages) {
      itemCache.put(unlock.item, unlock.stage);
    }
  }


  @ScriptFunction(modid = "orestages", inputFormat = "String Block Block")
  public void addOreStage(Converter converter, String[] line) {
    oreStages.add(new OreStage(
        Block.getBlockFromItem(((ItemStack) converter.convert(line[1], 1)).getItem())
            .getDefaultState(),
        Block.getBlockFromItem(((ItemStack) converter.convert(line[2], 1)).getItem())
            .getDefaultState(), getStageFromString(line[0])));
  }

  @ScriptFunction(modid = "orestages", inputFormat = "ItemStack String")
  public void addUnlock(Converter converter, String[] line) {
    unlockStages.add(new Unlock(line[1], (ItemStack) converter.convert(line[0], 1)));
  }

  private String getStageFromString(String stage) {
    return stage.replaceAll("_", " ");
  }

  private String getStage(ItemStack stack) {
    for (ItemStack item : itemCache.keySet()) {
      if (item.isItemEqualIgnoreDurability(stack)) {
        return itemCache.getOrDefault(item, "");
      }
    }
    return "";
  }

  public class OreStage {

    public IBlockState block;
    public IBlockState replacement;
    public String stage;

    public OreStage(IBlockState block, IBlockState replacement, String stage) {
      this.block = block;
      this.replacement = replacement;
      this.stage = stage;
    }
  }

  public class Unlock {

    public String stage;
    public ItemStack item;

    public Unlock(String stage, ItemStack item) {
      this.stage = stage;
      this.item = item;
    }

    @SubscribeEvent
    public void onPlayerCraft(ItemCraftedEvent e) {
      String possibleStage = getStage(e.crafting);
      if (!possibleStage.isEmpty()) {
        GameStageHelper.getPlayerData(e.player).addStage(possibleStage);
        e.player.sendMessage(new TextComponentString(
            TextFormatting.AQUA + "You have just unlocked the %STAGE% stage!"
                .replaceAll("%STAGE%", possibleStage)));
        GameStageHelper.syncPlayer(e.player);
      }
    }
  }

  @SubscribeEvent
  public void onItemPickup(ItemPickupEvent e) {
    String possibleStage = getStage(e.getStack());
    if (!possibleStage.isEmpty()) {
      GameStageHelper.getPlayerData(e.player).addStage(possibleStage);
      e.player.sendMessage(new TextComponentString(
          TextFormatting.AQUA + "You have just unlocked the %STAGE% stage!"
              .replaceAll("%STAGE%", possibleStage)));
      GameStageHelper.syncPlayer(e.player);
    }
  }


}
