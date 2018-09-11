package com.wurmcraft.common.script.converters;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.DataWrapper;
import com.wurmcraft.api.script.StackSettings;
import com.wurmcraft.api.script.anotations.DataConverter;
import com.wurmcraft.api.script.converter.IDataConverter;
import com.wurmcraft.api.script.exceptions.InvalidItemStack;
import com.wurmcraft.common.script.utils.IngredientWrapper;
import com.wurmcraft.common.support.Minecraft;
import com.wurmcraft.common.support.Minecraft.OreEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@DataConverter
public class OreDictConverter implements IDataConverter<Ingredient> {

  private static final Thread mainThread = Thread.currentThread();
  private static NonBlockingHashMap<String, Ingredient> cachedData;
  private Logger log;

  public OreDictConverter() {
    cachedData = new NonBlockingHashMap<>();
    this.log = WurmTweaks.logger;
  }

  public OreDictConverter(boolean cacheData) {
    if (cacheData) {
      cachedData = new NonBlockingHashMap<>();
    }
    this.log = WurmTweaks.logger;
  }

  @Override
  public String getName() {
    return "OreDictionary";
  }

  @Override
  public int getMeta(String data) {
    return -1;
  }

  @Override
  public int getDataSize(String data) {
    return 1;
  }

  @Override
  public DataWrapper getName(String data) {
    String oreEntry =
        data.contains(StackSettings.START.getData()) ? data.substring(1, data.length() - 1) : data;
    if (oreEntry.contains(StackSettings.NAME.getData())) {
      return new DataWrapper("null", "null");
    }
    boolean valid;
    if (cachedData != null && cachedData.size() > 0 && cachedData.contains(oreEntry)) {
      return new DataWrapper("oredict", oreEntry);
    }
    synchronized (mainThread) {
      valid = OreDictionary.doesOreNameExist(oreEntry);
    }
    if (!valid && !oreEntry.contains(StackSettings.NAME.getData())) {
      List<ItemStack> cache = new ArrayList<>();
      for (OreEntry entry : Minecraft.scriptOreEntry.toArray(new OreEntry[0])) {
        if (entry.values.equalsIgnoreCase(oreEntry)) {
          cache.add(entry.entry);
          valid = true;
        }
      }
      if (cache.size() > 0) {
        cachedData.put(oreEntry, new IngredientWrapper(cache.toArray(new ItemStack[0])));
      }
    }
    if (valid) {
      if (cachedData != null && !cachedData.containsKey(oreEntry)) {
        synchronized (mainThread) {
          cachedData.put(oreEntry, new IngredientWrapper(OreDictionary.getOres(oreEntry)));
        }
      }
      return new DataWrapper("oredict", oreEntry);
    }
    return new DataWrapper("null", "null");
  }

  @Override
  public String getExtraData(String data) {
    return "";
  }

  @Override
  public String getBasicData(String data) {
    DataWrapper name = getName(data);
    return name != null && !name.getModid().equals("null") ? name.getName() : "";
  }

  @Override
  public Ingredient getData(String data) {
    String entry = getBasicData(data);
    if (!entry.isEmpty() && cachedData != null && cachedData.containsKey(getBasicData(entry))) {
      return cachedData.get(entry);
    }
    return getIngredient(data);
  }

  private Ingredient getIngredient(String data) {
    if (data.startsWith(StackSettings.START.getData())
        && data.endsWith(StackSettings.END.getData())
        && !data.contains(StackSettings.NAME.getData())) {
      if (data.substring(
              data.indexOf(StackSettings.START.getData()) + 1,
              data.indexOf(StackSettings.END.getData()))
          .matches(StackSettings.EMPTY_STACK.getData())) {
        return Ingredient.EMPTY;
      }
      String entry = getBasicData(data);
      if (cachedData != null && cachedData.containsKey(entry)) {
        return cachedData.get(entry);
      }
      synchronized (mainThread) {
        NonNullList<ItemStack> stack = OreDictionary.getOres(entry);
        return new IngredientWrapper(stack);
      }
    }
    throw new InvalidItemStack("Invalid Ingredient '" + data + "'");
  }

  @Override
  public String toString(Ingredient data) {
    StringBuilder builder = new StringBuilder();
    builder.append(StackSettings.START.getData());
    String oreEntry = findOreEntry(data);
    if (oreEntry.isEmpty()) {
      throw new InvalidItemStack("Invalid Ingredient '" + oreEntry + "'");
    }
    builder.append(oreEntry);
    builder.append(StackSettings.END.getData());
    return builder.toString();
  }

  private String findOreEntry(Ingredient ingredient) {
    if (cachedData != null && cachedData.contains(ingredient)) {
      for (String entry : cachedData.keySet()) {
        if (cachedData.get(entry).equals(ingredient)) {
          return entry;
        }
      }
    }
    synchronized (mainThread) {
      ItemStack[] testItems = ingredient.getMatchingStacks();
      HashMap<ItemStack, int[]> oreIds = new HashMap<>();
      for (ItemStack stack : testItems) {
        oreIds.put(stack, OreDictionary.getOreIDs(stack));
      }
      int sameID = locateSameOreID(oreIds);
      String oreName = OreDictionary.getOreName(sameID);
      return oreName.equals("Unknown") ? "" : oreName;
    }
  }

  private int locateSameOreID(HashMap<ItemStack, int[]> oreDict) {
    if (oreDict.size() > 0) {
      int sameID = -1;
      for (ItemStack entry : oreDict.keySet()) {
        for (int id : oreDict.get(entry)) {
          if (sameID >= 0) {
            if (sameID != id) {
              return -1;
            }
          } else {
            sameID = id;
          }
        }
      }
      return sameID;
    }
    return -1;
  }

  @Override
  public boolean isValid(String data) {
    String basicData = getBasicData(data);
    return !basicData.isEmpty();
  }

  @Override
  public void print(String message) {
    if (message.startsWith("[Debug]") && message.length() > 8) {
      log.debug(message.substring(8, message.length()));
    } else {
      log.info(message);
    }
  }
}
