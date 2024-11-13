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

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow protected WorldProperties properties;

    @Shadow @Final public Dimension dimension;

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

            if (null != player) {
                float light = player.getBrightnessAtEyes(1.0F);
                depth = player.y;

                if (light < 0.06) {
                    lightLevel = 0;
                } else if (light < 0.07) {
                    lightLevel = 1;
                } else if (light < 0.09) {
                    lightLevel = 2;
                } else if (light < 0.125) {
                    lightLevel = 3;
                } else if (light < 0.14) {
                    lightLevel = 4;
                } else if (light < 0.16) {
                    lightLevel = 5;
                } else if (light < 0.2) {
                    lightLevel = 6;
                } else if (light < 0.25) {
                    lightLevel = 7;
                } else if (light < 0.3) {
                    lightLevel = 8;
                } else if (light < 0.325) {
                    lightLevel = 9;
                } else if (light < 0.4) {
                    lightLevel = 10;
                } else if (light < 0.5) {
                    lightLevel = 11;
                } else if (light < 0.625) {
                    lightLevel = 12;
                } else if (light < 0.75) {
                    lightLevel = 13;
                } else if (light < 0.875) {
                    lightLevel = 14;
                } else {
                    lightLevel = 15;
                }

                if (null != this.dimension.biomeSource) {
                    biome = this.dimension.biomeSource.getBiome((int)Math.floor(player.x), (int)Math.floor(player.z));
                    if (null != biome) {
                        biomeName = biome.name;
                    }
                }
            }

            if (Config.config.enableBiomeFogColors) {
                if (Biome.RAINFOREST == biome) { // + .2
                    ModHelper.ModHelperFields.fogRedMultiplier = 0.9F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.2F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.1F;
                } else if (Biome.SWAMPLAND == biome) { // + .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 0.9F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.1F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.1F;
                } else if (Biome.SEASONAL_FOREST == biome) { // + .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.1F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.FOREST == biome) { // - .1
                    ModHelper.ModHelperFields.fogRedMultiplier = 0.9F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                } else if (Biome.SAVANNA == biome) { // + .4
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.1F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.2F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.1F;
                } else if (Biome.SHRUBLAND == biome) { // + .8
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.3F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.2F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.3F;
                } else if (Biome.TAIGA == biome) { // + .7
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.1F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.2F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.4F;
                } else if (Biome.DESERT == biome) { // + .6
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.4F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.3F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 0.9F;
                } else if (Biome.PLAINS == biome) { // + .5
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.2F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.1F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.2F;
                } else if (Biome.ICE_DESERT == biome) { // + 2.4
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.7F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.7F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 2.0F;
                } else if (Biome.TUNDRA == biome) { // + 1.1
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.4F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.2F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.5F;
                } else if (Biome.HELL == biome) { // - .7
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 0.8F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 0.5F;
                } else if (Biome.SKY == biome) { // + 1.3
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.6F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.3F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.4F;
                } else { // + .0
                    ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                    ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
                }
            }
            else
            {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
            }

            if (Config.config.enableCaveDepthFog) {

                if (128.0 < depth) {
                    depth = 128.0;
                } else if (1.0 > depth) {
                    depth = 1.0;
                }

                ModHelper.ModHelperFields.fogDensityMultiplier = 0.0F + ((float)(depth / 128.0F) * 1.0F);
            }
            //ModHelper.ModHelperFields.fogDensityMultiplier = 2.0F / (16 - lightLevel);

            System.out.println("Day: " + currentDay + ", Time: " + currentTimeOfDay + ", Total: " + currentTime + ", Biome: " + biomeName);
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
