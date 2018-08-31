package com.wurmcraft.common.events;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootDestroyer {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void addLoot (LootTableLoadEvent e) {
    LootTable table = e.getTable ();
    if (table.isFrozen ()) {
      try {
        Field field = getField (table.getClass (),"isFrozen","isFrozen");
        field.setAccessible (true);
        field.setBoolean (table,false);
      } catch (Exception e1) {
        e1.printStackTrace ();
      }
    }
    List<LootPool> pools = new ArrayList<>();
    try {
      pools = (List <LootPool>) getField (table.getClass (),"pools","field_186466_c").get (table);
    } catch (Exception e1) {
      e1.printStackTrace ();
    }
    List <String> markForRemoval = new ArrayList <> ();
    if (pools.size () > 0)
      for (LootPool pool : pools)
        markForRemoval.add (pool.getName ());
    for (String name : markForRemoval)
      e.getTable ().removePool (name);
    e.setTable (table);
  }

  public Field getField (Class clazz,String obd,String non) {
    try {
      Field field = clazz.getDeclaredField (obd);
      if (field != null) {
        field.setAccessible (true);
        Field modifiersField = Field.class.getDeclaredField ("modifiers");
        modifiersField.setAccessible (true);
        modifiersField.setInt (field,field.getModifiers () & ~Modifier.FINAL);
        return field;
      }
    } catch (Exception e) {
      try {
        Field field = clazz.getDeclaredField (non);
        if (field != null) {
          field.setAccessible (true);
          Field modifiersField = Field.class.getDeclaredField ("modifiers");
          modifiersField.setAccessible (true);
          modifiersField.setInt (field,field.getModifiers () & ~Modifier.FINAL);
          return field;
        }
      } catch (Exception f) {
        f.printStackTrace ();
      }
    }
    return null;
  }

}
