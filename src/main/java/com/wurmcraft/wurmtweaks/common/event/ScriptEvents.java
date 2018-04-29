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

	private static List <ItemStack> throwEvent = new ArrayList <> ();
	private static HashMap <ItemStack, ItemStack> dropPickupEvent = new HashMap <> ();
	private static List <Item> pickupQuick = new ArrayList <> ();
	private static HashMap <ItemStack, String[]> tooltipEvent = new HashMap <> ();
	private static List <Item> tooltipQuick = new ArrayList <> ();
	private static List <Item> throwQuickCheck = new ArrayList <> ();

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

	private ItemStack convertToCorrect (ItemStack pickupItem,ItemStack conversion) {
		if (conversion == ItemStack.EMPTY)
			return ItemStack.EMPTY;
		else if (pickupItem.getCount () == conversion.getCount ())
			return conversion;
		else if (pickupItem.getCount () > conversion.getCount () && conversion.getCount () > 0) {
			ItemStack temp = conversion.copy ();
			temp.setCount (pickupItem.getCount () / conversion.getCount ());
			return temp;
		} else {
			ItemStack temp = conversion.copy ();
			temp.setCount (pickupItem.getCount () * conversion.getCount ());
			return temp;
		}
	}

	@SubscribeEvent
	@SideOnly (value = Side.CLIENT)
	public void onRenderTooltip (ItemTooltipEvent e) {
		if (tooltipQuick.size () > 0 && tooltipEvent.size () > 0 && tooltipQuick.contains (e.getItemStack ().getItem ()))
			for (ItemStack item : tooltipEvent.keySet ())
				if (StackHelper.isSameIgnoreSize (item,e.getItemStack ()))
					for (String str : tooltipEvent.get (item))
						e.getToolTip ().add (format (str));
	}

	private String format (String unformatted) {
		String formatted = unformatted.replaceAll ("&","§");
		if (formatted.contains ("§"))
			formatted = formatted.substring (1,formatted.length ());
		return formatted;
	}
}
