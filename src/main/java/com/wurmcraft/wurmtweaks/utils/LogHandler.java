package com.wurmcraft.wurmtweaks.utils;

import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogHandler {

	private static final Logger LOGGER = LogManager.getLogger (Global.NAME);
	private static final Logger SCRIPT_LOGGER = LogManager.getLogger ("WurmScript");
	private static final FileOutputStream FILE_LOG;

	static {
		FileOutputStream fos = null;
		try {
			String file = Loader.instance().getConfigDir().getParent() + "/" + Global.MODID + ".log";
			System.out.println("WURMLOG: " + file);
			File logFile = new File(file);
			if (!logFile.exists()) logFile.createNewFile();
			fos = new FileOutputStream(logFile);
		} catch (IOException e) {
			System.err.println("Error opening " + Global.MODID + " log!");
		} finally {
			FILE_LOG = fos;
		}
	}

	private static void logToFile(String data) {
		if (ConfigHandler.logToFile && FILE_LOG != null)
			try {
				FILE_LOG.write(data.getBytes());
			} catch (IOException e) {
				SCRIPT_LOGGER.log(Level.ERROR, "ERROR WRITING MESSAGE TO LOG FILE! Message: '" + data + "'");
			}
	}

	private static void log (Level level, String msg) {
		LOGGER.log(level, msg);
		logToFile("[" + level.name() + "]: " + msg + "\n");
	}

	public static void info(String msg) {
		log(Level.INFO, msg);
	}

	public static void error(String msg) {
		log(Level.ERROR, msg);
	}

	public static void debug(String msg) {
		if (ConfigHandler.debug) log(Level.DEBUG, msg);
	}

	public static void script(String file, int lineNo, String msg) {
		SCRIPT_LOGGER.log(Level.INFO,file + " [" + lineNo + "]: " + msg);
		logToFile("[" + Level.INFO.name() + "]: " + file + ":" + lineNo + "\n" + msg + "\n");
	}
}
