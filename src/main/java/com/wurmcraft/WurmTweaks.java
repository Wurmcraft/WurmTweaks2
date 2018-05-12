package com.wurmcraft;

import com.wurmcraft.common.CommonProxy;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.blocks.WurmTweaksBlocks;
import com.wurmcraft.common.command.WTCommand;
import com.wurmcraft.common.items.WurmTweaksItems;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.FunctionsRegistry;
import com.wurmcraft.script.WurmScript;
import com.wurmcraft.script.support.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import javax.script.Bindings;
import java.util.List;
import java.util.concurrent.Future;

@Mod (modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPENDENCIES)
public class WurmTweaks {

	@Mod.Instance (Global.MODID)
	public static WurmTweaks instance;

	@SidedProxy (serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
	public static CommonProxy proxy;

	public static WurmScript script;

	private static List <Future <?>> scriptFutures;
	private static List <Future <?>> downloadFutures;

	@Mod.EventHandler
	public void onPreInit (FMLPreInitializationEvent e) {
		WurmTweaksItems.register ();
		WurmTweaksBlocks.register ();
		proxy.preInit ();
		FunctionsRegistry.register (new Minecraft ());
		FunctionsRegistry.register (new AbyssalCraft ());
		FunctionsRegistry.register (new ActuallyAdditions ());
		FunctionsRegistry.register (new AE2 ());
		FunctionsRegistry.register (new AstralSorcery ());
		FunctionsRegistry.register (new Avaritia ());
		FunctionsRegistry.register (new BetterWithMods ());
		FunctionsRegistry.register (new BloodMagic ());
		FunctionsRegistry.register (new Botania ());
		FunctionsRegistry.register (new Calculator ());
		FunctionsRegistry.register (new CharcoalPit ());
		FunctionsRegistry.register (new DraconicEvolution ());
		FunctionsRegistry.register (new EnvironmentalTech ());
		FunctionsRegistry.register (new Events ());
		FunctionsRegistry.register (new ExtraUtils2 ());
		FunctionsRegistry.register (new GalacticCraft ());
		FunctionsRegistry.register (new ImmersiveEngineering ());
		FunctionsRegistry.register (new IndustrialForegoing ());
		// TODO
		//		FunctionsRegistry.register (new Mekanism ());
		//		FunctionsRegistry.register (new NuclearCraft ());
		//		FunctionsRegistry.register (new OreStages ());
		//		FunctionsRegistry.register (new PnumaticCraft ());
		//		FunctionsRegistry.register (new SonarCore ());
				FunctionsRegistry.register (new TConstruct ());
		//		FunctionsRegistry.register (new TechReborn ());
		//		FunctionsRegistry.register (new Thaumcraft ());
				FunctionsRegistry.register (new ThermalExpansion ());
				FunctionsRegistry.register (new ToughAsNails ());
		script = new WurmScript ();
		if (ConfigHandler.Script.checkForUpdates)
			try {
				downloadFutures = script.downloadScripts (script.getHelper ().getFileFromName (ConfigHandler.General.masterScript.substring (ConfigHandler.General.masterScript.lastIndexOf ("/"),ConfigHandler.General.masterScript.length ())),false);
			} catch (Exception e1) {
				e1.printStackTrace ();
			}
	}

	@Mod.EventHandler
	public void onInit (FMLInitializationEvent e) {
		proxy.init ();
		Bindings bindings = FunctionsRegistry.createBindings ();
		if (script.getHelper ().doneLoading (downloadFutures,true))
			scriptFutures = script.processScripts (bindings,script.getHelper ().getRunnableScripts (script.getHelper ().getFileFromName (ConfigHandler.General.masterScript.substring (ConfigHandler.General.masterScript.lastIndexOf ("/"),ConfigHandler.General.masterScript.length ()))));
	}

	@Mod.EventHandler
	public void onPostInit (FMLPostInitializationEvent e) {
		proxy.postInit ();
		FunctionsRegistry.init ();
		if (script.getHelper ().doneLoading (scriptFutures,false))
			FunctionsRegistry.finishSupport ();
	}

	@Mod.EventHandler
	public void onLoad (FMLLoadEvent e) {
		if (script.getHelper ().doneLoading (scriptFutures,true))
			FunctionsRegistry.finishSupport ();
	}

	@Mod.EventHandler
	public void onServerStarting (FMLServerStartingEvent e) {
		e.registerServerCommand (new WTCommand ());
	}

	@Mod.EventHandler
	public void onServerStopping (FMLServerStoppingEvent e) {
		try {
			script.getHelper ().shutdown ();
		} catch (InterruptedException e1) {
		}
	}
}
