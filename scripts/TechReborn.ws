/*
    Name: Tech Reborn
    Curse: https://minecraft.curseforge.com/projects/techreborn
    Author: modmuss50, drcrazy, Gigabit101, Ourtenm ProfessorProspector, Yulife
*/

// Shapeless Rolling Machine
// addShapelessRolling('<output> <input>...')
addShapelessRolling('<1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod>')

// Shaped Rolling Machine
// addShapedRolling('<output> <style> <format>')
addShapedRolling('<1xminecraft:apple> RPR RPR R <1xminecraft:blaze_rod> P <plankWood>')

// ScrapBox
// addScrapbox('<stack>')
addScrapbox('<gemDiamond>')

// Generator Fluid
// addGeneratorFluid('<type> <*fluid> <energy>')
// THERMAL, GAS, DIESEL, SEMIFLUID, PLASMA
addGeneratorFluid('THERMAL <*100xwater> 800')

// Fusion Reactor
// addTechFusion('<output> <topInput> <bottomInput> <startEU> <euTick> <time>')
addTechFusion('<1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 9001 16386 120')

// Alloy Smelter
// addAlloySmelter('<output> <input> <input2> <time> <euTick>')
addAlloySmelter('<1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 400 128')

// Assembly Machine
// addAssemblingMachine('<output> <input> <input2> <time> <euTick>')
addAssemblingMachine('<1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 60 32')

// Industrial Blast Furnace
// addIndustrialBlastFurnace('<output> <output2> <input> <input2> <time> <euTick> <heat>')
addIndustrialBlastFurnace('<1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> <1xminecraft:golden_apple> 100 32 1200')

// Centerfuge
// addCenterfuge('<output> <output2> <output3> <input> <input2> <time> <euTick> <output4>')
addCenterfuge('<1xminecraft:apple> <1xminecraft:apple> <1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 120 32 <1xminecraft:apple>')

// Chemical Reactor
//addChemicalReactor('<output> <input> <time> <euTick>')
addChemicalReactor('<1xminecraft:apple> <1xminecraft:blaze_rod> 120 32')
addGrinder('<1xminecraft:apple> <1xminecraft:blaze_rod> 120 32')
addDistillationTower('<1xminecraft:apple> <1xminecraft:apple> <1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 120 32')

// Extractor
// addExtractor('<output> <input> <time> <euTick>')
addExtractor('<1xminecraft:apple> <1xminecraft:blaze_rod> 120 32')

// Grinder
// addGrinder('<output> <input> <time> <euTick>')
addGrinder('<1xminecraft:apple> <1xminecraft:blaze_rod> 120 32')

// Implosion Compressor
// addImplosionCompressor('<input> <input2> <output> <output2> <time> <euTick>')
addImplosionCompressor('<1xminecraft:apple> <1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 120 32')

// Industrial Electrolyzer
// addIndustrialElectrolyzer('<input> <input2> <output> <output2> <output3> <output4> <time> <euTick>')
addIndustrialElectrolyzer('<1xminecraft:apple> <1xminecraft:apple> <1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 120 32')

// Industrial Grinder
// addIndustrialGrinder('<input> <*input2> <output> <output2> <output3> <output4> <time> <euTick>')
addIndustrialGrinder('<1xminecraft:apple> <*100xwater> <1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 120 32')

// Industrial SawMill
// addIndustrialSawmill('<input> <*input2> <output> <output2> <output3> <time> <euTick>')
addIndustrialSawmill('<1xminecraft:apple> <*100xwater> <1xminecraft:apple> <1xminecraft:blaze_rod> <1xminecraft:blaze_rod> 120 32')

// Vacuum Freezer
// addVacuumFreezer('<output> <input> <time> <euTick')
addVacuumFreezer('<1xminecraft:apple> <1xminecraft:blaze_rod> 120 32')
