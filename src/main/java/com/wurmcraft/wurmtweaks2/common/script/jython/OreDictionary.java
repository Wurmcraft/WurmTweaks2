package com.wurmcraft.wurmtweaks2.common.script.jython;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import net.minecraft.item.ItemStack;
import org.python.icu.impl.InvalidFormatException;

public class OreDictionary {

    public final String item;
    public final String entry;

    private final ItemStack stack;

    public OreDictionary(String item, String entry) throws InvalidFormatException {
        this.item = item;
        try {
            stack = (ItemStack) WurmTweaks2API.dataConverters.get("ItemStack").getData(item);
        } catch (Exception e) {
            throw new InvalidFormatException(item + " is not a valid item!");
        }
        this.entry = entry;
        register();
    }

    private void register() {
        net.minecraftforge.oredict.OreDictionary.registerOre(entry,stack);
    }
}
