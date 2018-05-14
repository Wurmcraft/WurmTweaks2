/*
    Name: Immersive Engineering
    Curse: https://minecraft.curseforge.com/projects/immersive-engineering
    Author: BlueSunrise, malte0811, Mr_Hazard
*/

// Alloy Furnace
// addAlloyRecipe('<output> <input> <input2> <time>')
addAlloyRecipe('<1xminecraft:apple> <1xminecraft:golden_apple> <1xminecraft:blaze_rod> 400')

// Arc Furnace
// addArcFurnace('<output> <slag> <input> <time> <energy> | <additives>...')
addArcFurnace('<1xminecraft:apple> <empty> <1xminecraft:blaze_rod> 120 500')
addArcFurnace('<1xminecraft:golden_apple> <1xminecraft:apple> <1xminecraft:blaze_rod> 120 500 <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> <1xminecraft:apple>')

// Blast Furnace
// addBlastFurnace('<output> <slag> <input> <time>')
addBlastFurnace('<1xminecraft:apple> <empty> <1xminecraft:golden_apple> 600')

// Blast Fuel
// addBlastFuel('<stack> <time>')
addBlastFuel('<1xminecraft:apple> 100')

// Coke Oven
// addCokeOven('<output> <input> <creosote> <time>')
addCokeOven('<1xminecraft:apple> <1xminecraft:golden_apple> 20 400')

// Crusher
// addCrusher('<output> <input> <energy> | <secOutput> <secOutput%>')
addCrusher('<1xminecraft:apple> <1xminecraft:golden_apple> 4000')
addCrusher('<1xminecraft:apple> <1xminecraft:golden_apple> 4000 <1xminecraft:blaze_rod> 25')

// Fermenter
// addFermenter('<output> <*output> <input> <energy>')
addFermenter('<1xminecraft:apple> <*100xwater> <1xminecraft:golden_apple> 5000')

// Metal Press
// addMetalPress('<output> <input> <mold> <time>')
addMetalPress('<1xminecraft:apple> <1xminecraft:golden_apple> <1xminecraft:blaze_rod> 240')

// Refinery
// addRefinery('<*output> <*input> <*input> <energy>')
addRefinery('<*1xoil> <*1xwater> <*1xlava> 500')

// Squeezer
// addSqueezer('<output> <*output> <input> <energy>')
addSqueezer('<1xminecraft:apple> <*100xlava> <1xminecraft:golden_apple> 8000')