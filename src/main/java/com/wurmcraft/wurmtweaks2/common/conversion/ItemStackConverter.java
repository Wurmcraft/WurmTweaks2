package com.wurmcraft.wurmtweaks2.common.conversion;

import com.wurmcraft.wurmtweaks2.WurmTweaks2;
import com.wurmcraft.wurmtweaks2.api.conversion.DataWrapper;
import com.wurmcraft.wurmtweaks2.api.conversion.IDataConverter;
import com.wurmcraft.wurmtweaks2.common.config.ConfigHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class ItemStackConverter implements IDataConverter<ItemStack> {

  // Formatting Characters
  public static final String COUNT = "x";
  public static final String SEPARATOR = ":";
  public static final String META = "@";
  public static final String NBT = "^";

  // Cache
  public static final NonBlockingHashMap<ItemStack, String> cache = new NonBlockingHashMap<>();
  public static final NonBlockingHashMap<String, ItemStack> reverseCache = new NonBlockingHashMap<>();

  @Override
  public String getName() {
    return "ItemStack";
  }

  @Override
  public int getMeta(String data) {
    if (data.contains(META)) {
      try {
        String metaNum = data.substring(data.indexOf(META) + 1,
            data.contains(NBT) ?
                data.indexOf(NBT) :
                data.indexOf(">"));
        return Integer.parseInt(metaNum);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  @Override
  public int getDataSize(String data) {
    if (data.contains(COUNT + SEPARATOR)) {
      try {
        String sizeNum = data.substring(1, data.indexOf("x"));
        return Integer.parseInt(sizeNum);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
    return 1;
  }

  @Override
  public DataWrapper getName(String data) {
    String modid = "minecraft";
    String name = "air";
    boolean hasMeta = data.contains(META);
    boolean hasNBT = data.contains(NBT);
    int spacerCount = StringUtils.countMatches(
        hasMeta ? data.substring(1, data.indexOf(META) - 1)
            : (hasNBT ? data.substring(1, data.indexOf(NBT) - 1) : data), SEPARATOR);
    boolean hasModIDOrStackSize = spacerCount > 0;
    if (hasMeta && !hasModIDOrStackSize) {
      name = data.substring(1, data.indexOf(META));
    } else if (hasNBT && !hasModIDOrStackSize) {
      name = data.substring(1, data.indexOf(NBT));
    } else if (!hasModIDOrStackSize) {
      name = data.substring(1, data.length() - 1);
    }
    if (hasModIDOrStackSize) {
      boolean hasStackSize = data.contains(COUNT + SEPARATOR);
      int endPos = hasMeta ? data.indexOf(META)
          : (hasNBT ? data.indexOf(NBT) : data.length() - 1);
      String helper = data.substring(data.indexOf(SEPARATOR) + 1, endPos);
      if (hasStackSize && spacerCount == 1) {
        name = helper;
      } else {
        if (hasStackSize) {
          modid = helper.substring(0, helper.indexOf(SEPARATOR));
          name = helper.substring(helper.indexOf(SEPARATOR) + 1);
        } else {
          modid = data.substring(1, data.indexOf(SEPARATOR));
          name = helper;
        }
      }
    }
    return new DataWrapper(modid, name);
  }

  @Override
  public String getExtraData(String data) {
    if (data.contains(NBT)) {
      return data.substring(data.indexOf(NBT), data.length() - 1);
    }
    return null;
  }

  @Override
  public Object getBasicData(String data) {
    return ForgeRegistries.ITEMS.getValue(getName(data).toResourceLocation());
  }

  @Override
  public ItemStack getData(String data) {
    if (ConfigHandler.cacheConversions && reverseCache.containsKey(data)) {
      return reverseCache.get(data);
    }
    Item item = (Item) getBasicData(data);
    ItemStack stack = new ItemStack(item, getDataSize(data), getMeta(data));
//    String nbt = getExtraData(data);
//    if (nbt != null && !nbt.isEmpty()) {
//      try {
//        NBTTagCompound nbtData = JsonToNBT.getTagFromJson(nbt);
//        stack.setTagCompound(nbtData);
//      } catch (NBTException e) {
//        WurmTweaks2.LOGGER
//            .warn("Failed to load NBT for '" + data + "', creating without NBT");
//      }
//    }
    if (ConfigHandler.cacheConversions) {
      cache.put(stack.copy(), data);
      reverseCache.put(data, stack.copy());
    }
    return stack;
  }

  @Override
  public String toString(ItemStack data) {
    if (data.isEmpty()) {
      return "<empty>";
    }
    if (ConfigHandler.cacheConversions && cache.containsKey(data)) {
      return cache.get(data);
    }
    StringBuilder builder = new StringBuilder();
    builder.append("<");
    if (data.getCount() > 1) {
      builder.append(data.getCount());
      builder.append(COUNT);
      builder.append(SEPARATOR);
    }
    ResourceLocation name = data.getItem().getRegistryName();
    if (name != null && !name.getResourceDomain().isEmpty() && !name.getResourceDomain()
        .equalsIgnoreCase("minecraft")) {
      builder.append(name.getResourceDomain());
      builder.append(SEPARATOR);
    }
    builder.append(name.getResourcePath());
    if (data.getItemDamage() > 0) {
      builder.append(META);
      builder.append(data.getItemDamage());
    }
    if (data.getTagCompound() != null) {
      builder.append(NBT);
      builder.append(data.getTagCompound().toString());
    }
    builder.append(">");
    if (ConfigHandler.cacheConversions) {
      cache.put(data.copy(), builder.toString());
      reverseCache.put(builder.toString(), data.copy());
    }
    return builder.toString();
  }

  @Override
  public boolean isValid(String data) {
    if (ConfigHandler.cacheConversions && reverseCache.containsKey(data)) {
      return true;
    }
    try {
      getData(data);
      return true;
    } catch (Exception ignored) {
    }
    return false;
  }
}
