package com.wurmcraft.api.script;

public enum StackSettings {
  START("<", "type.start"),
  END(">", "type.end"),
  META("@", "type.meta"),
  STACKSIZE("x", "type.stackSize"),
  NAME(":", "type.name"),
  EXTRA_DATA("^", "type.extraData"),
  INFINITE("...", "type.infinite"),
  EXTRA("***", "type.extra"),
  // Special Types
  FLUID("*", "type.fluid"),
  ENTITY("#", "type.entity"),
  GAS("%", "type.gas"),
  EMPTY_STACK("empty", "type.empty"),
  SPACE("_", "type.space");

  private final String data;
  private final String langKey;

  StackSettings(String data, String langKey) {
    this.data = data;
    this.langKey = langKey;
  }

  public String getData() {
    return data;
  }

  public String getLangKey() {
    return langKey;
  }

  @Override
  public String toString() {
    return langKey;
  }
}
