package com.wurmcraft.common.items;

import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.Registry;
import net.minecraft.item.Item;

public class WurmTweaksItems {

  public static Item itemMeta;

  public static void register() {
    register(
        itemMeta = new ItemMeta(ConfigHandler.metaItems.replaceAll(" ", "").split(",")),
        "itemMeta");
  }

  private static Item register(Item item, String name) {
    Registry.registerItem(item, name);
    return item;
  }
}
