package com.wurmcraft.script.support;

import com.buuz135.industrial.api.recipe.BioReactorEntry;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import com.buuz135.industrial.api.recipe.ProteinReactorEntry;
import com.buuz135.industrial.api.recipe.SludgeEntry;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.api.Types;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.buuz135.industrial.api.IndustrialForegoingHelper.*;

public class IndustrialForegoing extends SupportHelper {

 private List<BioReactorEntry> bioReactor = Collections.synchronizedList(new ArrayList<>());
 private List<SludgeEntry> sludge = Collections.synchronizedList(new ArrayList<>());
 private List<ProteinReactorEntry> protein = Collections.synchronizedList(new ArrayList<>());
 private List<LaserDrillEntry> laser = Collections.synchronizedList(new ArrayList<>());

 public IndustrialForegoing() {
  super("industrialforegoing");
 }

 @Override
 public void init() {
  bioReactor.clear();
  sludge.clear();
  protein.clear();
  laser.clear();
  if (ConfigHandler.Script.removeAllMachineRecipes) {
   BioReactorEntry.BIO_REACTOR_ENTRIES.clear();
   SludgeEntry.SLUDGE_RECIPES.clear();
   ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES.clear();
   LaserDrillEntry.LASER_DRILL_ENTRIES.clear();
  }
 }

 @Override
 public void finishSupport() {
  for (BioReactorEntry recipe : bioReactor) addBioReactorEntry(recipe);
  for (SludgeEntry recipe : sludge) addSludgeRefinerEntry(recipe);
  for (ProteinReactorEntry recipe : protein) addProteinReactorEntry(recipe);
  for (LaserDrillEntry recipe : laser) addLaserDrillEntry(recipe);
 }

 @ScriptFunction
 public void addBioReactor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 1, "addBioReactor('<stack>')");
  isValid(helper, input[0]);
  bioReactor.add(new BioReactorEntry(convertStack(helper, input[0])));
 }

 @ScriptFunction
 public void addSludgeRefiner(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 2, "addSludgeRefiner('<stack> <weight>')");
  isValid(helper, input[0]);
  isValid(Types.INTEGER, helper, input[1]);
  sludge.add(new SludgeEntry(convertStack(helper, input[0]), convertInteger(input[1])));
 }

 @ScriptFunction
 public void addProteinReactor(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 1, "addProteinReactor('<stack>')");
  isValid(helper, input[0]);
  protein.add(new ProteinReactorEntry(convertStack(helper, input[0])));
 }

 @ScriptFunction
 public void addLaser(StackHelper helper, String line) {
  String[] input = validateFormat(line, line.split(" ").length == 3, "addLaser('<stack> <color> <weight>')");
  isValid(helper, input[1]);
  isValid(Types.INTEGER, helper, input[0], input[2]);
  laser.add(new LaserDrillEntry(convertInteger(input[0]), convertStack(helper, input[1]), convertInteger(input[2])));
 }
}