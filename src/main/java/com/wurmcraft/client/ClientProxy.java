package com.wurmcraft.client;

import com.wurmcraft.common.CommonProxy;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.blocks.WurmTweaksBlocks;
import com.wurmcraft.common.items.WurmTweaksItems;
import com.wurmcraft.common.reference.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ClientProxy extends CommonProxy {

	private static void createModel (Item item,int meta,String name) {
		ModelLoader.setCustomModelResourceLocation (item,meta,new ModelResourceLocation (Global.MODID + ":" + name,"inventory"));
	}

	@Override
	public void init () {
		super.init ();
	}

	@Override
	public void postInit () {
		super.postInit ();
	}

	@Override
	public IThreadListener getThreadListener (MessageContext ctx) {
		if (ctx.side.isClient ())
			return Minecraft.getMinecraft ();
		return null;
	}

	@Override
	public EntityPlayer getPlayer (MessageContext ctx) {
		if (ctx.side.isClient ())
			return Minecraft.getMinecraft ().player;
		else
			return null;
	}

	@Override
	public void preInit () {
		super.preInit ();
		MinecraftForge.EVENT_BUS.register (this);
	}

	@SubscribeEvent
	public void loadModel (ModelRegistryEvent e) {
		String[] metaItems = ConfigHandler.General.metaItems.replaceAll (" ","").split (",");
		for (int index = 0; index < metaItems.length; index++)
			createModel (WurmTweaksItems.itemMeta,index,metaItems[index]);
		createModel (Item.getItemFromBlock (WurmTweaksBlocks.transparentAluminum),0,"transparentAluminum");
		createModel (Item.getItemFromBlock (WurmTweaksBlocks.stoneMagic),0,"stoneMagic");
		createModel (Item.getItemFromBlock (WurmTweaksBlocks.logMagic),0,"logMagic");
	}
}
