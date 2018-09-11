package com.wurmcraft.common.script.converters;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.DataWrapper;
import com.wurmcraft.api.script.StackSettings;
import com.wurmcraft.api.script.anotations.DataConverter;
import com.wurmcraft.api.script.converter.IDataConverter;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@DataConverter
public class GasStackConverter implements IDataConverter<GasStack> {

  private static final Thread mainThread = Thread.currentThread();
  private NonBlockingHashMap<String, GasStack> cachedData;

  public GasStackConverter() {
    cachedData = new NonBlockingHashMap<>();
  }

  public GasStackConverter(boolean cacheData) {
    if (cacheData) {
      cachedData = new NonBlockingHashMap<>();
    }
  }

  @Override
  public String getName() {
    return "GasStack";
  }

  @Override
  public int getMeta(String data) {
    return -1;
  }

  @Override
  public int getDataSize(String data) {
    if (data.contains(StackSettings.STACKSIZE.getData())) {
      String fluidSize =
          data.substring(
              data.indexOf(StackSettings.GAS.getData()) + 1,
              data.indexOf(StackSettings.STACKSIZE.getData()));
      try {
        return Integer.parseInt(fluidSize);
      } catch (NumberFormatException e) {
        print("Invalid Number (GasSize) '" + fluidSize + "'");
      }
    }
    return 0;
  }

  @Override
  public DataWrapper getName(String data) {
    if (data.contains(StackSettings.START.getData())
        && data.contains(StackSettings.END.getData())
        && data.contains(StackSettings.GAS.getData())) {
      String name =
          data.substring(
              data.indexOf(StackSettings.STACKSIZE.getData()) + 1,
              data.indexOf(StackSettings.END.getData()));
      return new DataWrapper("N/A", name);
    }
    return new DataWrapper("null", "null");
  }

  @Override
  public String getExtraData(String data) {
    return "";
  }

  @Override
  public Object getBasicData(String data) {
    synchronized (mainThread) {
      return GasRegistry.getGas(getName(data).getName());
    }
  }

  @Override
  public GasStack getData(String data) {
    if (cachedData != null && cachedData.containsKey(data)) {
      return cachedData.get(data);
    }
    Gas fluid = (Gas) getBasicData(data);
    if (fluid != null) {
      GasStack stack = new GasStack(fluid, getDataSize(data));
      cachedData.put(data, stack);
      return stack;
    }
    return null;
  }

  @Override
  public String toString(GasStack data) {
    return StackSettings.START.getData()
        + StackSettings.GAS.getData()
        + data.amount
        + StackSettings.STACKSIZE.getData()
        + data.getGas().getUnlocalizedName().substring(5)
        + StackSettings.END.getData();
  }

  @Override
  public boolean isValid(String data) {
    return getData(data) != null;
  }

  @Override
  public void print(String message) {
    WurmTweaks.logger.error(message);
  }
}
