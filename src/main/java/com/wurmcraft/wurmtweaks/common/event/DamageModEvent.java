package com.wurmcraft.wurmtweaks.common.event;

import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageModEvent {

	@SubscribeEvent
	public void onDamge (LivingDamageEvent e) {
		e.setAmount ((float) (e.getAmount () * ConfigHandler.damageMod));
	}
}
