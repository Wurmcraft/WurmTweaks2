package com.wurmcraft.script.utils;

import com.google.common.base.Preconditions;
import com.wurmcraft.api.IModSupport;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.script.exception.InvalidStackException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

//TODO a lot of methods in here can be static
/**
 * Simplify creation of IModSupport
 *
 * @see IModSupport
 */
public abstract class SupportHelper implements IModSupport {

 protected String modid;

 /**
  * @param modid ID used by the mod and forge
  */
 public SupportHelper(String modid) {
  this.modid = modid;
 }

 /**
  * @see IModSupport#finishSupport()
  */
 @Override
 public abstract void finishSupport();

 //TODO REDUNDANT :: Literally every mod support class splits the string in the same way, multiple times
 @Deprecated
 /**
  * Verifiys if the script line is valid and ready to proccess
  *
  * @param line         Script Line
  * @param condition    Used to check if the script line is valid
  * @param errorMessage Message to display if condition is not met.
  * @return a String[] if the line is valid
  */
 protected String[] validateFormat(String line, boolean condition, String errorMessage) {
  if (condition) return line.split(" ");
  //TODO Log
  System.out.println(errorMessage);
  return new String[0];
 }

 /**
  * Converts a Scripts String into a Item
  *
  * @param helper StackHelper Object from the thread
  * @param stack  Script ItemStack String
  * @see StackHelper#convert(String)
  */
 protected ItemStack convertStack(StackHelper helper, String stack) {
  return helper.convert(stack);
 }

 /**
  * Converts a Scripts String into a Ingedient
  *
  * @param helper StackHelper Object from the thread
  * @param stack  Script Ingredient String
  */
 protected Ingredient convertIngredient(StackHelper helper, String stack) {
  return helper.convertIngredient(stack);
 }

 /**
  * Converts a Scripts String into a FluidStack
  *
  * @param helper StackHelper Object from the thread
  * @param stack  Script FluidStack String
  */
 protected FluidStack convertFluidStack(StackHelper helper, String stack) {
  return helper.convertFluid(stack);
 }

 //TODO why, not only is it not needed, it should be a static utility
 @Deprecated
 /**
  * Converts a Scripts String into a Float
  *
  * @param stack StackHelper Object from the thread
  */
 protected float convertFloat(String stack) {
  return Float.parseFloat(stack);
 }

 //TODO why, not only is it not needed, it should be a static utility
 @Deprecated
 /**
  * Converts a Scripts String into a Float
  *
  * @param stack StackHelper Object from the thread
  */
 protected int convertInteger(String stack) {
  return Integer.parseInt(stack);
 }

 /**
  * Checks if a Script OreDict entry is valid
  *
  * @param helper   StackHelper Object from the thread
  * @param oreEntry Script oreDict Entry
  */
 protected boolean isOre(StackHelper helper, String oreEntry) {
  return helper.isOreEntry(oreEntry);
 }

 //TODO REDUNDANT
 @Deprecated
 /**
  * Checks if a script object is valid at a certain type
  *
  * @param type   Type of Object to test for
  * @param helper StackHelper Object from thread
  * @param item   possible script Item
  */
 protected void isValid(EnumInputType type,StackHelper helper,String... item) {
  for (String i : item)
   if (type.equals(EnumInputType.ITEMSTACK))
    checkNotNull(type, convertStack(helper, i), "Invalid ItemStack '" + i + "'");
   else if (type.equals(EnumInputType.INGREDENT))
    checkNotNull(type, convertIngredient(helper, i), "Invalid Ingredient '" + i + "'");
   else if (type.equals(EnumInputType.FLUIDSTACK))
    checkNotNull(type, convertFluidStack(helper, i), "Invalid FluidStack '" + i + "'");
   else if (type.equals(EnumInputType.ORE))
    Preconditions.checkArgument(isOre(helper, i), "Invalid Ore Entry '" + i + "'");
   else if (type.equals(EnumInputType.STRING))
    Preconditions.checkArgument(i.length() >= 3, "Invalid String '" + i + "'");
   else if (type.equals(EnumInputType.INTEGER))
    try {
     Integer.parseInt(i);
    } catch (NumberFormatException e) {
     System.out.println("Invalid Integer '" + i + "'");
    }
   else if (type.equals(EnumInputType.FLOAT))
    try {
     Float.parseFloat(i);
    } catch (NumberFormatException e) {
     System.out.println("Invalid Float '" + i + "'");
    }
 }

 //TODO REDUNDANT
 @Deprecated
 /**
  * Simplifiyed version of
  *
  * @param helper StackHelper Object from thread
  * @param item   ItemStack Script Object
  * @see SupportHelper#isValid(EnumInputType, StackHelper, String...)
  */
 protected void isValid(StackHelper helper, String... item) {
  isValid(EnumInputType.ITEMSTACK, helper, item);
 }

 /**
  * Checks if the Script Object is null and if so throw an exception
  */
 private void checkNotNull(EnumInputType type,Object object,String errorMessage) throws InvalidStackException {
  if (object == null)
   throw new InvalidStackException(errorMessage);
 }

 /**
  * Helps the constructor by setting the supports modid
  *
  * @return modid
  */
 @Override
 public String modid() {
  return modid;
 }

 /**
  * @see IModSupport#init() ()
  */
 @Override
 public abstract void init();
}
