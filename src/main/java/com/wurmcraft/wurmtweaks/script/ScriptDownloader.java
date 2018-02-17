package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScriptDownloader {

	public String masterScript;
	public File saveLocation;
	public List <String> slaveScripts = new ArrayList <> ();
	public WurmScript wurmScript;
	private String slaveScript;
	private ThreadHelper helper;

	public ScriptDownloader (String mainScript,File saveLocation,String slaveScript) {
		this.masterScript = mainScript;
		this.saveLocation = saveLocation;
		this.slaveScript = slaveScript;
		helper = new ThreadHelper ();
		init ();
	}

	public void init () {
		if (ConfigHandler.checkForRecipeUpdates)
			downloadFile (masterScript,"master.ws");
		if (wurmScript == null)
			wurmScript = new WurmScript ();
		if (new File (saveLocation + File.separator + "master.ws").exists ()) {
			try {
				List <String> masterScriptLines = Files.readAllLines (new File (saveLocation + File.separator + "master.ws").toPath ());
				String[] withCommentsRemoved = wurmScript.removeComments (masterScriptLines.toArray (new String[0]));
				Collections.addAll (slaveScripts,withCommentsRemoved);
				downloadSlaveScripts ();
				if (WurmScript.reload) {
					ForgeRegistry <IRecipe> recipeRegistry = (ForgeRegistry <IRecipe>) ForgeRegistries.RECIPES;
					recipeRegistry.freeze ();
					WurmScript.reload = false;
				}
			} catch (IOException e) {
				LogHandler.info ("Unable To Load 'master.ws' from disk");
			}
		} else
			LogHandler.info ("Failed to find master.ws");
	}

	public void reload () {
		wurmScript.reload ();
		init ();
	}

	private void downloadFile (String loc,String fileName) {
		try {
			URL url = new URL (loc);
			File location = new File (saveLocation + File.separator + fileName);
			if (ConfigHandler.checkForRecipeUpdates || !location.exists ())
				try {
					FileUtils.copyURLToFile (url,location);
				} catch (IOException e) {
					LogHandler.info ("Unable to " + (location.exists () ? "update" : "download / save") + ", " + loc + " I/O exception");
				}
		} catch (MalformedURLException e) {
			LogHandler.info (loc + " is not a valid URL! (Unable To Activate WurmScript)");
		}
	}

	private void downloadSlaveScripts () {
		for (String script : slaveScripts)
			downloadFile (slaveScript + "/" + script,script);
	}

	public void processScripts () {
		if (slaveScript.length () > 0)
			for (String script : slaveScripts)
				helper.scheduleScript (new File (saveLocation + File.separator + script));
	}
}
