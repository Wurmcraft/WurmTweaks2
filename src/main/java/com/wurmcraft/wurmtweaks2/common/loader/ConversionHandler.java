package com.wurmcraft.wurmtweaks2.common.loader;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.api.conversion.IDataConverter;
import com.wurmcraft.wurmtweaks2.common.conversion.ItemStackConverter;
import java.util.HashMap;

public class ConversionHandler {

  public static void register(IDataConverter converter) {
    if(WurmTweaks2API.dataConverters == null)
      WurmTweaks2API.dataConverters = new HashMap<>();
    WurmTweaks2API.dataConverters.put(converter.getName(), converter);
  }

  public static HashMap<String, IDataConverter> setupConversions() {
    register(new ItemStackConverter());
    return WurmTweaks2API.dataConverters;
  }
}
