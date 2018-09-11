/*
	WurmTweaks v0.3.X
	Actually Additions Example Script
	Author: Ellpeck, Shadows_Of_Fire
	Curse: https://minecraft.curseforge.com/projects/actually-additions
*/

/*
	Add a Crusher Recipe

	Format:		   ItemStack ItemStack   ItemStack      Integer
	addAACrusher('<output> <input> <secondaryOutput> <chance>')
*/
addAACrusher('<minecraft:apple> <minecraft:log@2> <minecraft:leaves@2> 25')

/*
	Add a Ball of Fur Drop

	Format:		   ItemStack Integer
	addBallOfFur('<item> <chance>')
*/
addBallOfFur('<minecraft:bedrock> 80')

/*
	Add Treasure Chest Loot

	Format:		       ItemStack Integer Integer
	addTreasureChest('<item> <chance> <maxAmount>')

	Note: Minimum amount of spawn is set to the amount of the item
*/
addTreasureChest('<4xminecraft:apple> 95 24')

/*
	Add a Reconstructer Recipe

	Format:		       ItemStack ItemStack Integer
	addReconstructor('<output> <input> <energy>')
*/
addReconstructor('<minecraft:apple> <minecraft:apple> 5600')


/*
	Add a Empowerer Recipe

	Format:		ItemStack ItemStack ItemStack ItemStack ItemStack ItemStack Integer Integer
	addEmpowerer('<output> <input> <slot1> <slot2> <slot3> <slot4> <energyPerStand> <time>')
*/
addEmpowerer('<minecraft:apple> <minecraft:log> <minecraft:log@1> <minecraft:log@2> <minecraft:log@3> <minecraft:log@4> 800 100')


/*
	Add a Composter Recipe

	Format:		    ItemStack       Block        ItemStack        Block
	addComposter('<output> <outputDisplayBlock> <input> <inputDisplayBlock>')
*/
addComposter('<minecraft:apple> <minecraft:wool@14> <minecraft:log> <minecraft:hay_block>')