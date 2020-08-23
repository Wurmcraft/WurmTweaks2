package com.wurmcraft.wurmtweaks2.api.conversion;

public interface IDataConverter<T> {

  String getName();

  int getMeta(String data);

  int getDataSize(String data);

  DataWrapper getName(String data);

  String getExtraData(String data);

  Object getBasicData(String data);

  T getData(String data);

  String toString(T data);

  boolean isValid(String data);

}
