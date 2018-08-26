package com.wurmcraft.common.script.converters;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.DataWrapper;
import com.wurmcraft.api.script.anotations.DataConverter;
import com.wurmcraft.api.script.converter.IDataConverter;
import com.wurmcraft.api.script.StackSettings;
import com.wurmcraft.api.script.exceptions.InvalidItemStack;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@DataConverter
public class StackConverter implements IDataConverter<ItemStack> {

  private static final Thread mainThread = Thread.currentThread();
  private NonBlockingHashMap<String, ItemStack> cachedData;
  private Logger log;


  @Override
  public String getName() {
    return "ItemStack";
  }

  public StackConverter() {
    cachedData = new NonBlockingHashMap<>();
    this.log = WurmTweaks.logger;
  }

  public StackConverter(boolean cacheData) {
    if (cacheData) {
      cachedData = new NonBlockingHashMap<>();
    }
    this.log = WurmTweaks.logger;
  }

  @Override
  public int getMeta(String data) {
    if (data.contains(StackSettings.META.getData())) {
      boolean hasNBT = data.contains(StackSettings.EXTRA_DATA.getData());
      String meta =
          data.substring(
              data.indexOf(StackSettings.META.getData()) + 1,
              hasNBT
                  ? data.indexOf(StackSettings.EXTRA_DATA.getData())
                  : data.indexOf(StackSettings.END.getData()));
      try {
        int d = Integer.parseInt(meta);
        return d >= OreDictionary.WILDCARD_VALUE ? OreDictionary.WILDCARD_VALUE : d;
      } catch (NumberFormatException e) {
        print("Invalid Number (Meta) '" + meta + "'");
      }
    }
    return 0;
  }

  @Override
  public int getDataSize(String data) {
    if (data.contains(StackSettings.STACKSIZE.getData())) {
      String stackSize =
          data.substring(
              data.indexOf(StackSettings.START.getData()) + 1,
              data.indexOf(StackSettings.STACKSIZE.getData()));
      try {
        return Integer.parseInt(stackSize);
      } catch (NumberFormatException e) {
        print("Invalid Number (StackSize) '" + stackSize + "'");
      }
    }
    return 1;
  }

  @Override
  public DataWrapper getName(String data) {
    if (data.contains(StackSettings.START.getData())
        && data.contains(StackSettings.END.getData())
        && data.contains(StackSettings.NAME.getData())) {
      boolean hasDedicatedStackSize = data.contains(StackSettings.STACKSIZE.getData());
      String modid =
          data.substring(
              hasDedicatedStackSize
                  ? data.indexOf(StackSettings.STACKSIZE.getData()) + 1
                  : data.indexOf(StackSettings.START.getData()) + 1,
              data.indexOf(StackSettings.NAME.getData()));
      boolean hasMETA = data.contains(StackSettings.META.getData());
      boolean hasNBT = data.contains(StackSettings.EXTRA_DATA.getData());
      String name;
      if (hasMETA) {
        name =
            data.substring(
                data.indexOf(StackSettings.NAME.getData()) + 1,
                data.indexOf(StackSettings.META.getData()));
      } else if (hasNBT) {
        name =
            data.substring(
                data.indexOf(StackSettings.NAME.getData()) + 1,
                data.indexOf(StackSettings.EXTRA_DATA.getData()));
      } else {
        name =
            data.substring(
                data.indexOf(StackSettings.NAME.getData()) + 1,
                data.indexOf(StackSettings.END.getData()));
      }
      return new DataWrapper(modid, name);
    } else if (data.contains(StackSettings.START.getData())
        && data.contains(StackSettings.END.getData())
        && data.substring(
        data.indexOf(StackSettings.START.getData()) + 1,
        data.indexOf(StackSettings.END.getData()))
        .matches(StackSettings.EMPTY_STACK.getData())) {
      return new DataWrapper("empty", "empty");
    }
    return new DataWrapper("null", "null");
  }

  @Override
  public String getExtraData(String data) {
    if (data.contains(StackSettings.EXTRA_DATA.getData())) {
      return data.substring(
          data.indexOf(StackSettings.EXTRA_DATA.getData()) + 1,
          data.indexOf(StackSettings.END.getData()));
    }
    return "";
  }

  @Override
  public Item getBasicData(String data) {
    DataWrapper location = getName(data);
    if (location.getModid().equals("empty") && location.getName().equals("empty")) {
      return Items.AIR;
    }
    synchronized (mainThread) {
      return ForgeRegistries.ITEMS.getValue(location.toResourceLocation());
    }
  }

  @Override
  public ItemStack getData(String data) {
    if (data.substring(
        data.indexOf(StackSettings.START.getData()) + 1,
        data.indexOf(StackSettings.END.getData()))
        .matches(StackSettings.EMPTY_STACK.getData())) {
      return ItemStack.EMPTY;
    }
    if (cachedData != null && cachedData.containsKey(data)) {
      return cachedData.get(data);
    }
    return getItemStack(data);
  }

  private ItemStack getItemStack(String data) {
    if (data.startsWith(StackSettings.START.getData())
        && data.endsWith(StackSettings.END.getData())
        && data.contains(StackSettings.NAME.getData())) {
      if (data.substring(
          data.indexOf(StackSettings.START.getData()) + 1,
          data.indexOf(StackSettings.END.getData()))
          .matches(StackSettings.EMPTY_STACK.getData())) {
        return ItemStack.EMPTY;
      }
      Item item = getBasicData(data);
      int stackSize = getDataSize(data);
      int meta = getMeta(data);
      String nbt = getExtraData(data);
      ItemStack stack = new ItemStack(item, stackSize, meta);
      if (nbt.length() > 0) {
        try {
          NBTTagCompound stackNBT = JsonToNBT.getTagFromJson(nbt);
          stack.setTagCompound(stackNBT);
        } catch (NBTException e) {
          print("Invalid NBT '" + nbt + "'");
        }
      }
      return stack;
    }
    throw new InvalidItemStack("Invalid ItemStack '" + data + "'");
  }

  @Override
  public String toString(ItemStack data) {
    StringBuilder builder = new StringBuilder();
    builder.append(StackSettings.START.getData());
    if (data.getCount() > 1) {
      builder.append(data.getCount());
      builder.append(StackSettings.STACKSIZE.getData());
    }
    ResourceLocation location = data.getItem().getRegistryName();
    if (location != null) {
      builder.append(location.getResourceDomain());
      builder.append(StackSettings.NAME.getData());
      builder.append(location.getResourcePath());
      if (data.getMetadata() > 0) {
        builder.append(StackSettings.META.getData());
        builder.append(data.getMetadata());
      }
      if (data.hasTagCompound()) {
        builder.append(StackSettings.EXTRA_DATA.getData());
        builder.append(data.getTagCompound() != null ? data.getTagCompound().toString() : "{}");
      }
      builder.append(StackSettings.END.getData());
      return builder.toString();
    }
    return "";
  }

  @Override
  public boolean isValid(String data) {
    try {
      ItemStack stack = getData(data);
      if (stack != null) {
        cachedData.put(data, stack);
        return true;
      }

    } catch (Exception e) {
    }
    return false;
  }

  @Override
  public void print(String message) {
    if (message.startsWith("[Debug]") && message.length() > 8) {
      log.debug(message.substring(8));
    } else {
      log.info(message);
    }
  }
}
