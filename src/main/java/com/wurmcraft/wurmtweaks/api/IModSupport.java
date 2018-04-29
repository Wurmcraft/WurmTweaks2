package com.wurmcraft.wurmtweaks.api;

public interface IModSupport {

	/**
	 Checks for this ModID Before Loading The Controller
	 */
	String getModID ();

	/**
	 Called Once The Support Has Been Loaded
	 */
	void init ();

}
