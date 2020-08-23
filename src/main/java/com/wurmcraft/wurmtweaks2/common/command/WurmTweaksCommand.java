package com.wurmcraft.wurmtweaks2.common.command;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.api.conversion.DataWrapper;
import com.wurmcraft.wurmtweaks2.api.conversion.IDataConverter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
    return "/wt <hand, convert> <data>";
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
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    }
  }
}
