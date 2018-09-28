package com.wurmcraft.api.script.converter;

import com.wurmcraft.api.script.DataWrapper;

/**
 * Allows for the creation of custom data converters
 *
 * <p>DataConverts are used by WurmScript to convert a string into a <T>
 *
 * @param <T> data type this converter will convert the string into
 */
public interface IDataConverter<T> {

  String getName();

  /**
   * Collects the Objects Meta value (-1 for none)
   *
   * @return meta value of the DataStack
   */
  int getMeta(String data);

  /**
   * Collects the Objects Size
   *
   * @return Size of the DataStack
   */
  int getDataSize(String data);

  /**
   * Collects the Object Name Usually in modid:name format
   *
   * @return DataWrapper of the DataStacks name
   */
  DataWrapper getName(String data);

  /**
   * Collects the Objects extra data(empty String for none) An Example of this is ItemStack NBT
   *
   * @return String of extra data
   */
  String getExtraData(String data);

  /**
   * Simplified Version of The Object An example if this is an Item from ItemStack
   *
   * @return Simplifiyed Version of the Full DataType if not just return the full dataType
   */
  Object getBasicData(String data);

  /**
   * Fully Converts the DataStack to its full form with all its extra data
   *
   * @return Fully converted DataStack
   */
  T getData(String data);

  /**
   * Fully converts the Object into its String variant
   *
   * @return String version of the Object
   */
  String toString(T data);

  /**
   * Checks if the Object input is a valid of this type
   *
   * @return If the Object is of this type
   */
  boolean isValid(String data);

  /**
   * Print Based on the current Script Location, Prints a message based on were the script is
   * currently running
   *
   * @param message Message to be displayed (Used for errors or debug)
   */
  void print(String message);
}
