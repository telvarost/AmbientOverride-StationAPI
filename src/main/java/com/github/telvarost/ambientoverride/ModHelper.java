package com.github.telvarost.ambientoverride;

import java.util.Random;

public class ModHelper {

    public static float growthApproach(float currentValue, float targetValue, float speed) {
        if (Math.abs(currentValue - targetValue) > 0.001F) {
            currentValue += (targetValue - currentValue) * speed;
        } else {
            currentValue = targetValue;
        }

        return currentValue;
    }

    public static float linearApproach(float currentValue, float targetValue, float speed) {
        if (currentValue < (targetValue - speed)) {
            currentValue += speed;
        } else if (currentValue > (targetValue + speed)) {
            currentValue -= speed;
        } else {
            currentValue = targetValue;
        }

        return currentValue;
    }

    public static void randomizeFogValues(Random random) {
        /** - Randomize fog values */
        ModHelper.Fields.biomeFogColorStrength = ( random.nextFloat() * 2.0F ) * Config.config.biomeFogColorsMaxIntensity;
        ModHelper.Fields.caveDepthFogStrength  = ( random.nextFloat() / 2.0F ) * Config.config.caveDepthFogMaxIntensity;
        ModHelper.Fields.lightLevelFogStrength = ( random.nextFloat() / 2.0F ) * Config.config.lightLevelFogMaxIntensity;
        ModHelper.Fields.morningFogStrength    = ( random.nextFloat() / 2.0F ) * Config.config.morningFogMaxIntensity;
        ModHelper.Fields.voidFogStrength       = Config.config.voidFogIntensity;
        if (1.0F > Config.config.morningFogProbability) {
            ModHelper.Fields.morningFogRng = random.nextFloat();
        } else {
            ModHelper.Fields.morningFogRng = 1.0F;
        }
        ModHelper.Fields.startTimeOffset       = random.nextInt(6000);
        ModHelper.Fields.morningFogDuration    = random.nextInt(6000 - ModHelper.Fields.startTimeOffset) + 1000L;
        ModHelper.Fields.caveFogDepthOffset    = random.nextInt(9);
    }

    public static void setConstantFogValues(Random random) {
        /** - Randomize fog values */
        ModHelper.Fields.biomeFogColorStrength = Config.config.biomeFogColorsMaxIntensity;
        ModHelper.Fields.caveDepthFogStrength  = Config.config.caveDepthFogMaxIntensity;
        ModHelper.Fields.lightLevelFogStrength = Config.config.lightLevelFogMaxIntensity;
        ModHelper.Fields.morningFogStrength    = Config.config.morningFogMaxIntensity;
        ModHelper.Fields.voidFogStrength       = Config.config.voidFogIntensity;
        if (1.0F > Config.config.morningFogProbability) {
            ModHelper.Fields.morningFogRng = random.nextFloat();
        } else {
            ModHelper.Fields.morningFogRng = 1.0F;
        }
        ModHelper.Fields.startTimeOffset       = random.nextInt(6000);
        ModHelper.Fields.morningFogDuration    = random.nextInt(6000 - ModHelper.Fields.startTimeOffset) + 1000L;
        ModHelper.Fields.caveFogDepthOffset    = 4;
    }

    public static class Fields {
        /** - Initialize daily fog values */
        public static float biomeFogColorStrength = 1.0F * Config.config.biomeFogColorsMaxIntensity;
        public static float caveDepthFogStrength  = ( 0.5F / 2.0F ) * Config.config.caveDepthFogMaxIntensity;
        public static float lightLevelFogStrength = ( 0.5F / 2.0F ) * Config.config.lightLevelFogMaxIntensity;
        public static float morningFogStrength    = ( 0.5F / 2.0F ) * Config.config.morningFogMaxIntensity;
        public static float voidFogStrength       = Config.config.voidFogIntensity;
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
