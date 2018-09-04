package com.wurmcraft.common.events;

import com.wurmcraft.common.ConfigHandler;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class DamageHealEvents {

  @SubscribeEvent
  public void onRespawn(PlayerRespawnEvent e) {
    e.player.setHealth(e.player.getMaxHealth());
  }

  @SubscribeEvent
  public void onDamage(LivingDamageEvent e) {
    e.setAmount(e.getAmount() * (float) ConfigHandler.damageMod);
  }

  @SubscribeEvent
  public void livingTick(LivingHealEvent e) {
    e.setAmount((float) (e.getAmount() * ConfigHandler.regenMod));
  }
}
