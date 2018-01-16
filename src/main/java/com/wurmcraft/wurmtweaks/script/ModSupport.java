package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.script.support.*;
import net.minecraftforge.fml.common.Loader;

public class ModSupport {

	public static void init () {
		WurmScript.register (new Minecraft ());
		if (Loader.isModLoaded ("tconstruct"))
			WurmScript.register (new TConstruct ());
		if (Loader.isModLoaded ("immersiveengineering"))
			WurmScript.register (new ImmersiveEngineering ());
		if (Loader.isModLoaded ("extrautils2"))
			WurmScript.register (new ExtraUtils2 ());
		if (Loader.isModLoaded ("draconicevolution"))
			WurmScript.register (new DraconicEvolution ());
		if (Loader.isModLoaded ("environmentaltech"))
			WurmScript.register (new EnvironmentalTech ());
		if (Loader.isModLoaded ("mekanism"))
			WurmScript.register (new Mekanism ());
		if (Loader.isModLoaded ("techreborn"))
			WurmScript.register (new TechReborn ());
		if (Loader.isModLoaded ("sonarcore"))
			WurmScript.register (new SonarCore ());
		if (Loader.isModLoaded ("calculator"))
			WurmScript.register (new Calculator ());
		if (Loader.isModLoaded ("actuallyadditions"))
			WurmScript.register (new ActuallyAdditions ());
		if (Loader.isModLoaded ("industrialforegoing"))
			WurmScript.register (new IndustrialForegoing ());
		if (Loader.isModLoaded ("nuclearcraft"))
			WurmScript.register (new NuclearCraft ());
		if (Loader.isModLoaded ("betterwithmods"))
			WurmScript.register (new BetterWithMods ());
	}
}
