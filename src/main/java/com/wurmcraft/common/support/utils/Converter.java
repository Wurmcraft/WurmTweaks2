package com.wurmcraft.common.support.utils;

import com.wurmcraft.api.WurmTweak2API;
import com.wurmcraft.api.script.converter.IDataConverter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class Converter {

  public static IDataConverter getFromName(String name) {
    for (IDataConverter a : WurmTweak2API.dataConverters) {
      if (a.getName().equalsIgnoreCase(name)) {
        return a;
      }
    }
    return null;
  }

  public IDataConverter attemptToFindConverter(String line) {
    for (IDataConverter converter : WurmTweak2API.dataConverters) {
      if (converter.isValid(line)) {
        return converter;
      }
    }
    return null;
  }

  public Object convert(String line, int extraData) {
    IDataConverter converter = attemptToFindConverter(line);
    if (converter != null) {
      return converter.getData(line);
    }
    return null;
  }

  public Object convert(String line) {
    return convert(line, 0);
  }

  public Object[] getBulkItems(String[] lines) {
    List<Object> list = new ArrayList<>();
    for (String line : lines) {
      list.add(convert(line));
    }
    return list.toArray();
  }

  public List<ItemStack> getBulkItemsAsList(String[] lines) {
    List<ItemStack> list = new ArrayList<>();
    for (String line : lines) {
      Object data = convert(line);
      if (data instanceof ItemStack) {
        list.add((ItemStack) convert(line));
      }
    }
    return list;
  }
}
