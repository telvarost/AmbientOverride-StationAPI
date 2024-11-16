package com.github.telvarost.ambientoverride;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "AmbientOverride")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigEntry(name = "Biome Fog Colors Enabled")
        public Boolean enableBiomeFogColors = true;

        @ConfigEntry(
                name = "Biome Fog Color Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float biomeFogColorMaxIntensity = 0.5F;

        @ConfigEntry(name = "Cave Depth Fog Enabled")
        public Boolean enableCaveDepthFog = true;

        @ConfigEntry(
                name = "Cave Depth Fog Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float caveDepthFogMaxIntensity = 0.5F;

        @ConfigEntry(name = "Light Level Fog Enabled")
        public Boolean enableLightLevelFog = true;

        @ConfigEntry(
                name = "Light Level Fog Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float lightLevelFogMaxIntensity = 0.5F;

        @ConfigEntry(name = "Morning Fog Enabled")
        public Boolean enableMorningFog = true;

        @ConfigEntry(
                name = "Morning Fog Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float morningFogMaxIntensity = 0.5F;

        @ConfigEntry(
                name = "Morning Fog Probability",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float morningFogProbability = 0.25F;
    }
}
