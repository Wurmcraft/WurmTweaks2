package com.wurmcraft.api.script;

import net.minecraft.util.ResourceLocation;

/** Custom Implementation of ResourceLocation to does not automatically ToLowerCase() */
public class DataWrapper {

  private String modid;
  private String name;

  /**
   * Custom Implementation of ResourceLocation to does not automatically ToLowerCase()
   *
   * @param modid Stacks Modid
   * @param name Stacks Name
   */
  public DataWrapper(String modid, String name) {
    this.modid = modid;
    this.name = name;
  }

  public String getModid() {
    return modid;
  }

  public String getName() {
    return name;
  }

  public ResourceLocation toResourceLocation() {
    return new ResourceLocation(modid, name);
  }

  public String toString() {
    return this.modid + StackSettings.NAME + this.name;
  }

  @Override
  public boolean equals(Object otherDataWrapper) {
    if (this == otherDataWrapper) {
      return true;
    } else if (!(otherDataWrapper instanceof DataWrapper)) {
      return false;
    } else {
      DataWrapper otherWrapper = (DataWrapper) otherDataWrapper;
      return this.modid.equals(otherWrapper.modid) && this.name.equals(otherWrapper.name);
    }
  }
}
