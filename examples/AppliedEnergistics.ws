/*
	WurmTweaks v0.3.X
	AE2 Example Script
	Author: AlgorithmX2, akarso, Cisien, fireball1725, thatsIch
	Curse: https://minecraft.curseforge.com/projects/applied-energistics-2
*/

/*
	Add a Grinder Recipe

	Format:		ItemStack ItemStack     ItemStack     Float   Integer
	addAEGrinder('<output> <input> <secendaryOutput> <chance> <cost>')
*/
addAEGrinder('<minecraft:apple> <minecraft:log> <minecraft:leaves@2> .25 5')
addAEGrinder('<minecraft:apple> <minecraft:log> <empty> 0 2')

/*
	Add a Inscriber Recipe

	Format:		   ItemStack ItemStack ItemStack String ItemStack
	addInscriber('<output> <top> <bottom> <type> <input>')
*/
addInscriber('<minecraft:apple> <minecraft:log> <minecraft:leaves@2> inscribe <minecraft:wool>')
addInscriber('<minecraft:apple> <minecraft:log> <minecraft:leaves@2> press <appliedenergistics2:material@12>')
