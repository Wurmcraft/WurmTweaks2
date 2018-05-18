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

public class WurmScript {
 public static final String DEFAULT_URL =
  ConfigHandler.masterScript.substring(0, ConfigHandler.masterScript.lastIndexOf("/"));

 public static String removeAllComments(String str) {
  //Remove single and multi-line comments
  str = str.replaceAll("//.*|/\\*[\\s\\S]*?\\*/|(\"(\\\\.|[^\"])*\")", "");
  //Remove trailing whitespace and newlines
  str = str.replaceAll("\\h*(\r?\n|\r)(?:\\h*\\1)+", "\n");
  return str;
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
   return new File(ConfigHandler.scriptDir + File.separator + name.substring(name.lastIndexOf(File.separator) + 1));
  }
  return new File(ConfigHandler.scriptDir + File.separator + name);
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
  File[] files = new File(ConfigHandler.scriptDir).listFiles();
  if (files != null) {
   for (File file : files) {
    if (file.equals(ConfigHandler.masterScript)) continue;
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
     if (ConfigHandler.deleteOld) {
      file.delete();
     } else {
      if (!file.getName().toLowerCase().contains("disabled") && !file.isDirectory()) {
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
  File scriptsLocation = new File(ConfigHandler.scriptDir);
  if (scriptsLocation.exists() || scriptsLocation.mkdirs()) {
   List<URL> scripts = new LinkedList<>();
   try {
    for (String fileName : getScriptNamesFromMaster()) {
     scripts.add(getURLFromName(fileName));
    }
   } catch (IOException e) {
    e.printStackTrace();
   }
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
   System.out.println("Failed to create / load '" + scriptsLocation.getAbsolutePath() + "' unable to download scripts");
  }
 }

 //TODO Dynamic single and multi line comments
 private static String[] getScriptNamesFromMaster() throws IOException {
  File localMaster = getFileFromName(ConfigHandler.masterScript);
  String data;
  if (localMaster.exists()) {
   data = new String(Files.readAllBytes(localMaster.toPath()));
  } else {
   try (InputStream is = new URL(ConfigHandler.masterScript).openStream()) {
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
  File masterFile = getFileFromName(ConfigHandler.masterScript);
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
   final ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
   String logName = file.getName().substring(0, file.getName().indexOf(".")) + ".log";
   final File logFile = new File(ConfigHandler.logDirectory + File.separator + logName);
   if (!logFile.exists()) {
    try {
     logFile.createNewFile();
    } catch (IOException e) {
     e.printStackTrace();
    }
   }
   try (PrintStream log = new PrintStream(new FileOutputStream(logFile, false))) {
    final String script = removeAllComments(new String(Files.readAllBytes(file.toPath())));
    //TODO might cause issues
    for (String line : script.split(System.lineSeparator())) {
     if (line.equals("")) continue;
     String originalName = line.substring(0, line.lastIndexOf("("));
     try {
      engine.eval(line.replace(originalName, originalName.toLowerCase()), functions);
     } catch (Exception e) {
      if (ConfigHandler.debug) {
       log.println(("Script error while reading line: '" + line + "' in file '" + file.getName() + "'"));
       if (e instanceof ScriptException) {
        ScriptException se = (ScriptException)e;
        log.println(formatScriptError(se.getFileName(), se.getLineNumber(), se.getMessage()));
       }
      }
     }
    }
   } catch (IOException e) {
    System.err.println("Failed to open log for script '" + file.getName() + "' at '" + logFile + "'!");
   } finally {
    Thread.currentThread().interrupt();
   }
  };
 }
}
