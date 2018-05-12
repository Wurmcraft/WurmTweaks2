package com.wurmcraft.script.utils.tan;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.modifier.TemperatureModifier;

import java.util.HashMap;

public class ArmorTemp {

	public static HashMap <Item, Integer> armorStats = new HashMap <> ();

	public static void setArmorTemp (ItemStack item,int temp) {
		armorStats.putIfAbsent (item.getItem (),temp);
	}

	public class ArmorModifier extends TemperatureModifier {

		public ArmorModifier () {
			super ("armorWT");
		}

		@Override
		public Temperature applyPlayerModifiers (EntityPlayer player,Temperature initialTemperature,IModifierMonitor monitor) {
			int newTemperatureLevel = initialTemperature.getRawValue ();
			InventoryPlayer inventory = player.inventory;
			for (int index = 0; index < 4; index++)
				if (inventory.armorInventory.get (index) != ItemStack.EMPTY)
					if (armorStats.containsKey (inventory.armorInventory.get (index).getItem ()))
						newTemperatureLevel += armorStats.get (inventory.armorInventory.get (index).getItem ());
			monitor.addEntry (new IModifierMonitor.Context (this.getId (),"Armor",initialTemperature,new Temperature (newTemperatureLevel)));
			return new Temperature (newTemperatureLevel);
		}

		@Override
		public boolean isPlayerSpecific () {
			return true;
		}
	}

}
