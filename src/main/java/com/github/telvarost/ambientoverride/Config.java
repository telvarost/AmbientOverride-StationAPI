package com.github.telvarost.ambientoverride;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.*;

public class Config {

    @GConfig(value = "config", visibleName = "AmbientOverride")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigName("Test")
        @MultiplayerSynced
        public Float test = 2.0F;
    }
}
