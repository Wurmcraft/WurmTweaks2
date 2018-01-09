package com.wurmcraft.wurmtweaks;

import com.wurmcraft.wurmtweaks.common.CommonProxy;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.common.command.WTCommand;
import com.wurmcraft.wurmtweaks.common.event.ScriptEvents;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ScriptDownloader;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.script.support.*;
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
		MinecraftForge.EVENT_BUS.register (new ScriptEvents ());
		WurmScript.register (new TConstruct ());
		WurmScript.register (new ImmersiveEngineering ());
		WurmScript.register (new ExtraUtils2 ());
		WurmScript.register (new DraconicEvolution ());
		WurmScript.register (new EnvironmentalTech ());
		WurmScript.register (new Mekanism ());
		WurmScript.register (new TechReborn ());
		WurmScript.register (new SonarCore ());
		WurmScript.register (new Calculator ());
		WurmScript.register (new ActuallyAdditions ());
		WurmScript.register (new IndustrialForegoing ());
		WurmScript.register (new NuclearCraft ());
		ScriptDownloader dl = new ScriptDownloader (ConfigHandler.masterScript,WurmScript.wurmScriptLocation,ConfigHandler.masterScript.replaceAll ("/master.ws",""));
	}

	@Mod.EventHandler
	public void onServerStarting (FMLServerStartingEvent e) {
		e.registerServerCommand (new WTCommand ());
	}
}
