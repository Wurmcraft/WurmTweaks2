package com.wurmcraft.script.utils;

import com.wurmcraft.script.WurmScript;
import org.apache.commons.io.FileUtils;
import org.lwjgl.Sys;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static com.wurmcraft.script.WurmScript.SCRIPT_DIR;

/**
 Helper Methods for WurmScript

 @see WurmScript */
public class ScriptHelper {

	public final ExecutorService THREAD_POOL = Executors.newFixedThreadPool (2);

	/**
	 Correctly shutdown the ExecutorService
	 */
	public void shutdown () throws InterruptedException {
		THREAD_POOL.shutdown ();
		THREAD_POOL.awaitTermination (10,TimeUnit.SECONDS);
	}

	/**
	 Downloads the URl into a file based on its name

	 @param urls URL's to download

	 @see ScriptHelper#getFileFromName(String)
	 */
	public List <Future <?>> downloadScripts (URL... urls) {
		List <Future <?>> futures = new ArrayList <> ();
		if (SCRIPT_DIR.exists () || SCRIPT_DIR.mkdirs ())
			for (URL url : urls) {
				Future <?> future = THREAD_POOL.submit (() -> {
					try {
						File saveLocation = getFileFromName (url.toString ());
						try {
							if (!saveLocation.exists () || compareFileToURL (saveLocation,url)) {
								System.out.println ("Downloading '" + url + "' to '" + saveLocation.getAbsolutePath () + "'");
								FileUtils.copyURLToFile (url,saveLocation);
							}
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace ();
						}
					} catch (IOException e) {
						System.out.println ("Failed to download '" + url + "' " + e.getLocalizedMessage ());
						e.printStackTrace ();
					}
				});
				futures.add (future);
			}
		else
			System.out.println ("Failed to create / load '" + SCRIPT_DIR.getAbsolutePath () + "' unable to download scripts");
		return futures;
	}

	/**
	 Used to keep a localFile in sync with a URL / Web file

	 @param file Local file to compare URL to
	 @param url URL that may have updated / changed

	 @return If the URL has changed requring a download of the updated file
	 */
	private boolean compareFileToURL (File file,URL url) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance ("MD5");
		InputStream steam = url.openStream ();
		BufferedInputStream bis = new BufferedInputStream (steam);
		BufferedOutputStream bos = new BufferedOutputStream (new FileOutputStream (file.getName ()));
		int i;
		while ((i = bis.read ()) != -1)
			bos.write (i);
		bos.close ();
		byte[] urlHash = md.digest ();
		md.reset ();
		md.update (FileUtils.readFileToByteArray (file));
		return Arrays.equals (urlHash,md.digest ());
	}

	/**
	 Formats a message to be used for displaying script info

	 @param fileName Name of file (used for displaying)
	 @param lineNo Line the message occurred on.
	 @param msg The Message to be displayed

	 @return The formatted message
	 */
	public String formatMessage (String fileName,int lineNo,String msg) {
		return "[" + fileName + "] <" + lineNo + "> " + msg;
	}

	/**
	 Converts the script name into a local file / script

	 @param name Name of the script file
	 */
	public File getFileFromName (String name) {
		if (name.contains ("http://") || name.contains ("https://"))
			return new File (SCRIPT_DIR + File.separator + name.substring (name.lastIndexOf ("/") + 1));
		return new File (SCRIPT_DIR + File.separator + name);
	}

	/**
	 @param name Name of the file to look for

	 @return The URL / location of the script based on its location
	 */
	public URL getURLFromName (String name) throws MalformedURLException {
		if (name.contains ("http://") || name.contains ("https://"))
			return new URL (name);
		return new URL ((WurmScript.DEFAULT_URL.endsWith ("/") ? WurmScript.DEFAULT_URL : WurmScript.DEFAULT_URL + "/") + name);
	}

	/**
	 Checks to see if all the scripts have been proccessed

	 @param futures List of the script futures

	 @return if all the scripts have been completed
	 */
	public boolean doneLoading (List <Future <?>> futures,boolean waitTillCompleted) {
		if (waitTillCompleted) {
			for (Future <?> future : futures) {
				try {
					future.get ();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace ();
				}
			}
			return true;
		} else {
			boolean allDone = true;
			for (Future <?> future : futures)
				allDone &= future.isDone ();
			return allDone;
		}
	}

	public File[] getRunnableScripts (File masterFile) {
		if (masterFile != null && masterFile.exists ()) {
			List <File> runnnableScripts = new ArrayList <> ();
			System.out.println (masterFile.getAbsolutePath ());
			for (File file : masterFile.getParentFile ().listFiles ())
				if (file.getName ().endsWith (".ws"))
					runnnableScripts.add (file);
			return runnnableScripts.toArray (new File[0]);
		}
		return new File[0];
	}
}
