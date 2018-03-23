package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class OreStages extends ModSupport {

	@Override
	public String getModID () {
		return "orestages";
	}

	@Override
	public void init () {

	}

	@ScriptFunction
	public void addOreStage (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addOreStage('<stage> <block> <replacment>')");
		String stage = input[0];
		isValid (input[1],input[2]);
		ItemStack block = convertS (input[1]);
		ItemStack replacment = convertS (input[2]);
		OreTiersAPI.addReplacement (stage,Block.getBlockFromItem (block.getItem ()),block.getItemDamage (),Block.getBlockFromItem (replacment.getItem ()),replacment.getItemDamage (),true);
	}
}
