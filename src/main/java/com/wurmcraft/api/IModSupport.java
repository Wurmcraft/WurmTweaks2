package com.wurmcraft.api;

/**
 Class used to manage a mods support
 */
public interface IModSupport {

	/**
	 Loaded only when this modid is loaded
	 */
	String modid ();

	/**
	 Called Before the scripts are loaded
	 */
	void init ();

	/**
	 Called after the scripts have been loaded
	 (Used by mutithreading)
	 */
	void finishSupport ();

}
