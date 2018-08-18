package com.wurmcraft.common.script;

import static com.wurmcraft.common.ConfigHandler.*;
import static com.wurmcraft.common.ConfigHandler.checkForUpdates;
import static com.wurmcraft.common.ConfigHandler.masterScript;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import joptsimple.internal.Strings;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.*;

/**
 * Keeps the WurmScript script updated based on its URL
 *
 * Checks for WurmScript updates based on its URL along with starting a UpdateCheckerThread to check
 * for updates while the game is running
 */
public class ScriptChecker {

  private static String BASE_URL = masterScript.substring(0, masterScript.lastIndexOf("/"));

  /**
   * Loads the master file and gives a String[] of all the scripts that need to be run
   *
   * This will load the master script ether via URL or file then remove its comments and return a
   * list of the scripts that need to be run
   */
  public static String[] getLoadedScriptsFromMaster() {
    String data = "";
    try {
      data = IOUtils.toString(new URI(masterScript), Charset.defaultCharset());
    } catch (IOException | URISyntaxException e) {
      WurmTweaks.logger.warn("Failed to find / load URL '" + masterScript + "'");
      try {
        data = new String(Files.readAllBytes(new File("").toPath()));
      } catch (IOException f) {
        WurmTweaks.logger.warn("Failed to Default master file");
      }
    }
    data = data.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
    return data.split("\n");
  }

  /**
   * Downloads the scripts from the masterFile and saves them to the configuration folder
   *
   * Downloads the valid scripts from the masterFile and saves them into the configuration folder
   * for WurmScript
   */
  public static void downloadScripts(String... scriptName) {
    if (checkForUpdates) {
      for (String script : scriptName) {
        if (script.isEmpty()) {
          return;
        }
        if (script.endsWith(".ws")) {
          File saveLocation = new File(
              Loader.instance().getConfigDir().getParent() + File.separator + scriptDir);
          try {
            if (hasUpdated(script)) {
              FileUtils.copyURLToFile(new URL(BASE_URL + "/" + script), saveLocation, 10000, 1000);
              WurmTweaks.logger.trace(
                  "Downloaded '" + BASE_URL + "/" + script + " to " + saveLocation
                      .getAbsolutePath());
            } else {
              WurmTweaks.logger.trace(script + " is up to date!");
            }
          } catch (MalformedURLException e) {
            WurmTweaks.logger.info("Unable to download '" + BASE_URL + "/" + script + "'");
          } catch (IOException f) {
            WurmTweaks.logger.warn(
                "Unable to save '"
                    + script
                    + "' to "
                    + Loader.instance().getConfigDir().getAbsolutePath()
                    + File.separator
                    + Global.MODID
                    + File.separator
                    + script);
          }
        } else {
          WurmTweaks.logger.info(
              script + " is not a valid WurmScript, It will be forbidden from loading!");
        }
      }
    } else {
      WurmTweaks.logger.trace("Failed to download Scripts due to checkForUpdates set to false");
    }
  }

  /**
   * Checks the scripts URL to see if its local version has changed comparted to its web version.
   *
   * Created a MD5 hash of the web version and comapares it to its local (file) version and return
   * if they are the same, aka the local version is the same as the web version
   */
  private static boolean hasUpdated(String scriptName) {
    try {
      String urlHash = DigestUtils.md5Hex(IOUtils
          .toString(new URI(BASE_URL + "/" + scriptName), Charset.defaultCharset()));
      File saveLocation =
          new File(
              Loader.instance().getConfigDir().getAbsolutePath()
                  + File.separator
                  + Global.MODID
                  + File.separator
                  + scriptName);
      String fileHash = Strings.join(Files.readAllLines(saveLocation.toPath()), "");
      return urlHash.equals(fileHash);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}