package com.wurmcraft.wurmtweaks.script.thread;

import com.wurmcraft.wurmtweaks.script.WurmScript;

import java.io.File;

// TODO WIP
public class WorkerThread implements Runnable {

	private Thread thread;
	private File[] file;
	private WurmScript script;

	public WorkerThread (File[] file) {
		this.file = file;
		script = new WurmScript ();
		if (WurmScript.scriptFunctions.isEmpty ())
			script.init ();
	}

	@Override
	public void run () {
		for (File f : file) {
			System.out.println (thread.getName () + ": proccssing " + f.getName ());
			script.process (f);
		}
		thread.interrupt ();
	}

	public void start () {
		if (thread == null) {
			thread = new Thread (this,file[0].getName ());
			thread.start ();
		}
	}
}
