package com.wurmcraft.wurmtweaks;

import com.wurmcraft.wurmtweaks.common.CommonProxy;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.common.block.WTBlocks;
import com.wurmcraft.wurmtweaks.common.command.WTCommand;
import com.wurmcraft.wurmtweaks.common.event.ScriptEvents;
import com.wurmcraft.wurmtweaks.common.items.WTItems;
import com.wurmcraft.wurmtweaks.common.network.NetworkHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ModRegistry;
import com.wurmcraft.wurmtweaks.script.ScriptDownloader;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod (modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPEND, acceptedMinecraftVersions = Global.MC_VERSIONS)
public class WurmTweaks {

	@Mod.Instance (Global.MODID)
	public static WurmTweaks instance;

	@SidedProxy (serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
	public static CommonProxy proxy;
	public static ScriptDownloader dl;

	@Mod.EventHandler
	public void preInit (FMLPreInitializationEvent e) {
		WTItems.register ();
		WTBlocks.register ();
		proxy.preInit ();
	}

	@Mod.EventHandler
	public void init (FMLInitializationEvent e) {
		proxy.init ();
		NetworkHandler.registerPackets ();
		MinecraftForge.EVENT_BUS.register (new ScriptEvents ());
	}

	@Mod.EventHandler
	public void postInit (FMLPostInitializationEvent e) {
		proxy.postInit ();
		ConfigHandler.handleLateConfigSettings ();
		ModRegistry.init ();
		dl = new ScriptDownloader (ConfigHandler.masterScript,WurmScript.wurmScriptLocation,ConfigHandler.masterScript.replaceAll ("/master.ws",""));
	}

	@Mod.EventHandler
	public void onServerStarting (FMLServerStartingEvent e) {
		e.registerServerCommand (new WTCommand ());
		for (Gas stack : GasRegistry.getRegisteredGasses ())
			LogHandler.info ("Gas: " + stack.getUnlocalizedName ());
	}

}
