package com.wurmcraft.script;

import com.wurmcraft.common.ConfigHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class WurmScript {
 // Location Scripts are stored
 public static final File SCRIPT_DIR = new File("config/WurmTweaks");
 // @ConfigHandler
 public static final String DEFAULT_URL = ConfigHandler.General.masterScript.substring(0, ConfigHandler.General.masterScript.lastIndexOf("/"));

 public static String removeAllComments(String str) {
  return str.replaceAll("//.*|/\\*[\\s\\S]*?\\*/|(\"(\\\\.|[^\"])*\")", "");
 }

 private static boolean compareFileToURL(File file, URL url) throws IOException {
  if (!file.exists()) return false;
  String remoteData, localData = new String(Files.readAllBytes(file.toPath()));
  try (InputStream in = url.openStream()) {
   remoteData = new String(IOUtils.toByteArray(in));
  }
  return remoteData.equals(localData);
 }

 /**
  * Converts the script name into a local file / script
  *
  * @param name Name of the script file
  */
 public static File getFileFromName(String name) {
  if (name.contains("http://") || name.contains("https://")) {
   return new File(SCRIPT_DIR + File.separator + name.substring(name.lastIndexOf("/") + 1));
  }
  return new File(SCRIPT_DIR + File.separator + name);
 }

 /**
  * @param name Name of the file to look for
  * @return The URL / location of the script based on its location
  */
 public static URL getURLFromName(String name) throws MalformedURLException {
  if (name.contains("http://") || name.contains("https://"))
   return new URL(name);
  return new URL((DEFAULT_URL.endsWith("/") ? DEFAULT_URL : DEFAULT_URL + "/") + name);
 }

 /**
  * Formats a message to be used for displaying script info
  *
  * @param fileName Name of file (used for displaying)
  * @param lineNo   Line the message occurred on.
  * @param msg      The Message to be displayed
  * @return The formatted message
  */
 public static String formatScriptError(String fileName, int lineNo, String msg) {
  return "[" + fileName + "] <" + lineNo + "> " + msg;
 }

 private static void removeUnused() {
  File[] files = SCRIPT_DIR.listFiles();
  if (files != null) {
   for (File file : files) {
    if (file.equals(ConfigHandler.General.masterScript)) continue;
    boolean valid = false;
    try {
     for (String f : getScriptNamesFromMaster()) {
      if (getFileFromName(f).getName().equals(file.getName())) {
       valid = true;
      }
     }
    } catch (IOException e) {
     e.printStackTrace();
    }
    if (!valid) {
     if (ConfigHandler.General.deleteOld) {
      file.delete();
     } else {
      if (!file.getName().toLowerCase().contains("disabled")) {
       String newFileName =
        file.getParent() +
         File.separator +
         file.getName().substring(0, file.getName().length() - 3) +
         ".disabled";
       file.renameTo(new File(newFileName));
       System.out.println("aklsgjhasekl3ghqwigb");
      }
     }
    }
   }
  }
 }

 public static void downloadScripts() {
  if (SCRIPT_DIR.exists() || SCRIPT_DIR.mkdirs()) {
   List<URL> scripts = new LinkedList<>();
   try {
    for (String fileName : getScriptNamesFromMaster()) {
     scripts.add(getURLFromName(fileName));
    }
   } catch (IOException e) { e.printStackTrace(); }
   removeUnused();
   for (URL url : scripts) {
    try {
     File saveLocation = getFileFromName(url.toString());
     if (!compareFileToURL(saveLocation, url)) {
      System.out.println("Downloading '" + url + "' to '" + saveLocation.getAbsolutePath() + "'");
      FileUtils.copyURLToFile(url, saveLocation);
     }
    } catch (IOException e) {
     System.out.println("Failed to download '" + url + "' " + e.getLocalizedMessage());
     e.printStackTrace();
    }
   }
  } else {
   System.out.println("Failed to create / load '" + SCRIPT_DIR.getAbsolutePath() + "' unable to download scripts");
  }
 }

 //TODO Dynamic single and multi line comments
 private static String[] getScriptNamesFromMaster() throws IOException {
  File localMaster = getFileFromName(ConfigHandler.General.masterScript);
  String data;
  if (localMaster.exists()) {
   data = new String(Files.readAllBytes(localMaster.toPath()));
  } else {
   try (InputStream is = new URL(ConfigHandler.General.masterScript).openStream()) {
    data = new String(IOUtils.toByteArray(is));
   }
  }
  data = removeAllComments(data);
  List<String>
   files = new LinkedList<>(Arrays.asList(data.split("\n"))),
   toRemove = new LinkedList<>();
  files.forEach(fileName -> {
   if (!fileName.contains(".ws")) {
    toRemove.add(fileName);
   }
  });
  files.removeAll(toRemove);
  return files.toArray(new String[0]);
 }

 public static List<File> getRunnableScripts() {
  File masterFile = getFileFromName(ConfigHandler.General.masterScript);
//  if (masterFile.exists()) {
   List<File>
    runnableScripts = new ArrayList<>(Arrays.asList(masterFile.getParentFile().listFiles())),
    toRemove = new ArrayList<>();
   runnableScripts.remove(masterFile);
   for (File file : runnableScripts) {
    if (!file.getName().endsWith(".ws")) {
     toRemove.add(file);
    }
   }
   runnableScripts.removeAll(toRemove);
   return runnableScripts;
//  }
 }

 public static Runnable scriptToRunnable(File file, Bindings functions) {
  return () -> {
   try {
    ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
    //TODO Log
    System.out.println("Loading '" + file.getName() + "'");
    List<String> lines = new LinkedList<>();
    try {
     Files.readAllLines(file.toPath()).forEach(line -> {
      line = removeAllComments(line);
      if (!line.isEmpty()) {
       lines.add(line);
      }
     });
    } catch (IOException e) {
     System.err.println("Encountered error reading file: '" + file.getName() + "'!");
     e.printStackTrace();
    }
    for (String line : lines) {
     String originalName = line.substring(0, line.lastIndexOf("("));
     try {
      engine.eval(line.replace(originalName, originalName.toLowerCase()), functions);
     } catch (ScriptException e) {
      System.err.println("Script error while reading line: '" + line + "' in file '" + file.getName() + "'");
      System.out.println(formatScriptError(e.getFileName(), e.getLineNumber(), e.getMessage()));
     }
    }
   } catch(Exception e) {
    System.err.println("EXCEPTION ENCOUNTERED PROCESSING FILE: " + file.getName());
    e.printStackTrace();
   } finally {
    Thread.currentThread().interrupt();
   }
   //engine.eval (new BufferedReader (new FileReader (file)),functions);
  };
 }
}
