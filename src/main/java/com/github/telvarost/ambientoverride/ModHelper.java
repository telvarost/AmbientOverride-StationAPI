package com.github.telvarost.ambientoverride;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Namespace;

public class ModHelper {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    public static class Fields {
        /** - Initialize daily fog values */
        public static float biomeFogColorStrength = 1.0F * Config.config.biomeFogColorsMaxIntensity;
        public static float caveDepthFogStrength  = ( 0.5F / 2.0F ) * Config.config.caveDepthFogMaxIntensity;
        public static float lightLevelFogStrength = ( 0.5F / 2.0F ) * Config.config.lightLevelFogMaxIntensity;
        public static float morningFogStrength    = ( 0.5F / 2.0F ) * Config.config.morningFogMaxIntensity;
        public static float voidFogStrength       = ( 0.5F / 2.0F ) * Config.config.voidFogMaxIntensity;
        public static float morningFogRng         = 0.5F;
        public static int   startTimeOffset       = 1000;
        public static long  morningFogDuration    = 1000L;
        public static int   caveFogDepthOffset    = 4;

        /** - Target fog values */
        public static Float targetFogDensity     = Config.config.baseFogIntensity;
        public static Float targetFogRed         = 1.0F;
        public static Float targetFogGreen       = 1.0F;
        public static Float targetFogBlue        = 1.0F;

        /** - Current fog values */
        public static Float fogDensityMultiplier = Config.config.baseFogIntensity;
        public static Float fogRedMultiplier     = 1.0F;
        public static Float fogGreenMultiplier   = 1.0F;
        public static Float fogBlueMultiplier    = 1.0F;
    }
}
