package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks.script.support.*;
import net.minecraftforge.fml.common.Loader;

public class ModRegistry {

	public static void init () {
		WurmTweaks2API.register (new Minecraft ());
		WurmTweaks2API.register (new Events ());
		if (Loader.isModLoaded ("tconstruct"))
			WurmTweaks2API.register (new TConstruct ());
		if (Loader.isModLoaded ("immersiveengineering"))
			WurmTweaks2API.register (new ImmersiveEngineering ());
		if (Loader.isModLoaded ("extrautils2"))
			WurmTweaks2API.register (new ExtraUtils2 ());
		if (Loader.isModLoaded ("draconicevolution"))
			WurmTweaks2API.register (new DraconicEvolution ());
		if (Loader.isModLoaded ("environmentaltech"))
			WurmTweaks2API.register (new EnvironmentalTech ());
		if (Loader.isModLoaded ("mekanism"))
			WurmTweaks2API.register (new Mekanism ());
		if (Loader.isModLoaded ("techreborn"))
			WurmTweaks2API.register (new TechReborn ());
		if (Loader.isModLoaded ("sonarcore"))
			WurmTweaks2API.register (new SonarCore ());
		if (Loader.isModLoaded ("calculator"))
			WurmTweaks2API.register (new Calculator ());
		if (Loader.isModLoaded ("actuallyadditions"))
			WurmTweaks2API.register (new ActuallyAdditions ());
		if (Loader.isModLoaded ("industrialforegoing"))
			WurmTweaks2API.register (new IndustrialForegoing ());
		if (Loader.isModLoaded ("nuclearcraft"))
			WurmTweaks2API.register (new NuclearCraft ());
		if (Loader.isModLoaded ("betterwithmods"))
			WurmTweaks2API.register (new BetterWithMods ());
		if (Loader.isModLoaded ("abyssalcraft"))
			WurmTweaks2API.register (new AbyssalCraft ());
		if (Loader.isModLoaded ("avaritia"))
			WurmTweaks2API.register (new Avaritia ());
		if (Loader.isModLoaded ("botania"))
			WurmTweaks2API.register (new Botania ());
		if (Loader.isModLoaded ("bloodmagic"))
			WurmTweaks2API.register (new BloodMagic ());
		if (Loader.isModLoaded ("thermalexpansion"))
			WurmTweaks2API.register (new ThermalExpansion ());
		if (Loader.isModLoaded ("galacticraftcore"))
			WurmTweaks2API.register (new GalacticCraft ());
		if (Loader.isModLoaded ("pneumaticcraft"))
			WurmTweaks2API.register (new PnumaticCraft ());
		if (Loader.isModLoaded ("toughasnails"))
			WurmTweaks2API.register (new ToughAsNails ());
		if (Loader.isModLoaded ("orestages"))
			WurmTweaks2API.register (new OreStages ());
		if (Loader.isModLoaded ("charcoal_pit"))
			WurmTweaks2API.register (new CharcoalPit ());
		if (Loader.isModLoaded ("thaumcraft"))
			WurmTweaks2API.register (new Thaumcraft ());
	}
}
