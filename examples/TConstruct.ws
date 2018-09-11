
/*
	WurmTweaks v0.3.X
	TiCon Example Script
	Author:  Owner mDiyo, KnightMiner , bonusboni, Mascot Jadedcat
	Curse: https://www.curseforge.com/minecraft/mc-mods/tinkers-construct
*/

/*
	Add a Casting Recipe

	Format:		"ItemStack ItemStack FluidStack")
	addCasting('<output> <input> <*cost>')

*/

addCasting('<minecraft:apple> <minecraft:log> <*100xwater>')

/*
	Add a Basin Recipe

	Format:		   "ItemStack ItemStack FluidStack"
	addBasin('<output> <input> <*cost>')


*/

addBasin('<minecraft:apple> <minecraft:log> <*100xwater>')
/*
        Add a Alloy recipe

        Format:             "Fluidstack, FluidStack, FluidStack ..."
        addBasin('<Output> <Cast> <*input>')


*/

addAlloy('<*100xlava> <*50xlava> <*50xiron>')
/*
        Add a Drying Recipe

        Format:              "ItemStack, ItemStack, Integer"
        addAlloy('<> <> <> ')


*/
addDrying('<minecraft:log> <minecraft:apple> 1200')

/*
        Add a Fuel Recipe 

	Format:		      "FluidStack, Integer"
        addFuel('<> <>')


*/
addFuel('<*100xlava> 1200')

/*
        Add a Melting Recipe

	Format:			"FluidStack, ItemStack, Integer"
	addMelting('<> <>')


*/
addMelting('<*100xlava> <minecraft:apple> 1200')

/*
	Add an Entity Melting Recipe

	Format:			"FluidStack, Entity"
	addentityMelting('<> <>')


*/
addEntityMelting('<*100xLava> <ZOMBIE>')

/*
	Add Handle Melting Recipe

	Format:			"FLuidStack, ItemStack, Block"
	handleMelting('<> <> <>')

*/
handleMelting('<*100xlava> <minecraft:apple> <minecraft:dirt>')

/*
	Add a Material Parts Recipe

	Format:			"String, FluidStack"
	addMaterialParts('<> <>')

*/
handleMaterialParts('copper <*100xLava>')
handleMaterialParts('iron <*100xgold>')
