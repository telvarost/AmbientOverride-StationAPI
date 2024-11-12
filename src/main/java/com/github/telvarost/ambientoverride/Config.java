package com.github.telvarost.ambientoverride;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "AmbientOverride")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigEntry(name = "Test", multiplayerSynced = true)
        public Float test = 2.0F;
    }
}
