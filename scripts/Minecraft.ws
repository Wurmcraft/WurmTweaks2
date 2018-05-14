/*
    Name: Minecraft (Java Edition)
    Curse: https://minecraft.net/
    Author: Mojang
*/

// Ore Entry
// addOreEntry('<stack> <entry>')
addOreEntry('<1xminecraft:apple> fruitApple')

// Shapeless
// addShapeless('<output> [input]...')
addShapeless('<1xminecraft:blaze_rod> <1xminecraft:apple> <1xminecraft:golden_apple>')
addShapeless('<3xminecraft:blaze_rod> <fruitApple> <1xminecraft:golden_apple>')

// Shaped
// addShaped('<output> (style)...')
addShaped('<2xminecraft:apple> R_R RAR _RR R <1xminecraft:blaze_rod> A <1xminecraft:golden_apple>')
addShaped('<4xminecraft:apple> _AR O_A ARA R <1xminecraft:blaze_rod> A <1xminecraft:golden_apple> O <fruitApple>')

// Brewing
// addBrewing('<output> <input> <bottom>')
addBrewing('<1xminecraft:apple> <1xminecraft:golden_apple> <1xminecraft:blaze_rod>')

// Smelting / Furnace
// addFurnace('<output> <input> | [float]')
addFurnace('<1xminecraft:blaze_rod> <1xminecraft:cooked_porkchop> 4')
addFurnace('<1xminecraft:golden_apple> <1xminecraft:apple>')

