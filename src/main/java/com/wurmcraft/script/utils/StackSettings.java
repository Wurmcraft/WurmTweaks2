package com.wurmcraft.script.utils;

/**
 For use within StackHelper, Used to convert Script String's into its designated Object
 */
public enum StackSettings {

	FRONT ("<"),BACK (">"),SPACE ("%"),
	EMPTY_STACK ("<empty>"),
	STACK_SIZE ("x"),META ("@"),NBT ("^"),
	FLUID ("*"), GAS("%"), ASPECT("&");

	private String character;

	StackSettings (String character) {
		this.character = character;
	}

	@Override
	public String toString () {
		return character;
	}
}
