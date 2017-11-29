package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.utils.LogHandler;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.script.*;
import java.io.File;
import java.util.function.Function;

public class WurmScript {

	private static final ScriptEngine engine = new ScriptEngineManager ().getEngineByName ("nashorn");
	public static Bindings scriptFunctions = new SimpleBindings ();
	public static File currentScript = null;
	public static int lineNo = 0;

	public void init () {
		scriptFunctions.put ("addShapeless",new AddShapeless ());
	}

	public static void process (String line) {
		try {
			engine.eval (line,scriptFunctions);
			lineNo++;
		} catch (ScriptException e) {
			LogHandler.script (currentScript != null ? currentScript.getName () : "Code.ws",lineNo,e.getMessage ());
		}
	}

	public static void process (String[] lines) {
		for (String line : lines)
			process (line);
	}

	public class AddShapeless implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] itemStrings = s.split (" ");
			ItemStack output = StackHelper.convert (itemStrings[0],null);
			if (output != ItemStack.EMPTY) {
				NonNullList <ItemStack> recipeInput = NonNullList.create ();
				for (int index = 1; index < itemStrings.length; index++)
					if (StackHelper.convert (itemStrings[index]) != Ingredient.EMPTY)
						recipeInput.add (StackHelper.convert (itemStrings[index],null));
					else
						return null;
				//				RecipeUtils.addShapeless (output,(Object[]) recipeInput.toArray (new ItemStack[0]));
			} else if (!StackHelper.convert (itemStrings[0]).isSimple ()) {

			} else
				LogHandler.script (currentScript.getName (),lineNo,"Invalid Item '" + itemStrings[0] + "' For Shapeless Recipe Input");
			return null;
		}
	}
}
