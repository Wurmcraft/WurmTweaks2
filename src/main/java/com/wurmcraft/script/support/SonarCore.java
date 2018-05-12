package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.core.integration.planting.IFertiliser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SonarCore extends SupportHelper {

	private List<CustomFertilizer> fertilizer = Collections.synchronizedList (new ArrayList<> ());

	public SonarCore () {
		super ("sonarcore");
	}

	@Override
	public void init () {
		fertilizer.clear ();
		if (ConfigHandler.Script.removeAllMachineRecipes)
			sonar.core.SonarCore.fertilisers.getObjects ().clear ();
	}

	@Override
	public void finishSupport () {
		for (CustomFertilizer fert : fertilizer)
			sonar.core.SonarCore.fertilisers.registerObject (fert);
	}

	@ScriptFunction
	public void addFertilizer (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 1,"addFertilizer('<stack>')");
		isValid (helper,input[0]);
		fertilizer.add (new CustomFertilizer (convertStack (helper,input[0])));
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
