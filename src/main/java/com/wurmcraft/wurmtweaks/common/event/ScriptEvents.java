package com.wurmcraft.wurmtweaks.common.event;

import com.wurmcraft.wurmtweaks.reference.Local;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber
public class ScriptEvents {

	public static List <ItemStack> throwEvent = new ArrayList <> ();
	private static List <Item> throwQuickCheck = new ArrayList <> ();
	public static HashMap <ItemStack, ItemStack> dropPickupEvent = new HashMap <> ();
	public static List <Item> pickupQuick = new ArrayList <> ();
	public static HashMap <ItemStack, String[]> tooltipEvent = new HashMap <> ();
	public static List <Item> tooltipQuick = new ArrayList <> ();

	public static void addThrowCancelEvent (ItemStack stack) {
		if (stack != ItemStack.EMPTY) {
			throwQuickCheck.add (stack.getItem ());
			throwEvent.add (stack);
		}
	}

	public static void addPickupConversion (ItemStack pickupItem,ItemStack conversion) {
		if (pickupItem != ItemStack.EMPTY) {
			dropPickupEvent.put (pickupItem,conversion);
			pickupQuick.add (pickupItem.getItem ());
		}
	}

	public static void addToolTipEntry (ItemStack stack,String[] tooltip) {
		if (stack != ItemStack.EMPTY) {
			tooltipEvent.put (stack,tooltip);
			tooltipQuick.add (stack.getItem ());
		}
	}

	@SubscribeEvent
	public void onItemDropEvent (ItemTossEvent e) {
		if (throwEvent.size () > 0 && throwQuickCheck.size () > 0 && throwQuickCheck.contains (e.getEntityItem ().getItem ().getItem ()))
			for (ItemStack item : throwEvent)
				if (StackHelper.isSameIgnoreSize (item,e.getEntityItem ().getItem ())) {
					ItemStack stack = e.getEntityItem ().getItem ();
					e.setCanceled (true);
					e.getPlayer ().inventory.addItemStackToInventory (stack);
					e.getPlayer ().sendMessage (new TextComponentTranslation (Local.CHAT_ITEMTOSS_DENY));
				}
	}

	@SubscribeEvent
	public void onPickupEvent (EntityItemPickupEvent e) {
		if (pickupQuick.size () > 0 && dropPickupEvent.size () > 0 && pickupQuick.contains (e.getItem ().getItem ().getItem ()))
			for (ItemStack item : dropPickupEvent.keySet ())
				if (StackHelper.isSameIgnoreSize (item,e.getItem ().getItem ())) {
					e.getItem ().setPickupDelay (1);
					e.getItem ().setDead ();
					e.getEntityPlayer ().inventory.addItemStackToInventory (convertToCorrect (e.getItem ().getItem (),dropPickupEvent.get (item)));
				}
	}

	private ItemStack convertToCorrect (ItemStack pickupItem,ItemStack convertion) {
		if (convertion == ItemStack.EMPTY)
			return ItemStack.EMPTY;
		else if (pickupItem.getCount () == convertion.getCount ())
			return convertion;
		else if (pickupItem.getCount () > convertion.getCount ()) {
			ItemStack temp = convertion.copy ();
			temp.setCount (pickupItem.getCount () / convertion.getCount ());
			return temp;
		} else if (pickupItem.getCount () < convertion.getCount ()) {
			ItemStack temp = convertion.copy ();
			temp.setCount (pickupItem.getCount () * convertion.getCount ());
			return temp;
		}
		return ItemStack.EMPTY;
	}

	@SubscribeEvent
	@SideOnly (value = Side.CLIENT)
	public void onRenderTooltip (ItemTooltipEvent e) {
		if (tooltipQuick.size () > 0 && tooltipEvent.size () > 0 && tooltipQuick.contains (e.getItemStack ().getItem ()))
			for (ItemStack item : tooltipEvent.keySet ())
				if (StackHelper.isSameIgnoreSize (item,e.getItemStack ()))
					for (String str : tooltipEvent.get (item))
						e.getToolTip ().add (str.replaceAll ("&","§"));
	}
}
