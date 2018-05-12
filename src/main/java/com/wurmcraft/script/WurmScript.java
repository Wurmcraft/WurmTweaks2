package com.wurmcraft.script;

import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.ScriptHelper;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

public class WurmScript {

	// Location Scripts are stored
	public static final File SCRIPT_DIR = new File ("config/WurmTweaks");
	// @ConfigHandler
	public static final String DEFAULT_URL = ConfigHandler.General.masterScript.substring (0,ConfigHandler.General.masterScript.lastIndexOf ("/"));

	// Nashorn Engine
	private static final ScriptEngine engine = new ScriptEngineManager (null).getEngineByName ("nashorn");

	// Helper Instances
	private ScriptHelper helper = new ScriptHelper ();


	/**
	 Loads all the Scripts

	 @param functions Methods / Functions that are to be used for the scripts
	 @param files The Scripts

	 @return List of Futures to be added to correct registry when they are completed
	 */
	public List <Future <?>> processScripts (Bindings functions,File... files) {
		List <Future <?>> threadOutput = new ArrayList <> ();
		for (File file : files)
			threadOutput.add (runScript (file,functions));
		return threadOutput;
	}

	/**
	 Loads a single script

	 @param file Script / File to load into the script system
	 @param functions Method / Functions that are to be used with this script

	 @return Future to be added to correct registry when they are completed
	 */
	private Future <?> runScript (File file,Bindings functions) {
		return helper.THREAD_POOL.submit (() -> {
			try {
				System.out.println ("Loading '" + file.getName () + "'");
				try {
					for (String line : Files.readAllLines (file.toPath ()))
						engine.eval (line,functions);
				} catch (IOException e) {
					e.printStackTrace ();
				}
				//				engine.eval (new BufferedReader (new FileReader (file)),functions);
			} catch (ScriptException e) {
				e.printStackTrace ();
				System.out.println (helper.formatMessage (e.getFileName (),e.getLineNumber (),e.getMessage ()));
			}
		});
	}

	public ScriptHelper getHelper () {
		return helper;
	}

	/**
	 Find all the scripts based on the masterFile

	 @param masterFile File to look for all the scripts

	 @return List of all the scripts

	 @throws IOException Unable to load / find the masterFile
	 */
	private String[] getScriptLocations (File masterFile) throws IOException {
		List <String> withComments = Files.readAllLines (masterFile.toPath ());
		List <String> without = new ArrayList <> ();
		for (int x = 0; x < withComments.size (); x++) {
			if (withComments.get (x).startsWith ("/*") || withComments.get (x).replaceAll (" ","").startsWith ("/*"))
				for (int y = (x + 1); y < withComments.size (); y++)
					if (withComments.get (y).startsWith ("*/") || withComments.get (y).replaceAll (" ","").startsWith ("*/")) {
						x = y + 1;
						break;
					}
			without.add (withComments.get (x));
		}
		return without.toArray (new String[0]);
	}

	/**
	 @param masterFile masterFile to look for scripts in

	 @see WurmScript#getScriptLocations(File)
	 @see ScriptHelper#downloadScripts(URL...)
	 */
	public List <Future <?>> downloadScripts (File masterFile,boolean deleteInvalid) throws IOException {
		if (!masterFile.exists ()) {
			List <Future <?>> futures = helper.downloadScripts (new URL (ConfigHandler.General.masterScript));
			helper.doneLoading (futures, true);
		}
		String[] fileLocations = getScriptLocations (masterFile);
		removeUnused (masterFile,fileLocations,deleteInvalid);
		List <URL> downloadScripts = new ArrayList <> ();
		for (String f : fileLocations)
			downloadScripts.add (helper.getURLFromName (f));
		return helper.downloadScripts (downloadScripts.toArray (new URL[0]));
	}

	/**
	 Clean out the script folder of unknown or invalid scripts / files

	 @param files List of all the valid script file
	 */
	private void removeUnused (File masterFile,String[] files,boolean delete) {
		helper.THREAD_POOL.submit (() -> {
			for (File file : Objects.requireNonNull (SCRIPT_DIR.listFiles ())) {
				if (file.equals (masterFile))
					continue;
				boolean valid = false;
				for (String f : files)
					if (helper.getFileFromName (f).getName ().equals (file.getName ()))
						valid = true;
				if (!valid)
					if (delete)
						file.delete ();
					else
						file.renameTo (new File (file.getParent () + File.separator + file.getName ().substring (0,file.getName ().length () - 3) + ".disabled"));
			}
		});
	}
}
