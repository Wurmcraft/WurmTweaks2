package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.ScrapboxList;
import techreborn.api.TechRebornAPI;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.machines.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TechReborn implements IModSupport {

	@Override
	public String getModID () {
		return "techreborn";
	}

	@Override
	public void init () {
	}


	@ScriptFunction
	public void addShapelessRolling (String line) {
		String[] input = line.split (" ");
		ItemStack output = StackHelper.convert (input[0],null);
		if (output != ItemStack.EMPTY) {
			NonNullList <ItemStack> recipeInput = NonNullList.create ();
			for (int index = 1; index < input.length; index++)
				if (StackHelper.convert (input[index],null) != ItemStack.EMPTY)
					recipeInput.add (StackHelper.convert (input[index],null));
				else
					return;
			TechRebornAPI.addShapelessOreRollingMachinceRecipe (new ResourceLocation (Global.MODID,"WurmScript"),output,recipeInput.toArray (new ItemStack[0]));
		} else
			WurmScript.info ("Invalid Stack '" + input[0] + "'");
	}

	@ScriptFunction
	public void addShapedRolling (String line) {
		String[] recipeStrings = line.split (" ");
		ItemStack output = StackHelper.convert (recipeStrings[0],null);
		if (output != ItemStack.EMPTY) {
			if (recipeStrings.length % 2 != 0) {
				WurmScript.info ("Invalid Format '" + Strings.join (recipeStrings," ") + "' try <output> <recipe style> <varA>... <ItemA>...");
				return;
			}
			List <String> recipeStyle = new ArrayList <> ();
			int recipeFormatStart = 4;
			for (int index = 1; index < 4; index++)
				if (recipeStrings[index].length () <= 3 && recipeStrings[index].length () != 1)
					recipeStyle.add (recipeStrings[index].replaceAll (WurmScript.SPACER_CHAR," "));
				else if (recipeStrings[index].length () != 1) {
					recipeFormatStart = index + 1;
					break;
				}
			HashMap <Character, ItemStack> recipeFormat = new HashMap <> ();
			for (int index = recipeFormatStart; index < recipeStrings.length; index++) {
				if (recipeStrings[index].length () == 1) {
					Character formatChar = recipeStrings[index].charAt (0);
					index++;
					ItemStack formatIngredient = StackHelper.convert (recipeStrings[index],null);
					if (!formatIngredient.equals (ItemStack.EMPTY))
						recipeFormat.put (formatChar,formatIngredient);
					else {
						WurmScript.info (recipeStrings[index] + " is not a valid Stack, try using /wt hand");
						return;
					}
				} else {
					WurmScript.info ("Invalid Varable Format '" + recipeStrings[index] + "', try using 'Var'");
					return;
				}
			}
			if (RecipeUtils.countRecipeStyle (Strings.join (recipeStyle.toArray (new String[0]),"")) != recipeFormat.keySet ().size ()) {
				WurmScript.info ("Inpossible Varable Style to Format, check to make sure you have used all the varables in the recipe style!");
				return;
			}
			List <Object> temp = new ArrayList <> ();
			for (Character ch : recipeFormat.keySet ()) {
				temp.add (ch);
				temp.add (recipeFormat.get (ch));
			}
			List <Object> finalRecipe = new ArrayList <> ();
			finalRecipe.addAll (recipeStyle);
			finalRecipe.addAll (temp);
			TechRebornAPI.addRollingOreMachinceRecipe (new ResourceLocation (Global.MODID,"WurmScript"),output,finalRecipe.toArray (new Object[0]));
		}
	}

	@ScriptFunction
	public void addScrapbox (String line) {
		String[] input = line.split (" ");
		if (input.length == 1) {
			ItemStack stack = StackHelper.convert (input[0],null);
			if (stack != ItemStack.EMPTY)
				ScrapboxList.addItemStackToList (stack);
			else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addScrapbox('<stack>')");
	}

	@ScriptFunction
	public void addThermalGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.THERMAL,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addThermalGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addDieselGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.DIESEL,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addDieselGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addGasGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.GAS,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addGasGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addPlasmaGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.PLASMA,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addPlasmaGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addSemiFluidGeneratorFluid (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			FluidStack fluid = StackHelper.convertToFluid (input[0]);
			if (fluid != null) {
				try {
					int energy = Integer.parseInt (input[1]);
					if (energy > 0) {
						GeneratorRecipeHelper.registerFluidRecipe (EFluidGenerator.SEMIFLUID,fluid.getFluid (),energy);
					} else
						WurmScript.info ("Energy must be Greater Than 0!");
				} catch (NumberFormatException e) {
					WurmScript.info ("Invalid Number '" + input[1] + "'");
				}
			} else
				WurmScript.info ("Invalid Fluid '" + input[0] + "'");
		} else
			WurmScript.info ("addSemiFluidGeneratorFluid('<*fluid> <energy>')");
	}

	@ScriptFunction
	public void addTechFusion (String line) {
		String[] input = line.split (" ");
		if (input.length == 6) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack topInput = StackHelper.convert (input[1],null);
				if (topInput != ItemStack.EMPTY) {
					ItemStack bottomInput = StackHelper.convert (input[2],null);
					if (bottomInput != ItemStack.EMPTY) {
						try {
							int startEU = Integer.parseInt (input[3]);
							try {
								int euTick = Integer.parseInt (input[4]);
								try {
									int time = Integer.parseInt (input[5]);
									FusionReactorRecipeHelper.registerRecipe (new FusionReactorRecipe (topInput,bottomInput,output,startEU,euTick,time));
								} catch (NumberFormatException g) {
									WurmScript.info ("Invalid Number '" + input[5] + "'");
								}
							} catch (NumberFormatException f) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addFusion('<output> <topInput> <bottomInput> <startEu> <euTick> <time>')");
	}

	@ScriptFunction
	public void addAlloySmelter (String line) {
		String[] input = line.split (" ");
		if (input.length == 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack input1 = StackHelper.convert (input[1],null);
				if (input1 != ItemStack.EMPTY) {
					ItemStack input2 = StackHelper.convert (input[2],null);
					if (input2 != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							try {
								int euTick = Integer.parseInt (input[4]);
								RecipeHandler.addRecipe (new AlloySmelterRecipe (input1,input2,output,time,euTick));
							} catch (NumberFormatException f) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAlloySmelter('<output> <input1> <input2> <time> <euTick>");
	}

	@ScriptFunction
	public void addAssemblingMachine (String line) {
		String[] input = line.split (" ");
		if (input.length == 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack input1 = StackHelper.convert (input[1],null);
				if (input1 != ItemStack.EMPTY) {
					ItemStack input2 = StackHelper.convert (input[2],null);
					if (input2 != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							try {
								int euTick = Integer.parseInt (input[4]);
								RecipeHandler.addRecipe (new AssemblingMachineRecipe (input1,input2,output,time,euTick));
							} catch (NumberFormatException f) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addAssemblingMachine('<output> <input1> <input2> <time> <euTick>");
	}

	@ScriptFunction
	public void addIndustrialBlastFurnace (String line) {
		String[] input = line.split (" ");
		if (input.length == 7) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack input1 = StackHelper.convert (input[2],null);
					if (input1 != ItemStack.EMPTY) {
						ItemStack input2 = StackHelper.convert (input[3],null);
						if (input2 != ItemStack.EMPTY) {
							try {
								int time = Integer.parseInt (input[4]);
								try {
									int euTick = Integer.parseInt (input[5]);
									try {
										int heat = Integer.parseInt (input[6]);
										RecipeHandler.addRecipe (new BlastFurnaceRecipe (input1,input2,output,output2,time,euTick,heat));
									} catch (NumberFormatException g) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} catch (NumberFormatException f) {
									WurmScript.info ("Invalid Number '" + input[5] + "'");
								}
							} catch (NumberFormatException e) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addBlastFurnace('<output> <output2> <input1> <input2> <time> <euTick> <requiredHeat>')");
	}

	@ScriptFunction
	public void addCenterfuge (String line) {
		String[] input = line.split (" ");
		if (input.length == 8) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack output3 = StackHelper.convert (input[2],null);
					if (output3 != ItemStack.EMPTY) {
						ItemStack output4 = StackHelper.convert (input[3],null);
						if (output4 != ItemStack.EMPTY) {
							ItemStack inputStack = StackHelper.convert (input[4],null);
							if (inputStack != ItemStack.EMPTY) {
								ItemStack inputStack2 = StackHelper.convert (input[5],null);
								if (inputStack2 != ItemStack.EMPTY) {
									try {
										int time = Integer.parseInt (input[6]);
										try {
											int euTick = Integer.parseInt (input[7]);
											RecipeHandler.addRecipe (new CentrifugeRecipe (inputStack,inputStack2,output,output2,output3,output4,time,euTick));
										} catch (NumberFormatException f) {
											WurmScript.info ("Invalid Number '" + input[7] + "'");
										}
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} else
									WurmScript.info ("Invalid Stack '" + input[5] + "'");
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addCenterfuge('<output> <output2> <output3> <output4> <input> <input2> <time> <euTick>')");
	}

	@ScriptFunction
	public void addChemicalReactor (String line) {
		String[] input = line.split (" ");
		if (input.length == 5) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					ItemStack inputStack2 = StackHelper.convert (input[2],null);
					if (inputStack2 != ItemStack.EMPTY) {
						try {
							int time = Integer.parseInt (input[3]);
							try {
								int euTick = Integer.parseInt (input[4]);
								RecipeHandler.addRecipe (new ChemicalReactorRecipe (inputStack,inputStack2,output,time,euTick));
							} catch (NumberFormatException f) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} catch (NumberFormatException e) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addChemicalReactor('<output> <input> <input2> <time> <euTick>')");
	}

	@ScriptFunction
	public void addCompressor (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						try {
							int euTick = Integer.parseInt (input[3]);
							RecipeHandler.addRecipe (new CompressorRecipe (inputStack,output,time,euTick));
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else {
			WurmScript.info ("addCompressor('<output> <input> <time> <euTick>')");
		}
	}

	@ScriptFunction
	public void addDistillationTower (String line) {
		String[] input = line.split (" ");
		if (input.length == 8) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack output3 = StackHelper.convert (input[2],null);
					if (output3 != ItemStack.EMPTY) {
						ItemStack output4 = StackHelper.convert (input[3],null);
						if (output4 != ItemStack.EMPTY) {
							ItemStack inputStack = StackHelper.convert (input[4],null);
							if (inputStack != ItemStack.EMPTY) {
								ItemStack inputStack2 = StackHelper.convert (input[5],null);
								if (inputStack2 != ItemStack.EMPTY) {
									try {
										int time = Integer.parseInt (input[6]);
										try {
											int euTick = Integer.parseInt (input[7]);
											RecipeHandler.addRecipe (new DistillationTowerRecipe (inputStack,inputStack2,output,output2,output3,output4,time,euTick));
										} catch (NumberFormatException f) {
											WurmScript.info ("Invalid Number '" + input[7] + "'");
										}
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} else
									WurmScript.info ("Invalid Stack '" + input[5] + "'");
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addDistillationTower('<output> <output2> <output3> <output4> <input> <input2> <time> <euTick>')");
	}

	@ScriptFunction
	public void addExtractor (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						try {
							int euTick = Integer.parseInt (input[3]);
							RecipeHandler.addRecipe (new ExtractorRecipe (inputStack,output,time,euTick));
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else {
			WurmScript.info ("addExtractor('<output> <input> <time> <euTick>')");
		}
	}

	// TODO Possible Cross- Mod Support
	// 'Macerator Registry'?
	@ScriptFunction
	public void addGrinder (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						try {
							int euTick = Integer.parseInt (input[3]);
							// Counter The Counter :p
							RecipeHandler.addRecipe (new GrinderRecipe (inputStack,output,time,euTick * 10));
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else {
			WurmScript.info ("addGrinder('<output> <input> <time> <euTick>')");
		}
	}

	@ScriptFunction
	public void addImplosionCompressor (String line) {
		String[] input = line.split (" ");
		if (input.length == 6) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack inputStack = StackHelper.convert (input[2],null);
					if (inputStack != ItemStack.EMPTY) {
						ItemStack inputStack2 = StackHelper.convert (input[3],null);
						if (inputStack2 != ItemStack.EMPTY) {
							try {
								int time = Integer.parseInt (input[4]);
								try {
									int euTick = Integer.parseInt (input[5]);
									RecipeHandler.addRecipe (new ImplosionCompressorRecipe (inputStack,inputStack2,output,output2,time,euTick));
								} catch (NumberFormatException f) {
									WurmScript.info ("Invalid Number '" + input[5] + "'");
								}
							} catch (NumberFormatException e) {
								WurmScript.info ("Invalid Number '" + input[4] + "'");
							}
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addImplosionCompressor('<output> <output2> <input> <input2> <time> <euTick>')");
	}

	@ScriptFunction
	public void addIndustrialElectrolyzer (String line) {
		String[] input = line.split (" ");
		if (input.length == 8) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack output3 = StackHelper.convert (input[2],null);
					if (output3 != ItemStack.EMPTY) {
						ItemStack output4 = StackHelper.convert (input[3],null);
						if (output4 != ItemStack.EMPTY) {
							ItemStack inputStack = StackHelper.convert (input[4],null);
							if (inputStack != ItemStack.EMPTY) {
								ItemStack inputStack2 = StackHelper.convert (input[5],null);
								if (inputStack2 != ItemStack.EMPTY) {
									try {
										int time = Integer.parseInt (input[6]);
										try {
											int euTick = Integer.parseInt (input[7]);
											RecipeHandler.addRecipe (new IndustrialElectrolyzerRecipe (inputStack,inputStack2,output,output2,output3,output4,time,euTick));
										} catch (NumberFormatException f) {
											WurmScript.info ("Invalid Number '" + input[7] + "'");
										}
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} else
									WurmScript.info ("Invalid Stack '" + input[5] + "'");
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addIndustrialElectrolyzer('<output> <output2> <output3> <output4> <input> <input2> <time> <euTick>')");
	}

	// TODO Possible Cross- Mod Support
	// 'Macerator Registry'?
	@ScriptFunction
	public void addIndustrialGrinder (String line) {
		String[] input = line.split (" ");
		if (input.length == 8) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack output3 = StackHelper.convert (input[2],null);
					if (output3 != ItemStack.EMPTY) {
						ItemStack output4 = StackHelper.convert (input[3],null);
						if (output4 != ItemStack.EMPTY) {
							FluidStack inputStack = StackHelper.convertToFluid (input[4]);
							if (inputStack != null) {
								ItemStack inputStack2 = StackHelper.convert (input[5],null);
								if (inputStack2 != ItemStack.EMPTY) {
									try {
										int time = Integer.parseInt (input[6]);
										try {
											int euTick = Integer.parseInt (input[7]);
											RecipeHandler.addRecipe (new IndustrialGrinderRecipe (inputStack2,inputStack,output,output2,output3,output4,time,euTick));
										} catch (NumberFormatException f) {
											WurmScript.info ("Invalid Number '" + input[7] + "'");
										}
									} catch (NumberFormatException e) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} else
									WurmScript.info ("Invalid Stack '" + input[5] + "'");
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addIndustrialGrinder('<output> <output2> <output3> <output4> <input> <input2> <time> <euTick>')");
	}

	@ScriptFunction
	public void addIndustrialSawmill (String line) {
		String[] input = line.split (" ");
		if (input.length == 8) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack output2 = StackHelper.convert (input[1],null);
				if (output2 != ItemStack.EMPTY) {
					ItemStack output3 = StackHelper.convert (input[2],null);
					if (output3 != ItemStack.EMPTY) {
						FluidStack inputStack = StackHelper.convertToFluid (input[3]);
						if (inputStack != null) {
							ItemStack inputStack2 = StackHelper.convert (input[4],null);
							if (inputStack2 != ItemStack.EMPTY) {
								try {
									int time = Integer.parseInt (input[4]);
									try {
										int euTick = Integer.parseInt (input[6]);
										RecipeHandler.addRecipe (new IndustrialSawmillRecipe (inputStack2,inputStack,output,output2,output3,time,euTick));
									} catch (NumberFormatException f) {
										WurmScript.info ("Invalid Number '" + input[6] + "'");
									}
								} catch (NumberFormatException e) {
									WurmScript.info ("Invalid Number '" + input[5] + "'");
								}
							} else
								WurmScript.info ("Invalid Stack '" + input[4] + "'");
						} else
							WurmScript.info ("Invalid Stack '" + input[3] + "'");
					} else
						WurmScript.info ("Invalid Stack '" + input[2] + "'");
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else
			WurmScript.info ("addIndustrialSawmill('<output> <output2> <output3> <output4> <*input> <input2> <time> <euTick>')");
	}

	@ScriptFunction
	public void addVacuumFreezer (String line) {
		String[] input = line.split (" ");
		if (input.length == 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack inputStack = StackHelper.convert (input[1],null);
				if (inputStack != ItemStack.EMPTY) {
					try {
						int time = Integer.parseInt (input[2]);
						try {
							int euTick = Integer.parseInt (input[3]);
							// Counter The Counter :p
							RecipeHandler.addRecipe (new VacuumFreezerRecipe (inputStack,output,time,euTick * 10));
						} catch (NumberFormatException f) {
							WurmScript.info ("Invalid Number '" + input[3] + "'");
						}
					} catch (NumberFormatException e) {
						WurmScript.info ("Invalid Number '" + input[2] + "'");
					}
				} else
					WurmScript.info ("Invalid Stack '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Stack '" + input[0] + "'");
		} else {
			WurmScript.info ("addVacuumFreezer('<output> <input> <time> <euTick>')");
		}
	}
}
