/*
	WurmTweaks v0.3.X
	BWM Example Script
	Author: Beeto, BordListian, primetoxinz
	Curse: https://minecraft.curseforge.com/projects/better-with-mods
*/

/*
	Add a Hopper Recipe

                 String  ItemStack ItemStack ItemStack ...
	addHopperFilter('<type> <output> <input> <secendaryOutput>...')
*/
addHopperFilter('betterwithmods:dustNetherrack <minecraft:apple> <minecraft:log> <minecraft:leaves@2> <minecraft:leaves@3>')
addHopperFilter('betterwithmods:dustSoul <minecraft:apple> <minecraft:log> <minecraft:leaves@2> <minecraft:leaves@3>')
addHopperFilter('betterwithmods:dustGlowstone <minecraft:apple> <minecraft:log> <minecraft:leaves@2> <minecraft:leaves@3>')
addHopperFilter('betterwithmods:wicker <minecraft:apple> <minecraft:log> <minecraft:leaves@2> <minecraft:leaves@3>')
addHopperFilter('betterwithmods:soul_sand <minecraft:apple> <minecraft:log> <minecraft:leaves@2> <minecraft:leaves@3>')

/*
	 Add a Shaped Anvil Recipe

	addShapedAnvil('<output> <style> <style>')
*/
addShapedAnvil('<minecraft:apple> XAX ABA XAX X <minecraft:bedrock> A <minecraft:dirt> B <minecraft:stone>')


/*
	 Add a Shapeless Anvil Recipe

	addShapelessAnvil('<output> <input>...')
*/
addShapelessAnvil('<minecraft:apple> <minecraft:leaves@1> <minecraft:leaves@2> <minecraft:leaves@3> <minecraft:leaves@4>')

/*
	  Add Block Heat

              Integer Block
	addBWMHeat('<heat> <block>')
*/
addBWMHeat('5 <minecraft:torch>')

/*
	  Add a Cauldron Recipe

              ItemStack ItemStack...
	addCauldron('<output> <input> ...')
*/
addCauldron('<minecraft:apple> <minecraft:log>')

/*
	  Add a Stoked Cauldron Recipe

              ItemStack ItemStack...
	addStokedCauldron('<output> <input> ...')
*/
addStokedCauldron('<minecraft:apple> <minecraft:log> <minecraft:leaves@2>')

/*
	  Add a Crucible Recipe

              ItemStack ItemStack...
	addCrucible('<output> <input> ...')
*/
addCrucible('<minecraft:apple> <minecraft:log> <minecraft:leaves@2>')

/*
	  Add a Stoked Crucible Recipe

              ItemStack ItemStack...
	addStokedCrucible('<output> <input> ...')
*/
addStokedCrucible('<minecraft:apple> <minecraft:log> <minecraft:leaves@2>')

/*
	  Add a Mill Recipe

              ItemStack ItemStack...
	addMill('<output> <input> ...')
*/
addMill('<minecraft:apple> <minecraft:log>')
