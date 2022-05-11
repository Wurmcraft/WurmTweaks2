package com.wurmcraft.wurmtweaks2.common.script.jython;

import com.google.common.collect.Lists;
import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.api.conversion.IDataConverter;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.python.icu.impl.InvalidFormatException;


public class Item {

    public String item;
    private ItemStack stack;

    public Item(String item) throws InvalidFormatException {
        this.item = item;
        try {
            stack = (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(item);
        } catch (Exception e) {
            throw new InvalidFormatException(item + " is not a valid item!");
        }
    }

    public String getName() {
        return stack.getDisplayName();
    }

    public String toString() {
        return WurmTweaks2API.dataConverters.get("ItemStack").toString(stack);
    }

    public void removeRecipe() {
        recipeLock(false);
        ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
        ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());
        for (IRecipe r : recipes) {
            if (r.getRecipeOutput().isItemEqual(stack)) {
                recipeRegistry.remove(r.getRegistryName());
            }
        }
        recipeLock(true);
    }

    public String displayName() {
        return stack.getDisplayName();
    }

    public int stackSize() {
        return stack.getMaxStackSize();
    }

    public void stackSize(int size) {
        stack.getItem().setMaxStackSize(size);
    }

    public int count() {
        return stack.getCount();
    }

    public String count(int count) {
        stack.setCount(count);
        IDataConverter<ItemStack> converter = WurmTweaks2API.dataConverters.get("ItemStack");
        item = converter.toString(stack);
        return item;
    }

    public void noRepair() {
        stack.getItem().setNoRepair();
    }

    public void harvestLevel(String type, int level) {
        stack.getItem().setHarvestLevel(type, level);
    }

    // TODO Implement
    public void harvestSpeed(double speed) {}

    // TODO Implement
    public void harvestSpeed(String block, double speed) {}

    private void recipeLock(boolean lock) {
        ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
        if (lock) {
            recipeRegistry.freeze();
        } else {
            recipeRegistry.unfreeze();
        }
    }
}
