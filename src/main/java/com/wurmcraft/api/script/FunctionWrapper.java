package com.wurmcraft.api.script;

import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;

public class FunctionWrapper {

  private String modid;
  private String supportDependencies;
  private boolean threaded;
  private byte supportCode;
  private FunctionType type;
  private boolean precedence;
  private String name;
  private String typeData;
  private String inputFormat;
  private String[] guiVar;
  private Object function;
  private Object clazz;

  public FunctionWrapper(
      String modid,
      String supportDependencies,
      boolean threaded,
      byte supportCode,
      FunctionType type,
      boolean precedence,
      String name,
      String typeData,
      String inputFormat,
      String[] guiVar,
      Object function,
      Object clazz) {
    this.modid = modid;
    this.supportDependencies = supportDependencies;
    this.threaded = threaded;
    this.supportCode = supportCode;
    this.type = type;
    this.precedence = precedence;
    this.name = name;
    this.typeData = typeData;
    this.inputFormat = inputFormat;
    this.guiVar = guiVar;
    this.function = function;
    this.clazz = clazz;
  }

  @Override
  public String toString() {
    return "FunctionWrapper{"
        + "modid='"
        + modid
        + '\''
        + ", threaded="
        + threaded
        + ", supportCode="
        + supportCode
        + ", type="
        + type
        + ", precedence="
        + precedence
        + ", name='"
        + name
        + '\''
        + '}';
  }

  public String getModid() {
    return modid;
  }

  public String getSupportDependencies() {
    return supportDependencies;
  }

  public boolean isThreaded() {
    return threaded;
  }

  public byte getSupportCode() {
    return supportCode;
  }

  public FunctionType getType() {
    return type;
  }

  public boolean isPrecedence() {
    return precedence;
  }

  public String getName() {
    return name;
  }

  public String getTypeData() {
    return typeData;
  }

  public String getInputFormat() {
    return inputFormat;
  }

  public String[] getGuiVar() {
    return guiVar;
  }

  public Object getFunction() {
    return function;
  }

  public Object getClazz() {
    return clazz;
  }
}
