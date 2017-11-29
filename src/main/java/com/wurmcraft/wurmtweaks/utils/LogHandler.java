package com.wurmcraft.wurmtweaks.utils;

import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHandler {

	private static final Logger LOGGER = LogManager.getLogger (Global.NAME);
	private static final Logger SCRIPT_LOGGER = LogManager.getLogger ("WurmScript");

	private static void log (Level level,String msg) {
		LOGGER.log (level,msg);
	}

	public static void info (String msg) {
		log (Level.INFO,msg);
	}

	public static void error (String msg) {
		log (Level.ERROR,msg);
	}

	public static void debug (String msg) {
		if (ConfigHandler.debug)
			log (Level.DEBUG,msg);
	}

	public static void script (String file,int lineNo,String msg) {
		SCRIPT_LOGGER.log (Level.INFO,file + " [" + lineNo + "]: " + msg);
	}
}
