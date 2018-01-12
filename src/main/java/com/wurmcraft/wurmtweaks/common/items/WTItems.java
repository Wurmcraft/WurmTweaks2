package com.wurmcraft.wurmtweaks.common.items;

import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.common.Registry;
import net.minecraft.item.Item;

public class WTItems {

	public static Item itemMeta;

	public static void register () {
		register (itemMeta = new ItemMeta (ConfigHandler.metaItems.replaceAll (" ","").split (",")),"itemMeta");
	}

	private static Item register (Item item,String name) {
		Registry.registerItem (item,name);
		return item;
	}

}
