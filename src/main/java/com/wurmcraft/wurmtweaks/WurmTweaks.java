package com.wurmcraft.wurmtweaks;

import com.wurmcraft.wurmtweaks.common.CommonProxy;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.common.command.WTCommand;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ScriptDownloader;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.net.MalformedURLException;
import java.net.URL;

@Mod (modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPEND, acceptedMinecraftVersions = Global.MC_VERSIONS)
public class WurmTweaks {

	@Mod.Instance (Global.MODID)
	public static WurmTweaks instance;

	@SidedProxy (serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit (FMLPreInitializationEvent e) {
		proxy.preInit ();
	}

	@Mod.EventHandler
	public void init (FMLInitializationEvent e) {
		proxy.init ();
	}

	@Mod.EventHandler
	public void postInit (FMLPostInitializationEvent e) {
		proxy.postInit ();
		ConfigHandler.handleLateConfigSettings ();
		MinecraftForge.EVENT_BUS.register (new WurmTweaks ());
		ScriptDownloader dl = new ScriptDownloader (ConfigHandler.masterScript,ConfigHandler.wurmScriptLocation,ConfigHandler.masterScript.replaceAll ("/master.ws",""));
	}

	@Mod.EventHandler
	public void onServerStarting (FMLServerStartingEvent e) {
		e.registerServerCommand (new WTCommand ());
	}
}
