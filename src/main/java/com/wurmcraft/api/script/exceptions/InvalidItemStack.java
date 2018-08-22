package com.wurmcraft.api.script.exceptions;

/**
 * Thrown when a Invalid ItemStack is found trying to be converted
 */
public class InvalidItemStack extends IllegalArgumentException {

  public InvalidItemStack() {
  }

  public InvalidItemStack(String s) {
    super(s);
  }
}
