package com.wurmcraft.wurmtweaks.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.Cast;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.tinkering.MaterialItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// TODO Add Material Support
public class TConstruct extends ModSupport {

	private static int INGOT = 1000;
	private static int BLOCK = INGOT * 32;

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
		String[] input = verify (line,line.split (" ").length == 3,"addCasting('<output> <cast> <*fluid>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.FLUID,input[2]);
		FluidStack fluidStack = convertF (input[2]);
		TinkerRegistry.registerTableCasting (convertS (input[0]),convertS (input[1]),fluidStack.getFluid (),fluidStack.amount);
	}

	@ScriptFunction
	public void addBasin (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addBasin('<output> <cast> <*fluid>')");
		isValid (input[0]);
		isValid (EnumInputType.FLUID,input[2]);
		FluidStack fluidStack = convertF (input[2]);
		TinkerRegistry.registerBasinCasting (convertS (input[0]),convertS (input[1]),fluidStack.getFluid (),fluidStack.amount);
	}

	@ScriptFunction
	public void addAlloy (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addAlloy('<*output> <*input>...')");
		isValid (EnumInputType.FLUID,input[0]);
		List <FluidStack> inputFluids = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (EnumInputType.FLUID,input[index]);
			inputFluids.add (convertF (input[index]));
		}
		TinkerRegistry.registerAlloy (convertF (input[0]),inputFluids.toArray (new FluidStack[0]));
	}

	@ScriptFunction
	public void addDrying (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addDrying('<output> <input> <time>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		TinkerRegistry.registerDryingRecipe (convertS (input[0]),convertS (input[1]),convertNI (input[2]));
	}

	@ScriptFunction
	public void addFuel (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addFuel(<*fluid> <time>')");
		isValid (EnumInputType.FLUID,input[0]);
		isValid (EnumInputType.INTEGER,input[1]);
		TinkerRegistry.registerSmelteryFuel (convertF (input[0]),convertNI (input[1]));
	}

	@ScriptFunction
	public void addMelting (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addMelting('<*output> <input> <temp>')");
		isValid (EnumInputType.FLUID,input[0]);
		isValid (input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		FluidStack fluidStack = convertF (input[0]);
		TinkerRegistry.registerMelting (new MeltingRecipe (RecipeMatch.of (convertS (input[1]),fluidStack.amount),fluidStack,convertNI (input[2])));
	}

	@ScriptFunction
	public void addEntityMelting (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addEntityMelting('<*output> entityName')");
		EntityEntry entity = null;
		for (EntityEntry ent : ForgeRegistries.ENTITIES.getValues ())
			if (ent.getName ().equalsIgnoreCase (input[0]))
				entity = ent;
		Preconditions.checkNotNull (entity);
		TinkerRegistry.registerEntityMelting (entity.getEntityClass (),convertF (input[0]));
	}
	// End Of Normal Mod Support

	@ScriptFunction
	public void handleMelting (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"handleMelting('<*fluid> <ingot> <block>')");
		isValid (EnumInputType.FLUID,input[0]);
		isValid (input[1],input[2]);
		Fluid fluid = convertF (input[0]).getFluid ();
		if (convertS (input[2]) != ItemStack.EMPTY) {
			TinkerRegistry.registerMelting (convertS (input[2]),fluid,BLOCK);
			TinkerRegistry.registerBasinCasting (convertS (input[2]),null,fluid,BLOCK);
		}
		if (convertS (input[1]) != ItemStack.EMPTY) {
			TinkerRegistry.registerMelting (convertS (input[1]),fluid,INGOT);
			TinkerRegistry.registerTableCasting (convertS (input[1]),convertS ("<1xtconstruct:cast_custom@0>"),fluid,INGOT);
		}
	}

	@ScriptFunction
	public void handleMaterialParts (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"handleMaterialParts('<materialName> <*fluid>')");
		isValid (EnumInputType.FLUID,input[1]);
		Fluid fluid = convertF (input[1]).getFluid ();
		Material material = null;
		for (Material mat : TinkerMaterials.materials)
			if (mat.getIdentifier ().equalsIgnoreCase (input[0]))
				material = mat;
		for (IToolPart toolPart : TinkerRegistry.getToolParts ())
			if (toolPart instanceof MaterialItem && toolPart.canBeCasted ()) {
				ItemStack stack = toolPart.getItemstackWithMaterial (material);
				TinkerRegistry.registerMelting (stack,fluid,normalize (toolPart.getCost ()));
				ItemStack cast = new ItemStack (TinkerSmeltery.cast);
				Cast.setTagForPart (cast,stack.getItem ());
				TinkerRegistry.registerTableCasting (stack,cast,fluid,(normalize (toolPart.getCost ())));
			}
	}

	private int normalize (int amount) {
		if (amount == Material.VALUE_Ingot)
			return INGOT;
		else if (amount == Material.VALUE_Block)
			return BLOCK;
		else if (amount == Material.VALUE_Nugget)
			return INGOT / 9;
		else if (amount == Material.VALUE_Shard)
			return INGOT / 2;
		return INGOT * ((amount / Material.VALUE_Ingot));
	}
}
