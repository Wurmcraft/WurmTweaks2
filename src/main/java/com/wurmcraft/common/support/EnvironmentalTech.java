package com.wurmcraft.common.support;

import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_botanic.VMBotanic;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_ore.VMOre;
import com.valkyrieofnight.et.m_multiblocks.m_voidminer.m_res.VMRes;
import com.valkyrieofnight.vlib.lib.stack.WeightedItemStack;
import com.wurmcraft.api.script.anotations.FinalizeSupport;
import com.wurmcraft.api.script.anotations.InitSupport;
import com.wurmcraft.api.script.anotations.ScriptFunction;
import com.wurmcraft.api.script.anotations.ScriptFunction.FunctionType;
import com.wurmcraft.api.script.anotations.Support;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Method;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

// TODO (3)
@Support(modid = "environmentaltech")
public class EnvironmentalTech {

  private static NonBlockingHashSet<Object[]> botanic;
  private static NonBlockingHashSet<Object[]> res;
  private static NonBlockingHashSet<Object[]> ore;

  @Method(modid = "environmentaltech")
  @InitSupport
  public void init() {
    botanic = new NonBlockingHashSet<>();
    res = new NonBlockingHashSet<>();
    ore = new NonBlockingHashSet<>();
    if (ConfigHandler.removeAllMachineRecipes) {
      VMBotanic.getInstance().T1.getList().clear();
      VMBotanic.getInstance().T1.getTargeters().clear();
      VMBotanic.getInstance().T2.getList().clear();
      VMBotanic.getInstance().T2.getTargeters().clear();
      VMBotanic.getInstance().T3.getList().clear();
      VMBotanic.getInstance().T3.getTargeters().clear();
      VMBotanic.getInstance().T4.getList().clear();
      VMBotanic.getInstance().T4.getTargeters().clear();
      VMBotanic.getInstance().T5.getList().clear();
      VMBotanic.getInstance().T5.getTargeters().clear();
      VMBotanic.getInstance().T6.getList().clear();
      VMBotanic.getInstance().T6.getTargeters().clear();
      VMRes.getInstance().T1.getList().clear();
      VMRes.getInstance().T1.getTargeters().clear();
      VMRes.getInstance().T2.getList().clear();
      VMRes.getInstance().T2.getTargeters().clear();
      VMRes.getInstance().T3.getList().clear();
      VMRes.getInstance().T3.getTargeters().clear();
      VMRes.getInstance().T4.getList().clear();
      VMRes.getInstance().T4.getTargeters().clear();
      VMRes.getInstance().T5.getList().clear();
      VMRes.getInstance().T5.getTargeters().clear();
      VMRes.getInstance().T6.getList().clear();
      VMRes.getInstance().T6.getTargeters().clear();
      VMOre.getInstance().VOM_T1.getList().clear();
      VMOre.getInstance().VOM_T1.getTargeters().clear();
      VMOre.getInstance().VOM_T2.getList().clear();
      VMOre.getInstance().VOM_T2.getTargeters().clear();
      VMOre.getInstance().VOM_T3.getList().clear();
      VMOre.getInstance().VOM_T3.getTargeters().clear();
      VMOre.getInstance().VOM_T4.getList().clear();
      VMOre.getInstance().VOM_T4.getTargeters().clear();
      VMOre.getInstance().VOM_T5.getList().clear();
      VMOre.getInstance().VOM_T5.getTargeters().clear();
      VMOre.getInstance().VOM_T6.getList().clear();
      VMOre.getInstance().VOM_T6.getTargeters().clear();
    } else if (ScriptExecutor.reload) {
      // TODO Handle Reload
    }
  }

  @Method(modid = "environmentaltech")
  @FinalizeSupport
  public void finishSupport() {
    for (Object[] recipe : botanic) {
      if ((int) recipe[0] == 1) {
        VMBotanic.getInstance().T1.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 2) {
        VMBotanic.getInstance().T2.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 3) {
        VMBotanic.getInstance().T3.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 4) {
        VMBotanic.getInstance().T4.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 5) {
        VMBotanic.getInstance().T5.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 6) {
        VMBotanic.getInstance().T6.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);
      }
    }
    for (Object[] recipe : res) {
      if ((int) recipe[0] == 1) {
        VMRes.getInstance().T1.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 2) {
        VMRes.getInstance().T2.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 3) {
        VMRes.getInstance().T3.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 4) {
        VMRes.getInstance().T4.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 5) {
        VMRes.getInstance().T5.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 6) {
        VMRes.getInstance().T6.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);
      }
    }
    for (Object[] recipe : ore) {
      if ((int) recipe[0] == 1) {
        VMOre.getInstance().VOM_T1.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 2) {
        VMOre.getInstance().VOM_T2.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 3) {
        VMOre.getInstance().VOM_T3.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 4) {
        VMOre.getInstance().VOM_T4.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 5) {
        VMOre.getInstance().VOM_T5.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);

      } else if ((int) recipe[0] == 6) {
        VMOre.getInstance().VOM_T6.addResource((WeightedItemStack) recipe[1], (String) recipe[2]);
      }
    }
  }

  @Method(modid = "environmentaltech")
  @ScriptFunction(modid = "environmentaltech", inputFormat = "ItemStack Integer Integer String")
  public void addBotanicMiner(Converter converter, String[] line) {
    botanic.add(
        new Object[] {
          Integer.parseInt(line[2]),
          new WeightedItemStack(
              (ItemStack) converter.convert(line[0], 1), Integer.parseInt(line[1])),
          line[3]
        });
  }

  @Method(modid = "environmentaltech")
  @ScriptFunction(modid = "environmentaltech", inputFormat = "ItemStack Integer Integer String")
  public void addResourceMiner(Converter converter, String[] line) {
    res.add(
        new Object[] {
          Integer.parseInt(line[2]),
          new WeightedItemStack(
              (ItemStack) converter.convert(line[0], 1), Integer.parseInt(line[1])),
          line[3]
        });
  }

  @Method(modid = "environmentaltech")
  @ScriptFunction(
    modid = "environmentaltech",
    inputFormat = "ItemStack Integer Integer String",
    typeData = "Laser",
    type = FunctionType.Linked
  )
  public void addOreMiner(Converter converter, String[] line) {
    ore.add(
        new Object[] {
          Integer.parseInt(line[2]),
          new WeightedItemStack(
              (ItemStack) converter.convert(line[0], 1), Integer.parseInt(line[1])),
          line[3]
        });
  }
}
