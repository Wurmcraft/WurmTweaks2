/*
	WurmTweaks v0.3.X
	Avaritia Example Script
	Author: brandon3055, covers1624, Morpheus1101, TheRealp445ww0rd
	Curse: https://minecraft.curseforge.com/projects/avaritia-1-10
*/

/*
	Add a Shaped Extreme Crafting Recipe

	addShapedExtreme('<output> <format> <style')
*/
addShapedExtreme('<minecraft:apple> LLLLLLLLL LXXXXXXXL LLLLLLLLL L <minecraft:log> X <minecraft:log@2>')
addShapedExtreme('<minecraft:apple> LLLLLLLLL LXXXXXXXL LLLLLLLLL L <logWood> X <minecraft:stone>')

/*
	Add a Shapeless Extreme Crafting Recipe

	addShapelessExtreme('<output> <format> <style>')
*/
addShapelessExtreme('<minecraft:apple> <minecraft:log> <minecraft:log@1> <minecraft:log@2> <minecraft:log@3> <minecraft:log@4> <minecraft:log> <minecraft:log@1> <minecraft:log@2> <minecraft:log@3> <minecraft:log@4>')
addShapelessExtreme('<minecraft:apple> <logWood> <minecraft:stone> <minecraft:stone@1> <minecraft:stone@2> <minecraft:dirt> <minecraft:grass> <logWood> <minecraft:stone> <minecraft:stone@1> <minecraft:stone@2> <minecraft:dirt> <minecraft:grass>')

/*
	Add a Compression Recipe

  Format:        ItemStack ItemStack ... (as many stacks as you want)
	addCompression('<output> <input> ... ')
*/
addCompression('<minecraft:apple> <8000xminecraft:bedrock>')
