/*
    Name: Tinker's Construct
    Curse: https://minecraft.curseforge.com/projects/tinkers-construct
    Author: mDiyo, bino,, jadedcat, KnightMiner
*/

// Casting
// addCasting('<output> <cast> <*fluid>')
addCasting('<1xminecraft:blaze_rod> <empty> <*100xlava>')

// Basin
// addBasin('<output> <cast> <*fluid>')
addBasin('<1xminecraft:blaze_rod> <empty> <*1000xwater>')

// Alloy
// addAlloy('<*output> <*input>...')
addAlloy('<*100xiron> <*1000xwater> <*1000xlava>')

// Drying Rack
// addDrying('<output> <input> <time>')
addDrying('<1xminecraft:cooked_porkchop> <1xminecraft:blaze_rod> 240')

// Smeltery Fuel
// addFuel(<*fluid> <time>')
addFuel('<*1xiron> 400')

// Melting
// addMelting('<*output> <input> <temp>')
addMelting('<*100xiron> <1xminecraft:cooked_porkchop> 400')

// Entity Melting
// addEntityMelting('<*output> entityName')
addEntityMelting('<*100xwater> zombie')

// Creates Melting for block, ingot
// handleMelting('<*fluid> <ingot> <block>')
handleMelting('<*100xlava> <1xminecraft:blaze_rod> <1xminecraft:cooked_porkchop>')

// Create Parts and casting recipes
// handleMaterialParts('<materialName> <*fluid>')
handleMaterialParts('iron <*100xlava>')