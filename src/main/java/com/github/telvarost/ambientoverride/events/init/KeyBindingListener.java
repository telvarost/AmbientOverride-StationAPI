package com.github.telvarost.ambientoverride.events.init;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class KeyBindingListener {
    public static KeyBinding toggleTest;

    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        event.keyBindings.add(toggleTest = new KeyBinding("Toggle Test", Keyboard.KEY_K));
    }
}
