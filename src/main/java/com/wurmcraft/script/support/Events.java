package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.StackSettings;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@Mod.EventBusSubscriber (modid = Global.MODID)
public class Events extends SupportHelper {

	// Tooltip
	public static List <Item> tooltipQuick = Collections.synchronizedList (new ArrayList <> ());
	public static List <Object[]> tooltips = Collections.synchronizedList (new ArrayList <> ());
	// Drop
	public static List <ItemStack> drops = Collections.synchronizedList (new ArrayList <> ());
	// Pickup Conversion
	private static Map <ItemStack, ItemStack> dropPickupEvent = Collections.synchronizedMap (new HashMap <> ());
	private static List <Item> pickupQuick = Collections.synchronizedList (new ArrayList <> ());
	public Events () {
		super ("events");
	}

	private static boolean isSameIgnoreSize (ItemStack a,ItemStack b) {
		return a.getItem ().equals (b.getItem ()) && a.getTagCompound () == b.getTagCompound () && ((a.getItemDamage () == b.getItemDamage ()) || a.getItemDamage () == Short.MAX_VALUE);
	}

	@Override
	public void finishSupport () {

	}

	@Override
	public void init () {
		MinecraftForge.EVENT_BUS.register (this);
		tooltipQuick.clear ();
		tooltips.clear ();
	}

	@ScriptFunction
	public void addTooltip (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length >= 2,"addTooltip('<output> <tooltip-lines>...')");
		isValid (helper,input[0]);
		List <String> tips = new ArrayList <> ();
		for (int index = 1; index < input.length; index++)
			tips.add (input[index].replaceAll ("&","\u00A7").replaceAll (StackSettings.SPACE.toString ()," "));
		tooltipQuick.add (convertStack (helper,input[0]).getItem ());
		tooltips.add (new Object[] {convertStack (helper,input[0]),tips.toArray (new String[0])});
	}

	@SubscribeEvent
	@SideOnly (value = Side.CLIENT)
	public void onRenderTooltip (ItemTooltipEvent e) {
		if (tooltipQuick.size () > 0 && tooltips.size () > 0 && tooltipQuick.contains (e.getItemStack ().getItem ()))
			for (Object[] data : tooltips)
				if (isSameIgnoreSize ((ItemStack) data[0],e.getItemStack ()))
					e.getToolTip ().addAll (Arrays.asList (((String[]) data[1])));
	}

	@ScriptFunction
	public void disableDrop (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 1,"disableDrop('<item>')");
		isValid (helper,input[0]);
		drops.add (convertStack (helper,input[0]));
	}

	// TODO Add a Message
	@SubscribeEvent
	public void disableDrop (ItemTossEvent e) {
		if (e.getEntityItem ().getItem () != ItemStack.EMPTY)
			for (ItemStack items : drops)
				if (isSameIgnoreSize (items,e.getEntityItem ().getItem ()))
					e.setCanceled (true);
	}

	@ScriptFunction
	public void convertPickup (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 2,"convertPickup('<from> <to>')");
		isValid (helper,input[0],input[1]);
		ItemStack theStack = convertStack (helper,input[0]);
		pickupQuick.add (theStack.getItem ());
		dropPickupEvent.put (theStack,convertStack (helper,input[1]));
	}


	@SubscribeEvent
	public void onPickupEvent (EntityItemPickupEvent e) {
		if (pickupQuick.size () > 0 && dropPickupEvent.size () > 0 && pickupQuick.contains (e.getItem ().getItem ().getItem ()))
			for (ItemStack item : dropPickupEvent.keySet ())
				if (isSameIgnoreSize (item,e.getItem ().getItem ())) {
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
}
