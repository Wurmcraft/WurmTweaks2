package com.wurmcraft.common.network.msg;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.network.CustomMessage;
import com.wurmcraft.common.network.NetworkHandler;
import com.wurmcraft.script.FunctionsRegistry;
import com.wurmcraft.script.WurmScript;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;


import java.io.IOException;

// TODO Add to command
public class ReloadMessage extends CustomMessage.CustomClientMessage <ReloadMessage> {

    private boolean reload;

    public ReloadMessage () {
    }

    public ReloadMessage (boolean reload) {
        this.reload = reload;
    }

    @Override
    protected void read (PacketBuffer buff) throws IOException {
        reload = buff.readBoolean ();
    }

    @Override
    protected void write (PacketBuffer buff) {
        buff.writeBoolean (reload);
    }

    @Override
    public void process (EntityPlayer player,Side side) {
        Thread scriptManager = new Thread(() -> {
            Thread.currentThread().setName("WurmTweaks Reload Recipes");
            if (ConfigHandler.checkForUpdates) {
                if (WurmScript.downloadScripts()) {
                    FunctionsRegistry.loadedSupport.clear();
                    if (player != null) {
                        player.sendMessage(new TextComponentString (TextFormatting.GREEN + "Downloaded Scripts!"));
                    }
                }
            }
            NetworkHandler.sendTo (new ReloadMessage (true), (EntityPlayerMP) player);
            WurmTweaks.SCRIPT_MANAGER.run();
            FunctionsRegistry.finishSupport();
            if (player != null) {
                player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Reload complete!"));
            }
            Thread.currentThread().interrupt();
        });
        scriptManager.start();
    }
}
