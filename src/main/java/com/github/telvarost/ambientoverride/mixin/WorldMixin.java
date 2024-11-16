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
import net.minecraft.world.biome.source.BiomeSource;
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

import java.awt.*;
import java.util.Random;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow protected WorldProperties properties;

    @Shadow @Final public Dimension dimension;

    @Shadow public Random random;
    @Unique private int counter = 0;

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tick(CallbackInfo ci) {
        long currentTime = properties.getTime();
        long currentTimeOfDay = currentTime % 24000L;
        long currentDay = currentTime / 24000L;

        if (0 == currentTimeOfDay % 100) {

            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            String biomeName = "Unknown";
            Biome biome = null;
            int lightLevel = 0;
            Color color = Color.decode("0xFFFFFF");
            float brightness = 1.0F;
            double depth = 60;
            float light = 1.0F;

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

            float colorStrength = 0.5F + random.nextFloat();
            float horrorFogStrength = random.nextFloat();
            float caveFogStrength = random.nextFloat();
            int caveFogDepthOffset = random.nextInt(9);

            if (Config.config.enableBiomeFogColors) {
                if (Biome.RAINFOREST == biome) { // + .2
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (-0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.1F * colorStrength);
                } else if (Biome.SWAMPLAND == biome) { // + .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (-0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.1F * colorStrength);
                } else if (Biome.SEASONAL_FOREST == biome) { // + .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.FOREST == biome) { // - .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (-0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.SAVANNA == biome) { // + .4
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.SHRUBLAND == biome) { // + .8
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.3F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.3F * colorStrength);
                } else if (Biome.TAIGA == biome) { // + .6
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.4F * colorStrength);
                } else if (Biome.DESERT == biome) { // + .6
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.4F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.3F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (-0.1F * colorStrength);
                } else if (Biome.PLAINS == biome) { // + .5
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.1F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.2F * colorStrength);
                } else if (Biome.ICE_DESERT == biome) { // + 2.5
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.7F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.8F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (1.0F * colorStrength);
                } else if (Biome.TUNDRA == biome) { // + 1.1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.4F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.5F * colorStrength);
                } else if (Biome.HELL == biome) { // - .7
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (-0.2F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (-0.5F * colorStrength);
                } else if (Biome.SKY == biome) { // + 1.3
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F + (0.6F * colorStrength);
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F + (0.3F * colorStrength);
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F + (0.4F * colorStrength);
                } else { // + .0
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                }
            } else {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
            }

            float caveFog = 1.0F;
            if (Config.config.enableCaveDepthFog) {

                if ((64 - caveFogDepthOffset) > depth) {
                    caveFog = (((float) depth - (32 - caveFogDepthOffset)) * ((float) depth - (32 - caveFogDepthOffset))) / 1024.0F;
                }

                //ModHelper.ModHelperFields.fogDensityMultiplier = 0.0F + ((float)(depth / 128.0F) * 1.0F);
            }

            int morningFogChance = random.nextInt(3);
            long morningFogStartTime = 20000L;//((20000L + random.nextInt(6000)) % 24000L);
            long offsetMorningFogStartTime = (12000L < morningFogStartTime) ? (morningFogStartTime - 24000L) : morningFogStartTime;
            long offsetCurrentTimeOfDay = (12000L < currentTimeOfDay) ? (currentTimeOfDay - 24000L) : currentTimeOfDay;
            long morningFogDuration = 6000L;//random.nextInt(6000) + 1000L;
            float morningFog = 0.0F;
            float morningFogStrength = 1.0F;//random.nextFloat();

            if (  offsetCurrentTimeOfDay >= offsetMorningFogStartTime
               && offsetCurrentTimeOfDay < (offsetMorningFogStartTime + 1000L)
            ) {
                morningFog = (float) (offsetCurrentTimeOfDay - offsetMorningFogStartTime) / 1000;
            } else if (  offsetCurrentTimeOfDay >= (offsetMorningFogStartTime + 1000L)
                      && offsetCurrentTimeOfDay <  (offsetMorningFogStartTime + morningFogDuration)
            ) {
                morningFog = 1.0F;
            } else if (  offsetCurrentTimeOfDay >= (offsetMorningFogStartTime + morningFogDuration)
                      && offsetCurrentTimeOfDay <  (offsetMorningFogStartTime + morningFogDuration + 1000L)
            ) {
                morningFog = (float) (1000 - (offsetCurrentTimeOfDay - (offsetMorningFogStartTime + morningFogDuration))) / 1000;
            }

            ModHelper.ModHelperFields.fogDensityMultiplier = 0.5F                                                    // Base Fog Value
                                                           + 0//(((1.0F / 4.0F) - (light / 4.0F)) * horrorFogStrength)  // Horror Fog
                                                           + 0//(((1.0F / 4.0F) - (caveFog / 4.0F)) * caveFogStrength)  // Cave Fog
                                                           + (0.5F * morningFog) * morningFogStrength;               // Morning Fog

            System.out.println("Day: " + currentDay + ", Time: " + offsetCurrentTimeOfDay + ", Start: " + offsetMorningFogStartTime + ", Biome: " + biomeName);
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
