package com.wurmcraft.wurmtweaks.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ThreadHelper {

	public static ArrayList <Thread> threads = new ArrayList <> ();

	public void scheduleScript (File file) {
		Thread thread = new Thread (() -> {
			WurmScript script = new WurmScript ();
			script.setCurrentScript (file);
			try {
				script.process (Files.readAllLines (file.toPath ()));
			} catch (IOException e) {
				e.printStackTrace ();
			}
		});
		threads.add (thread);
	}
}
