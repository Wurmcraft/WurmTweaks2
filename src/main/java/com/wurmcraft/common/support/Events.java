package com.wurmcraft.common.support;

import com.wurmcraft.api.script.StackSettings;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Support(modid = "events")
public class Events {

  // Tooltip
  public static NonBlockingHashSet<Item> tooltipQuick = new NonBlockingHashSet<>();
  public static NonBlockingHashSet<Object[]> tooltips = new NonBlockingHashSet<>();
  // Drop
  public static NonBlockingHashSet<ItemStack> drops = new NonBlockingHashSet<>();
  // Pickup Conversion
  private static NonBlockingHashMap<ItemStack, ItemStack> dropPickupEvent = new NonBlockingHashMap<>();
  private static NonBlockingHashSet<Item> pickupQuick = new NonBlockingHashSet<>();


  private static boolean isSameIgnoreSize(ItemStack a, ItemStack b) {
    return a.getItem().equals(b.getItem()) && a.getTagCompound() == b.getTagCompound() && (
        (a.getItemDamage() == b.getItemDamage()) || a.getItemDamage() == Short.MAX_VALUE);
  }

  @FinalizeSupport
  public void finishSupport() {

  }

  @InitSupport
  public void init() {
    MinecraftForge.EVENT_BUS.register(this);
    if (ScriptExecutor.reload) {
      tooltipQuick.clear();
      tooltips.clear();
    }
  }

  @ScriptFunction(modid = "events", inputFormat = "ItemStack String ...")
  public void addTooltip(Converter converter, String[] line) {
    List<String> tips = new ArrayList<>();
    for (int index = 1; index < line.length; index++) {
      tips.add(
          line[index].replaceAll("&", "\u00A7").replaceAll(StackSettings.SPACE.getData(), " "));
    }
    tooltipQuick.add(((ItemStack) converter.convert(line[0])).getItem());
    tooltips.add(new Object[]{converter.convert(line[0]), tips.toArray(new String[0])});
  }

  @SubscribeEvent
  @SideOnly(value = Side.CLIENT)
  public void onRenderTooltip(ItemTooltipEvent e) {
    if (tooltipQuick.size() > 0 && tooltips.size() > 0 && tooltipQuick
        .contains(e.getItemStack().getItem())) {
      for (Object[] data : tooltips) {
        if (isSameIgnoreSize((ItemStack) data[0], e.getItemStack())) {
          e.getToolTip().addAll(Arrays.asList(((String[]) data[1])));
        }
      }
    }
  }

  @ScriptFunction(modid = "events", inputFormat = "ItemStack")
  public void disablePickup(Converter converter, String[] line) {
    drops.add((ItemStack) converter.convert(line[0]));
  }

  @SubscribeEvent
  public void disableDrop(ItemTossEvent e) {
    if (e.getEntityItem().getItem() != ItemStack.EMPTY) {
      for (ItemStack items : drops) {
        if (isSameIgnoreSize(items, e.getEntityItem().getItem())) {
          e.getPlayer().sendMessage(new TextComponentTranslation("chat.denyToss.name"));
          e.setCanceled(true);
        }
      }
    }
  }

  @ScriptFunction(modid = "events", inputFormat = "ItemStack ItemStack")
  public void convertPickup(Converter converter, String[] line) {
    ItemStack theStack = (ItemStack) converter.convert(line[0]);
    pickupQuick.add(theStack.getItem());
    dropPickupEvent.put(theStack, (ItemStack) converter.convert(line[1]));
  }


  @SubscribeEvent
  public void onPickupEvent(EntityItemPickupEvent e) {
    if (pickupQuick.size() > 0 && dropPickupEvent.size() > 0 && pickupQuick
        .contains(e.getItem().getItem().getItem())) {
      for (ItemStack item : dropPickupEvent.keySet()) {
        if (isSameIgnoreSize(item, e.getItem().getItem())) {
          e.getItem().setPickupDelay(1);
          e.getItem().setDead();
          e.getEntityPlayer().inventory.addItemStackToInventory(
              convertToCorrect(e.getItem().getItem(), dropPickupEvent.get(item)));
        }
      }
    }
  }

  private ItemStack convertToCorrect(ItemStack pickupItem, ItemStack conversion) {
    if (conversion == ItemStack.EMPTY) {
      return ItemStack.EMPTY;
    } else if (pickupItem.getCount() == conversion.getCount()) {
      return conversion;
    } else if (pickupItem.getCount() > conversion.getCount() && conversion.getCount() > 0) {
      ItemStack temp = conversion.copy();
      temp.setCount(pickupItem.getCount() / conversion.getCount());
      return temp;
    } else {
      ItemStack temp = conversion.copy();
      temp.setCount(pickupItem.getCount() * conversion.getCount());
      return temp;
    }
  }
}
