package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.core.integration.planting.IFertiliser;

import java.util.Random;

public class SonarCore extends ModSupport {

	@Override
	public String getModID () {
		return "sonarcore";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes)
			sonar.core.SonarCore.fertilisers.getObjects ().clear ();
	}

	@ScriptFunction
	public void addFertilizer (String line) {
		String[] input = verify (line,line.split (" ").length == 1,"addFertilizer('<stack>')");
		isValid (input[0]);
		sonar.core.SonarCore.fertilisers.registerObject (new CustomFertilizer (convertS (input[0])));
	}

	public class CustomFertilizer implements IFertiliser {

		private final ItemStack fertiliser;

		public CustomFertilizer (ItemStack stack) {
			this.fertiliser = stack;
		}

		@Override
		public boolean isLoadable () {
			return true;
		}

		@Override
		public String getName () {
			return fertiliser.getDisplayName ();
		}

		@Override
		public boolean canFertilise (World world,BlockPos pos,IBlockState state) {
			return state.getBlock () instanceof IGrowable;
		}

		@Override
		public boolean canGrow (World world,BlockPos pos,IBlockState state,boolean isClient) {
			return ((IGrowable) state.getBlock ()).canGrow (world,pos,state,isClient);
		}

		@Override
		public boolean canUseFertiliser (ItemStack stack,World world,Random rand,BlockPos pos,IBlockState state) {
			return stack.getItem () == fertiliser.getItem () && stack.getItemDamage () == fertiliser.getItemDamage () && ((IGrowable) state.getBlock ()).canUseBonemeal (world,rand,pos,state);
		}

		@Override
		public void grow (World world,Random rand,BlockPos pos,IBlockState state) {
			((IGrowable) state.getBlock ()).grow (world,rand,pos,state);
		}
	}
}
