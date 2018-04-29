package com.wurmcraft.wurmtweaks.script.thread;

import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.LogHandler;

import java.io.File;

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
			script.process (f);
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
