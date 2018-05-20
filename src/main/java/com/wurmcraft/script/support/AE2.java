package com.wurmcraft.script.support;

import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipe;
import appeng.api.features.InscriberProcessType;
import appeng.core.features.registries.inscriber.InscriberRecipe;
import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportBase;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AE2 extends SupportBase {

	private List <IInscriberRecipe> inscriber = Collections.synchronizedList (new ArrayList <> ());

	public AE2 () {
		super ("appliedenergistics");
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			for (IInscriberRecipe recipe : AEApi.instance ().registries ().inscriber ().getRecipes ())
				AEApi.instance ().registries ().inscriber ().removeRecipe (recipe);
		}
	}

	@Override
	public void finishSupport () {
		inscriber.clear ();
		for (IInscriberRecipe recipe : inscriber)
			AEApi.instance ().registries ().inscriber ().addRecipe (recipe);
	}

	@ScriptFunction
	public void addInscriber (StackHelper helper,String line) {
		String[] input = validateFormat (line,line.split (" ").length == 5,"addInscriber('<output> <input> <top> <bottom> <type>')");
		isValid (helper,input[0],input[1],input[2],input[3]);
		InscriberProcessType type = InscriberProcessType.valueOf (input[4].toUpperCase ());
		Preconditions.checkNotNull (type,"Invalid Inscriber Recipe Type!");
		Constructor[] constructor = InscriberRecipe.class.getDeclaredConstructors ();
		constructor[0].setAccessible (true);
		InscriberRecipe recipe = null;
		try {
			recipe = (InscriberRecipe) constructor[0].newInstance (new ArrayList <ItemStack> () {{
				for (ItemStack stack : convertIngredient (helper,input[1]).getMatchingStacks ())
					add (stack);
			}},convertStack (helper,input[0]),convertStack (helper,input[2]),convertStack (helper,input[3]),type);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace ();
		}
		if (recipe != null)
			inscriber.add (recipe);
		else
			ps.println ("Invalid Inscriber Recipe '" + line + "'");
	}
}
