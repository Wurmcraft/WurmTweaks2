/*
    Name: Mekanism
    Curse: https://minecraft.curseforge.com/projects/mekanism
    Author: aidancbrady, CyanideX
*/

// Enrichment Chamber
// addEnrichmentChamber('<output> <input>')
addEnrichmentChamber('<1xminecraft:apple> <1xminecraft:golden_apple>')

// Osmium Compressor
// addOsmiumCompressor('<output> <input>')
addOsmiumCompressor('<1xminecraft:apple> <1xminecraft:golden_apple>')

// Combiner
// addCombiner('<output> <input>')
addCombiner('<1xminecraft:apple> <1xminecraft:golden_apple>')

// Crusher
// addMCrusher('<output> <input>')
addMCrusher('<1xminecraft:apple> <1xminecraft:golden_apple>')

// Purification
// addPurification('<output> <input>')
addPurification('<1xminecraft:apple> <1xminecraft:golden_apple>')

// Metallurgic Infuser
// addMetallurgicInfuser('<output> <input>')
// CARBON, REDSTONE, BIO, DIAMOND, OBSIDIAN, FUNGI
addMetallurgicInfuser('<1xminecraft:apple> <1xminecraft:golden_apple> FUNGI 200')

// Chemical Infuser
// addChemicalInfuser('<%output> <%input> <%input2>')
addChemicalInfuser('<%1xliquidhydrogen> <%1xwater> <%1xliquidchlorine>')

// Chemical Oxidizer
addChemicalOxidizer('<%1xliquidhydrogen <1xminecraft:golden_apple>')

// Chemical Injection
// addChemicalOxidizer('<%output> <input>')
addChemicalInjection('<1xminecraft:apple> <1xminecraft:golden_apple> <%1xliquidhydrogen>')

// Electrolytic Separator
// addElectrolyticSeparator('<%leftOutput> <%rightOutput> <*input> <energy>')
addElectrolyticSeparator('<%1xliquidoxygen> <%1xliquidhydrogen> <*100xwater> 800')

// Sawmill
// addSawmill('<output> <input> | <secOutput> <secOutput%>')
addSawmill('<1xminecraft:apple> <1xminecraft:golden_apple>')
addSawmill('<1xminecraft:apple> <1xminecraft:golden_apple> <1xminecraft:apple> 25')

// Chemical Dissolution
// addChemicalDissolution('<%output> <input>')
addChemicalDissolution('<%1xwater> <1xminecraft:golden_apple>')

// Chemical Washer
// addChemicalWasher('<%output> <%input>')
addChemicalWasher('<%1xliquidoxygen> <%1xwater>')

// Chemical Crystallizer
// addChemicalCrystallizer('<output> <%input>')
addChemicalCrystallizer('<1xminecraft:apple> <%1xwater>')

// Pressure Chamber
// addMekPressureChamber('<output> <%outputGas> <%inputGas> <*inputFluid> <input> <extraEnergy> <time>')
addMekPressureChamber('<1xminecraft:apple> <%1x1xliquidoxygen> <%1xwater> <*100xwater> <1xminecraft:golden_apple> 500 240')

// Thermal Evaporation
// addThermalEvaporation('<*output> <*input>')
addThermalEvaporation('<*100xwater> <*100xlava>')

// Solar Neutron
// addSolarNeutron('<%output> <%input>')
addSolarNeutron('<%1xwater> <%1xliquidethene>')
