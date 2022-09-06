package com.wurmcraft.wurmtweaks2.common.command;

import static com.wurmcraft.wurmtweaks2.common.script.data.RecipeUtils.recipeLock;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.api.conversion.DataWrapper;
import com.wurmcraft.wurmtweaks2.api.conversion.IDataConverter;

import com.wurmcraft.wurmtweaks2.common.script.data.InvalidRecipe;
import com.wurmcraft.wurmtweaks2.common.script.data.ShapedRecipeWT;
import com.wurmcraft.wurmtweaks2.common.script.data.ShapelessRecipeWT;
import com.wurmcraft.wurmtweaks2.common.script.event.HarvestSpeed;
import com.wurmcraft.wurmtweaks2.common.script.jython.recipes.FurnaceRecipe;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.wurmcraft.wurmtweaks2.common.script.ScriptRunner;
import com.wurmcraft.wurmtweaks2.common.script.loader.ScriptIO;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class WurmTweaksCommand extends CommandBase {

  @Override
  public String getName() {
    return "WurmTweaks";
  }

  @Override
  public List<String> getAliases() {
    List<String> aliases = new ArrayList<>();
    aliases.add("wt");
    aliases.add(getName().toLowerCase());
    return aliases;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/wt <hand, convert, run, undo> <data>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      IDataConverter<ItemStack> itemStackConverter = WurmTweaks2API.dataConverters
          .get("ItemStack");
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (args.length == 1 && args[0].equalsIgnoreCase("hand")) {
        String item = itemStackConverter.toString(player.getHeldItemMainhand());
        player.sendMessage(new TextComponentString("Stack: " + item));
      } else if (args.length == 2 && args[0].equalsIgnoreCase("convert")
          || args.length == 2 && args[0].equalsIgnoreCase("load")) {
        String itemData = args[1];
        ItemStack stack = itemStackConverter.getData(itemData);
        player.sendMessage(new TextComponentString(
            "Adding item '" + stack.getDisplayName() + " to your inventory"));
        player.sendMessage(new TextComponentString(
            "Item: " + itemStackConverter.getName(itemData).name + " "
                + ((Item) itemStackConverter.getBasicData(itemData)).getRegistryName()));
        player.inventory.addItemStackToInventory(stack);
      } else if (args.length == 2 && args[0].equalsIgnoreCase("test")) {
        String data = args[1];
        DataWrapper wrapper = itemStackConverter.getName(data);
        player.sendMessage(new TextComponentString("ModID: " + wrapper.modid));
        player.sendMessage(new TextComponentString("Name: " + wrapper.name));
        player.sendMessage(new TextComponentString(
            "StackSize: " + itemStackConverter.getDataSize(data)));
        player.sendMessage(
            new TextComponentString("Meta: " + itemStackConverter.getMeta(data)));
      } else if (args.length == 2 && args[0].equalsIgnoreCase("run")) {
        File[] scripts = ScriptIO.getScripts();
        for (File script : scripts) {
          if (script.getName().equalsIgnoreCase(args[1])) {
            sender.sendMessage(
                new TextComponentString("Running Script '" + script.getName() + "'"));
            Writer writer = new StringWriter();
            try {
              ScriptRunner.createBindings();
              ScriptContext context = new SimpleScriptContext();
              context.setBindings(ScriptRunner.createBindings(),
                  ScriptContext.GLOBAL_SCOPE);
              context.setBindings(ScriptRunner.createBindings(),
                  ScriptContext.ENGINE_SCOPE);
              context.setAttribute(ScriptEngine.FILENAME, script.getName(),
                  ScriptContext.ENGINE_SCOPE);
              context.setAttribute("mc_version", "1.12.2", ScriptContext.GLOBAL_SCOPE);
              context.setWriter(writer);
//                            ScriptRunner.engine.eval(new FileReader(script));
              ScriptRunner.engine.eval(
                  Strings.join(ScriptRunner.core_py, "") + Strings.join(
                      Files.readAllLines(script.toPath()), "\n"), context);
            } catch (ScriptException | FileNotFoundException e) {
              sender.sendMessage(new TextComponentString(e.getMessage()));
            } catch (IOException e) {
              e.printStackTrace();
            }
            return;
          }
        }
        sender.sendMessage(
            new TextComponentString("Script " + args[1] + " does not exist!"));
      } else if (args.length == 1 && args[0].equals("undo")) {
        recipeLock(false);
        // Undo IRecipe
        for (Object key : ForgeRegistries.RECIPES.getKeys().toArray()) {
          IRecipe recipe = ForgeRegistries.RECIPES.getValue((ResourceLocation) key);
          if (recipe instanceof ShapedRecipeWT) {
            ((ShapedRecipeWT) recipe).enabled = false;
            ForgeRegistries.RECIPES.register(InvalidRecipe.from(recipe));
          }
          if (recipe instanceof ShapelessRecipeWT) {
            ((ShapelessRecipeWT) recipe).enabled = false;
            ForgeRegistries.RECIPES.register(InvalidRecipe.from(recipe));
          }
        }
        sender.sendMessage(
            new TextComponentString("Shaped / Shapeless Recipes have been removed"));
        // Undo Furnace
        for (ItemStack input : FurnaceRecipe.currentRecipes.keySet()) {
          FurnaceRecipes.instance().getSmeltingList().remove(input);
        }
        FurnaceRecipe.currentRecipes.clear();
        HarvestSpeed.harvestSpeedCache.clear();
        sender.sendMessage(
            new TextComponentString("Furnace Recipes have been removed"));
        // Undo Brewing Recipes
        // TODO Undo brewing recipes
        recipeLock(true);
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    }
  }
}
