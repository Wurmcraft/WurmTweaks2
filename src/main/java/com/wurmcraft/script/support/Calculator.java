package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.EnumInputType;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.minecraft.item.ItemStack;
import sonar.calculator.mod.common.recipes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator extends SupportHelper {

    private List <Object[]> seperator = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> atomic = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> calc = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> conductor = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> extraction = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> fabrication = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> flawless = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> heath = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> precision = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> scientific = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> stone = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> processing = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> restoration = Collections.synchronizedList (new ArrayList <> ());
    private List <Object[]> reassembly = Collections.synchronizedList (new ArrayList <> ());

    public Calculator () {
        super ("calculator");
    }

    @Override
    public void init () {
        seperator.clear ();
        atomic.clear ();
        calc.clear ();
        conductor.clear ();
        extraction.clear ();
        fabrication.clear ();
        flawless.clear ();
        heath.clear ();
        precision.clear ();
        scientific.clear ();
        stone.clear ();
        restoration.clear ();
        reassembly.clear ();
        if (ConfigHandler.removeAllMachineRecipes) {
            AlgorithmSeparatorRecipes.instance ().getRecipes ().clear ();
            AtomicCalculatorRecipes.instance ().getRecipes ().clear ();
            CalculatorRecipes.instance ().getRecipes ().clear ();
            ConductorMastRecipes.instance ().getRecipes ().clear ();
            ExtractionChamberRecipes.instance ().getRecipes ().clear ();
            FabricationChamberRecipes.instance ().getRecipes ().clear ();
            FlawlessCalculatorRecipes.instance ().getRecipes ().clear ();
            HealthProcessorRecipes.instance ().getRecipes ().clear ();
            PrecisionChamberRecipes.instance ().getRecipes ().clear ();
            ScientificRecipes.instance ().getRecipes ().clear ();
            StoneSeparatorRecipes.instance ().getRecipes ().clear ();
            ProcessingChamberRecipes.instance ().getRecipes ().clear ();
            RestorationChamberRecipes.instance ().getRecipes ().clear ();
            ReassemblyChamberRecipes.instance ().getRecipes ().clear ();
        }
    }

    @Override
    public void finishSupport () {
        for (Object[] r : seperator)
            AlgorithmSeparatorRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : atomic)
            AtomicCalculatorRecipes.instance ().addRecipe (r[0],r[1],r[2],r[3]);
        for (Object[] r : calc)
            CalculatorRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : conductor)
            ConductorMastRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : extraction)
            ExtractionChamberRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : fabrication)
            FabricationChamberRecipes.instance ().addRecipe (r[0],r[1]);
        for (Object[] r : flawless)
            FlawlessCalculatorRecipes.instance ().addRecipe (r[0],r[1],r[2],r[3],r[4]);
        for (Object[] r : heath)
            HealthProcessorRecipes.instance ().addRecipe (r[0],r[1]);
        for (Object[] r : precision)
            PrecisionChamberRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : scientific)
            ScientificRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : stone)
            StoneSeparatorRecipes.instance ().addRecipe (r[0],r[1],r[2]);
        for (Object[] r : processing)
            ProcessingChamberRecipes.instance ().addRecipe (r[0],r[1]);
        for (Object[] r : restoration)
            RestorationChamberRecipes.instance ().addRecipe (r[0],r[1]);
        for (Object[] r : reassembly)
            ReassemblyChamberRecipes.instance ().addRecipe (r[0],r[1]);
    }

    @ScriptFunction
    public void addAlgorithmSeparator (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addAlgorithmSeparator('<output> <output2> <input2>')");
        isValid (helper,input[0],input[1],input[2]);
        seperator.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2])});
    }

    @ScriptFunction
    public void addAtomicCalculator (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 4,"addAtomicCalculator('<output> <input>  <input2> <input3>')");
        isValid (helper,input[0],input[1],input[2]);
        atomic.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2]),convertStack (helper,input[3])});
    }

    @ScriptFunction
    public void addCalculator (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addCalculator('<output> <input>  <input2>')");
        isValid (helper,input[0],input[1],input[2]);
        calc.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2])});
    }

    @ScriptFunction
    public void addConductorMass (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addConductorMass('<output> <input> <energy>')");
        isValid (helper,input[0],input[1]);
        isValid (EnumInputType.INTEGER,helper,input[2]);
        conductor.add (new Object[] {convertStack (helper,input[1]),convertStack (helper,input[0]),convertInteger (input[2])});
    }

    @ScriptFunction
    public void addExtractionChamber (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addStoneSeperator('<output> <input>  <input2>')");
        isValid (helper,input[0],input[1],input[2]);
        extraction.add (new Object[] {convertStack (helper,input[1]),convertStack (helper,input[2]),new ExtractionChamberRecipes.ExtractionChamberOutput (convertStack (helper,input[0]))});
    }

    @ScriptFunction
    public void addFabrication (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length >= 2,"addFabrication('<output> <input>...')");
        isValid (helper,input[0]);
        List <ItemStack> inputItems = new ArrayList <> ();
        for (int index = 1; index < input.length; index++) {
            isValid (helper,input[index]);
            inputItems.add (convertStack (helper,input[index]));
        }
        fabrication.add (new Object[] {convertStack (helper,input[0]),inputItems.toArray (new ItemStack[0])});
    }

    @ScriptFunction
    public void addFlawlessCalculator (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 5,"addPrecisionChamber('<output> <input> <input2> <input3> <input4>')");
        isValid (helper,input[0],input[1],input[2],input[3],input[4]);
        flawless.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2]),convertStack (helper,input[3]),convertStack (helper,input[4])});
    }

    @ScriptFunction
    public void addHealthProccessor (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 2,"addHealthProccessor('<stack> <amount>')");
        isValid (helper,input[0]);
        isValid (EnumInputType.INTEGER,helper,input[1]);
        heath.add (new Object[] {convertStack (helper,input[0]),convertInteger (input[1])});
    }

    @ScriptFunction
    public void addPrecisionChamber (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addPrecisionChamber('<output> <input> <input2>')");
        isValid (helper,input[0],input[1],input[2]);
        precision.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2])});
    }

    @ScriptFunction
    public void addScientific (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addScientific('<output> <input> <input2>')");
        isValid (helper,input[0],input[1],input[2]);
        scientific.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2])});
    }

    @ScriptFunction
    public void addStoneSeperator (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 3,"addStoneSeperator('<output> <input> <input2>')");
        isValid (helper,input[0],input[1],input[2]);
        stone.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1]),convertStack (helper,input[2])});
    }

    @ScriptFunction
    public void addProcessingChamber (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 2,"addProcessingChamber('<output> <input>')");
        isValid (helper,input[0],input[1]);
        processing.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1])});
    }

    @ScriptFunction
    public void addRestorationChamber (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 2,"addRestorationChamber('<output> <input>')");
        isValid (helper,input[0],input[1]);
        restoration.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1])});
    }

    @ScriptFunction
    public void addReassemblyChamber (StackHelper helper,String line) {
        String[] input = validateFormat (line,line.split (" ").length == 2,"addReassemblyChamber('<output> <input>')");
        isValid (helper,input[0],input[1]);
        reassembly.add (new Object[] {convertStack (helper,input[0]),convertStack (helper,input[1])});
    }
}
