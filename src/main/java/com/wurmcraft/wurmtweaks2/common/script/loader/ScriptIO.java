package com.wurmcraft.wurmtweaks2.common.script.loader;

import com.wurmcraft.wurmtweaks2.WurmTweaks2;
import com.wurmcraft.wurmtweaks2.common.reference.Global;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScriptIO {

    public static final File SAVE_DIR = new File(Global.NAME.replaceAll(" ", "_"));

    public static File[] getScripts() {
        if (SAVE_DIR.exists()) {
            return SAVE_DIR.listFiles((dir, name) -> name.endsWith(".py") || name.endsWith(".ws"));
        } else
            SAVE_DIR.mkdirs();
        return new File[0];
    }

    public String[] readScript(File file) {
        try {
            return Files.readAllLines(file.toPath()).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            WurmTweaks2.LOGGER.warn("Failed to read script '" + file.getName() + "' (" + file.getAbsolutePath() + ")");
        }
        return new String[0];
    }
}
