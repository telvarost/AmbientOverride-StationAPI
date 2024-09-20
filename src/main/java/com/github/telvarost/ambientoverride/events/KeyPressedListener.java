package com.github.telvarost.ambientoverride.events;

import com.github.telvarost.ambientoverride.ModHelper;
import com.github.telvarost.ambientoverride.events.init.KeyBindingListener;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.lwjgl.input.Keyboard;

public class KeyPressedListener {
    Minecraft minecraft = null;

    @EventListener
    public void keyPress(KeyStateChangedEvent event) {
        if (minecraft == null) {
            minecraft = ((Minecraft) FabricLoader.getInstance().getGameInstance());
        }

        if (Keyboard.getEventKeyState() && minecraft.currentScreen == null) {
            if (minecraft.currentScreen == null) {
                if (Keyboard.isKeyDown(KeyBindingListener.toggleTest.code)) {
                    ModHelper.ModHelperFields.IsTest = !ModHelper.ModHelperFields.IsTest;
                }
            }
        }
    }
}
