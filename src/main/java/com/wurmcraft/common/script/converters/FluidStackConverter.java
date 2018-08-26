package com.wurmcraft.common.script.converters;


import com.wurmcraft.WurmTweaks;
import com.wurmcraft.api.script.DataWrapper;
import com.wurmcraft.api.script.StackSettings;
import com.wurmcraft.api.script.anotations.DataConverter;
import com.wurmcraft.api.script.converter.IDataConverter;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@DataConverter
public class FluidStackConverter implements IDataConverter<FluidStack> {

  private static final Thread mainThread = Thread.currentThread();
  private NonBlockingHashMap<String, FluidStack> cachedData;

  public FluidStackConverter() {
    cachedData = new NonBlockingHashMap<>();
  }

  public FluidStackConverter(boolean cacheData) {
    if (cacheData) {
      cachedData = new NonBlockingHashMap<>();
    }
  }

  @Override
  public String getName() {
    return "FluidStack";
  }

  @Override
  public int getMeta(String data) {
    return -1;
  }

  @Override
  public int getDataSize(String data) {
    if (data.contains(StackSettings.STACKSIZE.getData())) {
      String fluidSize = data.substring(data.indexOf(StackSettings.START.getData()) + 1,
          data.indexOf(StackSettings.STACKSIZE.getData()));
      try {
        return Integer.parseInt(fluidSize);
      } catch (NumberFormatException e) {
        print("Invalid Number (FluidSize) '" + fluidSize + "'");
      }
    }
    return 0;
  }

  @Override
  public DataWrapper getName(String data) {
    if (data.contains(StackSettings.START.getData()) && data
        .contains(StackSettings.END.getData())) {
      String name = data.substring(data.indexOf(StackSettings.STACKSIZE.getData()) + 1,
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
      return FluidRegistry.getFluid(getName(data).getName());
    }
  }

  @Override
  public FluidStack getData(String data) {
    if (cachedData != null && cachedData.containsKey(data)) {
      return cachedData.get(data);
    }
    Fluid fluid = (Fluid) getBasicData(data);
    if (fluid != null) {
      FluidStack stack = new FluidStack(fluid, getDataSize(data));
      cachedData.put(data, stack);
      return stack;
    }
    return null;
  }

  @Override
  public String toString(FluidStack data) {
    return StackSettings.START.getData() + data.amount + StackSettings.STACKSIZE.getData() + data
        .getUnlocalizedName().substring(5) + StackSettings.END.getData();
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
