package com.wurmcraft.wurmtweaks2.common.script.event;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class HarvestSpeed {

  public static NonBlockingHashMap<Item, com.wurmcraft.wurmtweaks2.common.script.data.HarvestSpeed> harvestSpeedCache = new NonBlockingHashMap<>();

  @SubscribeEvent
  public void onHarvest(PlayerEvent.BreakSpeed e) {
    ItemStack heldItem = e.getEntityPlayer()
        .getHeldItem(e.getEntityPlayer().swingingHand);
    if (harvestSpeedCache.contains(heldItem.getItem())) {
      com.wurmcraft.wurmtweaks2.common.script.data.HarvestSpeed harvestSpeed = harvestSpeedCache.get(
          heldItem.getItem());
      if (harvestSpeed.itemStr.equalsIgnoreCase("*")) {
        e.setNewSpeed((float) harvestSpeed.speed);
      } else {
        String block = WurmTweaks2API.dataConverters.get("ItemStack")
            .toString(new ItemStack(e.getState().getBlock())); // TODO block meta?
        if (harvestSpeed.block.equalsIgnoreCase(block)) {
          e.setNewSpeed((float) harvestSpeed.speed);
        }
      }
    }
  }
}
