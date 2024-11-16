package com.github.telvarost.ambientoverride.mixin;

import com.github.telvarost.ambientoverride.Config;
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
import org.spongepowered.asm.mixin.Unique;
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

    /** - Initialize daily fog values */
    @Unique private float biomeFogColorStrength = (( 0.5F * 2.0F ) + 1.0F) * Config.config.biomeFogColorMaxIntensity;
    @Unique private float caveDepthFogStrength  = ( 0.5F / 2.0F )          * Config.config.caveDepthFogMaxIntensity;
    @Unique private float lightLevelFogStrength = ( 0.5F / 2.0F )          * Config.config.lightLevelFogMaxIntensity;
    @Unique private float morningFogStrength    = 1.0F;//(random.nextFloat() / 2.0F) * Config.config.morningFogMaxIntensity;
    @Unique private float morningFogRng         = 0.5F;
    @Unique private int   startTimeOffset       = 0;
    @Unique private long  morningFogDuration    = 2000L;

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tick(CallbackInfo ci) {
        /** - Get current time */
        long currentTime = properties.getTime();
        long currentTimeOfDay = currentTime % 24000L;
        long currentDay = currentTime / 24000L;

        if (18000L == currentTimeOfDay) {
            /** - Get daily fog values */
            biomeFogColorStrength = ( (random.nextFloat() * 2.0F) + 1.0F ) * Config.config.biomeFogColorMaxIntensity;
            caveDepthFogStrength  = ( random.nextFloat() / 2.0F )          * Config.config.caveDepthFogMaxIntensity;
            lightLevelFogStrength = ( random.nextFloat() / 2.0F )          * Config.config.lightLevelFogMaxIntensity;
            morningFogStrength    = 1.0F;//(random.nextFloat() / 2.0F) * Config.config.morningFogMaxIntensity;
            if (1.0F > Config.config.morningFogProbability) {
                morningFogRng = random.nextFloat();
            } else {
                morningFogRng = 1.0F;
            }
            startTimeOffset       = random.nextInt(6000);
            morningFogDuration    = random.nextInt(6000 - startTimeOffset) + 1000L;
        }

        if (0 == currentTimeOfDay % 100) {

            /** - Initialize environment and player */
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            String biomeName = "Unknown";
            Biome biome = null;
            double depth = 60;
            float light = 1.0F;

            /** - Get environment around player */
            if (null != player) {
                light = player.getBrightnessAtEyes(1.0F);
                depth = player.y;

                if (null != this.dimension.biomeSource) {
                    biome = this.dimension.biomeSource.getBiome((int) Math.floor(player.x), (int) Math.floor(player.z));
                    if (null != biome) {
                        biomeName = biome.name;
                    }
                }
            }

            /** - Get target biome color */
            ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
            ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
            ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
            if (Config.config.enableBiomeFogColors) {
                if (Biome.RAINFOREST == biome) { // + .2
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (-0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                } else if (Biome.SWAMPLAND == biome) { // + .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (-0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                } else if (Biome.SEASONAL_FOREST == biome) { // + .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.FOREST == biome) { // - .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (-0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.SAVANNA == biome) { // + .4
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.SHRUBLAND == biome) { // + .8
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.3F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.3F * biomeFogColorStrength);
                } else if (Biome.TAIGA == biome) { // + .6
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.4F * biomeFogColorStrength);
                } else if (Biome.DESERT == biome) { // + .6
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.4F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.3F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (-0.1F * biomeFogColorStrength);
                } else if (Biome.PLAINS == biome) { // + .5
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                } else if (Biome.ICE_DESERT == biome) { // + 2.5
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.7F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.8F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (1.0F * biomeFogColorStrength);
                } else if (Biome.TUNDRA == biome) { // + 1.1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.4F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.5F * biomeFogColorStrength);
                } else if (Biome.HELL == biome) { // - .7
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (-0.2F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (-0.5F * biomeFogColorStrength);
                } else if (Biome.SKY == biome) { // + 1.3
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.6F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.3F * biomeFogColorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.4F * biomeFogColorStrength);
                } else { // + .0
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                }
            }

            /** - Get target cave fog density */
            float caveFogInverted = 1.0F;
            if (Config.config.enableCaveDepthFog) {
                int caveFogDepthOffset = random.nextInt(9);
                if ((64 - caveFogDepthOffset) > depth) {
                    caveFogInverted = (((float) depth - (32 - caveFogDepthOffset)) * ((float) depth - (32 - caveFogDepthOffset))) / 1024.0F;
                }
            }

            /** - Get target morning fog density */
            float morningFog = 0.0F;
            if (Config.config.enableMorningFog) {
                long morningFogStartTime = ((18000L + startTimeOffset) % 24000L);
                long offsetMorningFogStartTime = (12000L < morningFogStartTime) ? (morningFogStartTime - 24000L) : morningFogStartTime;
                long offsetCurrentTimeOfDay = (12000L < currentTimeOfDay) ? (currentTimeOfDay - 24000L) : currentTimeOfDay;
                System.out.println("Duration: " + morningFogDuration + ", Start Time: " + morningFogStartTime + ", Probability: " + morningFogRng);

                if (morningFogRng > (1.0F - Config.config.morningFogProbability)) {
                    System.out.println("True!");
                    if (offsetCurrentTimeOfDay >= offsetMorningFogStartTime
                            && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + 1000L)
                    ) {
                        morningFog = (float) (offsetCurrentTimeOfDay - offsetMorningFogStartTime) / 1000;
                    } else if (offsetCurrentTimeOfDay >= (offsetMorningFogStartTime + 1000L)
                            && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + morningFogDuration)
                    ) {
                        morningFog = 1.0F;
                    } else if (offsetCurrentTimeOfDay >= (offsetMorningFogStartTime + morningFogDuration)
                            && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + morningFogDuration + 1000L)
                    ) {
                        morningFog = (float) (1000 - (offsetCurrentTimeOfDay - (offsetMorningFogStartTime + morningFogDuration))) / 1000;
                    }
                }
            }

            /** - Calculate total target fog density */
            ModHelper.ModHelperFields.fogDensityMultiplier = 0.5F                                                 // Base Fog Strength
                                                           + 0//((1.0F - light) * lightLevelFogStrength)          // Light Level Fog
                                                           + 0//((1.0F - caveFogInverted) * caveDepthFogStrength) // Cave Depth Fog
                                                           +    (morningFog) * morningFogStrength;                // Morning Fog

            System.out.println("Day: " + currentDay + ", Time: " + currentTimeOfDay + ", Biome: " + biomeName);
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
        return Vec3d.createCached( x * ModHelper.ModHelperFields.fogRedMultiplier
                                 , y * ModHelper.ModHelperFields.fogGreenMultiplier
                                 , z * ModHelper.ModHelperFields.fogBlueMultiplier
                                 );
    }
}
