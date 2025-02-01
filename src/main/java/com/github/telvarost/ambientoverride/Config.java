package com.github.telvarost.ambientoverride;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "AmbientOverride")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigEntry(
                name = "Approaching Fog Color Behavior",
                description = "Linear = slow, Growth = exponential"
        )
        public FogApproachEnum fogColorApproachBehavior = FogApproachEnum.GROWTH;

        @ConfigEntry(
                name = "Approaching Fog Density Behavior",
                description = "Linear = slow, Growth = exponential"
        )
        public FogApproachEnum fogDensityApproachBehavior = FogApproachEnum.LINEAR;

        @ConfigEntry(
                name = "Allow Randomization",
                description = "Values are applied statically when false"
        )
        public Boolean allowRandomization = true;

        @ConfigEntry(
                name = "Base Fog Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float baseFogIntensity = 0.4F;

        @ConfigEntry(name = "Biome Fog Colors Enabled")
        public Boolean enableBiomeFogColors = true;

        @ConfigEntry(
                name = "Biome Fog Colors Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float biomeFogColorsMaxIntensity = 1.0F;

        @ConfigEntry(
                name = "Cave Depth Fog Colors Enabled",
                description = "Desaturates and darkens fog at low Y levels"
        )
        public Boolean enableCaveDepthFogColors = true;

        @ConfigEntry(name = "Cave Depth Fog Enabled")
        public Boolean enableCaveDepthFog = true;

        @ConfigEntry(
                name = "Cave Depth Fog Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float caveDepthFogMaxIntensity = 1.0F;

        @ConfigEntry(name = "Light Level (Darkness) Fog Enabled")
        public Boolean enableLightLevelFog = true;

        @ConfigEntry(
                name = "Light Level (Darkness) Fog Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float lightLevelFogMaxIntensity = 0.25F;

        @ConfigEntry(name = "Morning Fog Enabled")
        public Boolean enableMorningFog = true;

        @ConfigEntry(
                name = "Morning Fog Max Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float morningFogMaxIntensity = 1.0F;

        @ConfigEntry(
                name = "Morning Fog Probability",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float morningFogProbability = 0.25F;

        @ConfigEntry(
                name = "Void Fog Enabled",
                description = "Works best with growth fog behavior type"
        )
        public Boolean enableVoidFog = true;

        @ConfigEntry(
                name = "Void Fog Intensity",
                description = "Float value between 0.0 (0%) and 1.0 (100%)",
                maxLength = 1
        )
        public Float voidFogIntensity = 1.0F;
    }
}
