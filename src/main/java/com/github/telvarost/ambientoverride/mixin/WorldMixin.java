package com.github.telvarost.ambientoverride.mixin;

import com.github.telvarost.ambientoverride.Config;
import com.github.telvarost.ambientoverride.FogApproachEnum;
import com.github.telvarost.ambientoverride.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.*;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow protected WorldProperties properties;

    @Shadow @Final public Dimension dimension;

    @Shadow public Random random;

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tick(CallbackInfo ci) {
        /** - Get current time */
        long currentTime = properties.getTime();
        long currentTimeOfDay = currentTime % 24000L;

        if (18000L == currentTimeOfDay) {
            /** - Get daily fog values */
            if (Config.config.allowRandomization) {
                ModHelper.randomizeFogValues(random);
            } else {
                ModHelper.setConstantFogValues(random);
            }
        }

        if (1 == currentTimeOfDay % 20) {
            /** - Initialize environment and player */
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            Biome biome = null;
            double depth = 64;
            float light = 1.0F;

            /** - Get environment around player */
            if (null != player) {
                if (Config.config.enableLightLevelFog) {
                    light = player.getBrightnessAtEyes(1.0F);
                }
                depth = player.y;

                if (null != this.dimension.biomeSource) {
                    biome = this.dimension.biomeSource.getBiome((int) Math.floor(player.x), (int) Math.floor(player.z));
                }
            }

            /** - Initialize target fog colors */
            ModHelper.Fields.targetFogRed = 1.0F;
            ModHelper.Fields.targetFogGreen = 1.0F;
            ModHelper.Fields.targetFogBlue = 1.0F;
            float appliedBiomeFogColorStrength = ModHelper.Fields.biomeFogColorStrength;
            float caveFogColor = 1.0F;

            /** - Desaturate biome fog colors */
            if (Config.config.enableCaveDepthFogColors) {
                if (64 > depth) {
                    caveFogColor = (float) depth / 64.0F;
                    appliedBiomeFogColorStrength *= caveFogColor;
                }
            }

            /** - Get target biome color */
            if (Config.config.enableBiomeFogColors) {
                if (Biome.RAINFOREST == biome) { // - .2
                    ModHelper.Fields.targetFogRed = 0.85F + (0.15F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (-0.05F * ModHelper.Fields.biomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 0.85F + (0.15F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (0.2F * ModHelper.Fields.biomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 0.8F + (0.2F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (0.2F * ModHelper.Fields.biomeFogColorStrength);
                } else if (Biome.SWAMPLAND == biome) { // - .15
                    ModHelper.Fields.targetFogRed = 0.95F + (0.05F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (-0.1F * ModHelper.Fields.biomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 0.9F + (0.1F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (0.05F * ModHelper.Fields.biomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 0.9F + (0.1F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (0.1F * ModHelper.Fields.biomeFogColorStrength);
                } else if (Biome.SEASONAL_FOREST == biome) { // + .1
                    ModHelper.Fields.targetFogRed = 1.0F;
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.1F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F;
                } else if (Biome.FOREST == biome) { // - .1
                    ModHelper.Fields.targetFogRed = 1.0F + (-0.1F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F;
                    ModHelper.Fields.targetFogBlue = 1.0F;
                } else if (Biome.SAVANNA == biome) { // + .4
                    ModHelper.Fields.targetFogRed = 1.0F + (0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F;
                } else if (Biome.SHRUBLAND == biome) { // + .8
                    ModHelper.Fields.targetFogRed = 1.0F + (0.3F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (0.3F * appliedBiomeFogColorStrength);
                } else if (Biome.TAIGA == biome) { // + .6
                    ModHelper.Fields.targetFogRed = 1.0F + (0.1F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.1F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (0.4F * appliedBiomeFogColorStrength);
                } else if (Biome.DESERT == biome) { // + .6
                    ModHelper.Fields.targetFogRed = 1.0F + (0.4F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.3F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (-0.1F * appliedBiomeFogColorStrength);
                } else if (Biome.PLAINS == biome) { // + .5
                    ModHelper.Fields.targetFogRed = 1.0F + (0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.1F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (0.2F * appliedBiomeFogColorStrength);
                } else if (Biome.ICE_DESERT == biome) { // + 2.5
                    ModHelper.Fields.targetFogRed = 1.0F + (0.7F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.8F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (1.0F * appliedBiomeFogColorStrength);
                } else if (Biome.TUNDRA == biome) { // + 1.1
                    ModHelper.Fields.targetFogRed = 1.0F + (0.4F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (0.5F * appliedBiomeFogColorStrength);
                } else if (Biome.HELL == biome) { // 0
                    ModHelper.Fields.targetFogRed = 1.2F + (-0.2F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (-0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.2F + (-0.2F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (-0.2F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.2F + (-0.2F * (1.0F - Config.config.biomeFogColorsMaxIntensity)) + (-0.2F * appliedBiomeFogColorStrength);
                } else if (Biome.SKY == biome) { // + 1.3
                    ModHelper.Fields.targetFogRed = 1.0F + (0.6F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogGreen = 1.0F + (0.3F * appliedBiomeFogColorStrength);
                    ModHelper.Fields.targetFogBlue = 1.0F + (0.4F * appliedBiomeFogColorStrength);
                } else { // + .0
                    ModHelper.Fields.targetFogRed = 1.0F;
                    ModHelper.Fields.targetFogGreen = 1.0F;
                    ModHelper.Fields.targetFogBlue = 1.0F;
                }
            }

            /** - Get target cave fog color */
            if (Config.config.enableCaveDepthFogColors) {
                if (64 > depth) {
                    caveFogColor = 0.5F + (float) ((depth / 64.0F) * 0.5F);
                    ModHelper.Fields.targetFogRed   *= caveFogColor;
                    ModHelper.Fields.targetFogGreen *= caveFogColor;
                    ModHelper.Fields.targetFogBlue  *= caveFogColor;
                }
            }

            /** - Get target cave fog density */
            float caveFogInverted = 1.0F;
            float voidFog = 0.0F;
            if (Config.config.enableCaveDepthFog) {
                if ((64 - ModHelper.Fields.caveFogDepthOffset) > depth) {
                    caveFogInverted = ( ((float) depth - (32 - ModHelper.Fields.caveFogDepthOffset))
                                      * ((float) depth - (32 - ModHelper.Fields.caveFogDepthOffset))
                                      ) / 1024.0F;
                }
            }

            if (Config.config.enableVoidFog) {
                if (18 >= depth) {
                    if (0 > depth) {
                        voidFog = 1.0F;
                    } else {
                        voidFog = (18.0F - (float)depth) / 18.0F;
                    }
                    float voidFogColorOffset = (1.0F - voidFog) * (1.0F - voidFog) * (1.0F - voidFog) * (1.0F - voidFog);
                    ModHelper.Fields.targetFogRed   *= voidFogColorOffset;
                    ModHelper.Fields.targetFogGreen *= voidFogColorOffset;
                    ModHelper.Fields.targetFogBlue  *= voidFogColorOffset;
                }
            }

            /** - Get target morning fog density */
            float morningFog = 0.0F;
            if (Config.config.enableMorningFog) {
                long morningFogStartTime = ((18000L + ModHelper.Fields.startTimeOffset) % 24000L);
                long offsetMorningFogStartTime = (12000L < morningFogStartTime) ? (morningFogStartTime - 24000L) : morningFogStartTime;
                long offsetCurrentTimeOfDay = (12000L < currentTimeOfDay) ? (currentTimeOfDay - 24000L) : currentTimeOfDay;

                if (ModHelper.Fields.morningFogRng > (1.0F - Config.config.morningFogProbability)) {
                    if (offsetCurrentTimeOfDay >= offsetMorningFogStartTime
                            && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + 1000L)
                    ) {
                        morningFog = (float) (offsetCurrentTimeOfDay - offsetMorningFogStartTime) / 1000;
                    } else if (offsetCurrentTimeOfDay >= (offsetMorningFogStartTime + 1000L)
                            && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + ModHelper.Fields.morningFogDuration)
                    ) {
                        morningFog = 1.0F;
                    } else if (offsetCurrentTimeOfDay >= (offsetMorningFogStartTime + ModHelper.Fields.morningFogDuration)
                            && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + ModHelper.Fields.morningFogDuration + 1000L)
                    ) {
                        morningFog = (float) (1000 - (offsetCurrentTimeOfDay - (offsetMorningFogStartTime + ModHelper.Fields.morningFogDuration))) / 1000;
                    }
                }
            }

            /** - Calculate total target fog density */
            ModHelper.Fields.targetFogDensity = Config.config.baseFogIntensity                                     // Base Fog Strength
                                              + ((1.0F - light) * ModHelper.Fields.lightLevelFogStrength)          // Light Level Fog
                                              + ((1.0F - caveFogInverted) * ModHelper.Fields.caveDepthFogStrength) // Cave Depth Fog
                                              + (morningFog * ModHelper.Fields.morningFogStrength)                 // Morning Fog
                                              + (voidFog * ModHelper.Fields.voidFogStrength);                      // Void Fog
        }

        if (FogApproachEnum.LINEAR == Config.config.fogApproachBehavior) {
            ModHelper.Fields.fogDensityMultiplier = ModHelper.linearApproach (
                    ModHelper.Fields.fogDensityMultiplier,
                    ModHelper.Fields.targetFogDensity,
                    0.001F
            );
        } else {
            ModHelper.Fields.fogDensityMultiplier = ModHelper.growthApproach (
                    ModHelper.Fields.fogDensityMultiplier,
                    ModHelper.Fields.targetFogDensity,
                    0.02F
            );
        }

        if (FogApproachEnum.LINEAR == Config.config.fogApproachBehavior) {
            ModHelper.Fields.fogRedMultiplier = ModHelper.linearApproach (
                    ModHelper.Fields.fogRedMultiplier,
                    ModHelper.Fields.targetFogRed,
                    0.001F
            );
        } else {
            ModHelper.Fields.fogRedMultiplier = ModHelper.growthApproach (
                    ModHelper.Fields.fogRedMultiplier,
                    ModHelper.Fields.targetFogRed,
                    0.02F
            );
        }

        if (FogApproachEnum.LINEAR == Config.config.fogApproachBehavior) {
            ModHelper.Fields.fogGreenMultiplier = ModHelper.linearApproach (
                    ModHelper.Fields.fogGreenMultiplier,
                    ModHelper.Fields.targetFogGreen,
                    0.001F
            );
        } else {
            ModHelper.Fields.fogGreenMultiplier = ModHelper.growthApproach (
                    ModHelper.Fields.fogGreenMultiplier,
                    ModHelper.Fields.targetFogGreen,
                    0.02F
            );
        }

        if (FogApproachEnum.LINEAR == Config.config.fogApproachBehavior) {
            ModHelper.Fields.fogBlueMultiplier = ModHelper.linearApproach (
                    ModHelper.Fields.fogBlueMultiplier,
                    ModHelper.Fields.targetFogBlue,
                    0.001F
            );
        } else {
            ModHelper.Fields.fogBlueMultiplier = ModHelper.growthApproach (
                    ModHelper.Fields.fogBlueMultiplier,
                    ModHelper.Fields.targetFogBlue,
                    0.02F
            );
        }
    }

    @Environment(EnvType.CLIENT)
    @Redirect(
            method = "getSkyColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Vec3d;createCached(DDD)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    public Vec3d getSkyColor(double x, double y, double z) {
        return Vec3d.createCached( x * ModHelper.Fields.fogRedMultiplier
                                 , y * ModHelper.Fields.fogGreenMultiplier
                                 , z * ModHelper.Fields.fogBlueMultiplier
                                 );
    }

    @Inject(
            method = "afterSkipNight",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void afterSkipNight(CallbackInfo ci) {
        /** - Get daily fog values */
        ModHelper.Fields.biomeFogColorStrength = ( random.nextFloat() * 2.0F ) * Config.config.biomeFogColorsMaxIntensity;
        ModHelper.Fields.caveDepthFogStrength  = ( random.nextFloat() / 2.0F ) * Config.config.caveDepthFogMaxIntensity;
        ModHelper.Fields.lightLevelFogStrength = ( random.nextFloat() / 2.0F ) * Config.config.lightLevelFogMaxIntensity;
        ModHelper.Fields.morningFogStrength    = ( random.nextFloat() / 2.0F ) * Config.config.morningFogMaxIntensity;
        if (1.0F > Config.config.morningFogProbability) {
            ModHelper.Fields.morningFogRng = random.nextFloat();
        } else {
            ModHelper.Fields.morningFogRng = 1.0F;
        }
        ModHelper.Fields.startTimeOffset       = random.nextInt(6000);
        ModHelper.Fields.morningFogDuration    = random.nextInt(6000 - ModHelper.Fields.startTimeOffset) + 1000L;
        ModHelper.Fields.caveFogDepthOffset    = random.nextInt(9);
    }
}
