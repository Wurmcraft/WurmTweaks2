package com.wurmcraft.wurmtweaks.script.thread;

import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.LogHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

// TODO WIP
public class WorkerThread implements Runnable {

	private int index;
	private Thread thread;
	private File[] file;
	private WurmScript script;

	public WorkerThread (int index,File[] file) {
		this.index = index;
		this.file = file;
		script = new WurmScript ();
		if (WurmScript.scriptFunctions.isEmpty ())
			script.init ();
	}

	@Override
	public void run () {
		long time = System.currentTimeMillis ();
		for (File f : file) {
			LogHandler.info ("Starting " + f.getName ());
			try {
				List <String> slaveScriptLines = Files.readAllLines (f.toPath ());
				if (slaveScriptLines.size () > 0) {
					String[] withCommentsRemoved = WurmScript.removeComments (slaveScriptLines.toArray (new String[0]));
					script.process (withCommentsRemoved);
				}
			} catch (IOException e) {
				LogHandler.info ("Unable to read " + script + " I/O Exception");
			}
		}
		LogHandler.debug ("Script Loading Time: " + (System.currentTimeMillis () - time));
		thread.interrupt ();
	}

	public void start () {
		if (thread == null) {
			thread = new Thread (this,"WurmScript Worker " + index);
			thread.start ();
		}
	}
}
