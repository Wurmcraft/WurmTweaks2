/*
    Name: Thermal Expansion
    Curse: https://minecraft.curseforge.com/projects/thermalexpansion
    Author: TeamCoFH, covers1624, KingLemming, skyboy026, ZeldoKavira
*/

// Redstone Furnace
// addRedstoneFurnace('<output> <input> <energy>')
addRedstoneFurnace('<1xminecraft:apple> <1xminecraft:golden_apple> 600')

// Pulverizer
// addPulverizer('<output> <input> <energy> | <secOutput> <secOutput%>')
addPulverizer('<1xminecraft:apple> <1xminecraft:golden_apple> 600')
addPulverizer('<1xminecraft:apple> <1xminecraft:golden_apple> 600 <1xminecraft:golden_apple@1> 20')

// Sawmill
// addTESawmill('<output> <input> <energy>')
addTESawmill('<1xminecraft:apple> <1xminecraft:golden_apple> 600')

// Smelter
// addSmelter('<output> <inputA> <inputB> <energy')
addSmelter('<1xminecraft:apple> <1xminecraft:golden_apple> <1xminecraft:golden_apple@1> 600')

// Compactor
// addCompactor('<output> <input> <energy> <mode>')
// Gear, All, Coin, Plate
addCompactor('<1xminecraft:apple> <1xminecraft:golden_apple> 600 gear')

// Magma Crucible
// addMagmaCrucible('<*output> <input> <energy>')
addMagmaCrucible('<*100xwater> <1xminecraft:golden_apple> 600')

// Fluid Transposer
// addFluidTransposer('<output> <input> <fluid> <energy> <mode> | <chance>')
// Fill, Extract
addFluidTransposer('<1xminecraft:apple> <1xminecraft:golden_apple> <*100xwater> 4000 Fill')
addFluidTransposer('<1xminecraft:apple> <1xminecraft:golden_apple> <*100xwater> 4000 Extract 50')

// Centerfuge
// addCenterfuge('<output> <output2> <output3> <output4> <input> <energy> <*output5>')
addCenterfuge('<1xminecraft:apple> <1xminecraft:apple> <1xminecraft:apple> <1xminecraft:apple> <1xminecraft:golden_apple> 600 <*100xlava>')