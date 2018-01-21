package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// TODO Add Material Support
public class TConstruct implements IModSupport {

	@Override
	public String getModID () {
		return "tconstruct";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			try {
				Field tableCasting = TinkerRegistry.class.getDeclaredField ("tableCastRegistry");
				tableCasting.setAccessible (true);
				((List <ICastingRecipe>) tableCasting.get (TinkerRegistry.getAllTableCastingRecipes ())).clear ();
				Field melting = TinkerRegistry.class.getDeclaredField ("meltingRegistry");
				melting.setAccessible (true);
				((List <MeltingRecipe>) melting.get (TinkerRegistry.getAllMeltingRecipies ())).clear ();
				Field basinCast = TinkerRegistry.class.getDeclaredField ("basinCastRegistry");
				basinCast.setAccessible (true);
				((List <ICastingRecipe>) basinCast.get (TinkerRegistry.getAllBasinCastingRecipes ())).clear ();
				Field alloy = TinkerRegistry.class.getDeclaredField ("alloyRegistry");
				alloy.setAccessible (true);
				((List <AlloyRecipe>) alloy.get (TinkerRegistry.getAlloys ())).clear ();
				Field drying = TinkerRegistry.class.getDeclaredField ("dryingRegistry");
				drying.setAccessible (true);
				((List <DryingRecipe>) drying.get (TinkerRegistry.getAllDryingRecipes ())).clear ();
			} catch (IllegalAccessException | NoSuchFieldException e) {
				e.printStackTrace ();
			}
		}
	}

	@ScriptFunction
	public void addCasting (String line) {
		String[] input = line.split (" ");
		if (input.length > 0 && StackHelper.convert (input[0],null) != ItemStack.EMPTY) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (input.length == 3) {
				if (StackHelper.convert (input[1],null) != null) {
					ItemStack cast = StackHelper.convert (input[1],null);
					FluidStack fluidStack = StackHelper.convertToFluid (input[2]);
					if (fluidStack != null)
						TinkerRegistry.registerTableCasting (output,cast,fluidStack.getFluid (),fluidStack.amount);
					else
						WurmScript.info ("Invalid FluidStack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[0] + "'");
			} else
				WurmScript.info ("addCasting('<output> <cast> <*fluid>");
		} else
			WurmScript.info ("Invalid Stack '" + input[0] + "'");
	}

	@ScriptFunction
	public void addBasin (String line) {
		String[] input = line.split (" ");
		if (input.length > 0 && StackHelper.convert (input[0],null) != ItemStack.EMPTY) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (input.length == 3) {
				if (StackHelper.convert (input[1],null) != null) {
					ItemStack cast = StackHelper.convert (input[1],null);
					FluidStack fluidStack = StackHelper.convertToFluid (input[2]);
					if (fluidStack != null)
						TinkerRegistry.registerBasinCasting (output,cast,fluidStack.getFluid (),fluidStack.amount);
					else
						WurmScript.info ("Invalid FluidStack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[0] + "'");
			} else
				WurmScript.info ("addBasin('<output> <cast> <*fluid>");
		} else
			WurmScript.info ("Invalid Stack '" + input[0] + "'");
	}

	@ScriptFunction
	public void addAlloy (String line) {
		String[] input = line.split (" ");
		if (input.length > 1) {
			FluidStack outputStack = StackHelper.convertToFluid (input[0]);
			if (outputStack != null) {
				List <FluidStack> inputFluids = new ArrayList <> ();
				for (int index = 1; index < input.length; index++)
					if (StackHelper.convertToFluid (input[index]) != null)
						inputFluids.add (StackHelper.convertToFluid (input[index]));
					else {
						WurmScript.info ("Invalid FluidStack '" + input[0] + "'");
						return;
					}
				TinkerRegistry.registerAlloy (outputStack,inputFluids.toArray (new FluidStack[0]));
			} else
				WurmScript.info ("Invalid FluidStack '" + input[0] + "'");
		} else
			WurmScript.info ("addAlloy('<*output> <*input>...');");
	}

	@ScriptFunction
	public void addDrying (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					int time = Integer.parseInt (input[2]);
					if (time > 0)
						TinkerRegistry.registerDryingRecipe (inputStack,output,time);
					else
						WurmScript.info (time + " must be greater then 0");
				} else
					WurmScript.info ("Invalid Stack '" + input[0] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addDrying('<output> <input> <time>');");
	}

	@ScriptFunction
	public void addFuel (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				int time = Integer.parseInt (input[1]);
				if (time > 0) {
					TinkerRegistry.registerSmelteryFuel (fluid,time);
				} else
					WurmScript.info (time + " must be greater then 0");
			} else
				WurmScript.info ("Invalid FluidStack '" + input[0] + "'");
		} else
			WurmScript.info ("addFuel(<*fluid> <time>");
	}

	@ScriptFunction
	public void addMelting (String line) {
		String[] input = line.split (" ");
		if (input.length == 3) {
			ItemStack meltingStack = StackHelper.convert (input[0],null);
			if (meltingStack != ItemStack.EMPTY) {
				FluidStack fluid = StackHelper.convertToFluid (input[1]);
				if (fluid != null) {
					int temp = Integer.parseInt (input[2]);
					if (temp > 0)
						TinkerRegistry.registerMelting (new MeltingRecipe (RecipeMatch.of (meltingStack,fluid.amount),fluid,temp));
					else
						WurmScript.info (temp + " must be greater then 0");
				} else
					WurmScript.info ("Invalid FluidStack '" + input[0] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addMelting('<stack> <*fluid> <temp>');");
	}

	@ScriptFunction
	public void addEntityMelting (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			EntityEntry entity = null;
			for (EntityEntry ent : ForgeRegistries.ENTITIES.getValues ()) {
				if (ent.getName ().equalsIgnoreCase (input[0]))
					entity = ent;
			}
			if (entity != null) {
				FluidStack fluid = StackHelper.convertToFluid (input[1]);
				if (fluid != null) {
					TinkerRegistry.registerEntityMelting (entity.getEntityClass (),fluid);
				} else
					WurmScript.info ("Invalid FluidStack '" + input[0] + "'");
			} else
				WurmScript.info ("Invalid Entity '" + input[0] + "'");
		} else
			WurmScript.info ("addEntityMelting('entityName <*fluid>')");
	}
}
