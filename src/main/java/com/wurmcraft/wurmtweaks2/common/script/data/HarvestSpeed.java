package com.wurmcraft.wurmtweaks2.common.script.data;


import net.minecraft.item.Item;

public class HarvestSpeed {

  public String itemStr;
  public Item item;
  public String block;
  public double speed;

  public HarvestSpeed(String itemStr, Item item, String block, double speed) {
    this.itemStr = itemStr;
    this.item = item;
    this.block = block;
    this.speed = speed;
  }
}
