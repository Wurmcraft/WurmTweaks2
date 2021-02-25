package com.wurmcraft.wurmtweaks2.common.script.jython.recipes;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import static com.wurmcraft.wurmtweaks2.common.script.data.RecipeUtils.recipeLock;

public class FurnaceRecipe {

    public String output;
    public String input;
    public float exp;

    public FurnaceRecipe(String output, String input, float exp) {
        this.output = output;
        this.input = input;
        this.exp = exp;
        try {
            register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FurnaceRecipe(String output, String input) {
        this.output = output;
        this.input = input;
        this.exp = 0f;
        try {
            register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register() {
        recipeLock(false);
        FurnaceRecipes.instance().addSmeltingRecipe((ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(input), (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(output), exp);
        recipeLock(true);
    }
}
