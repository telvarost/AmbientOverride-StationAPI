package com.github.telvarost.ambientoverride.mixin;

import com.github.telvarost.ambientoverride.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {

    @Shadow protected WorldProperties properties;

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

            if (null != player.world) {
                if (null != player.world.method_1781()) {
                    biome = player.world.method_1781().getBiome((int)Math.floor(player.x), (int)Math.floor(player.z));
                    if (null != biome) {
                        biomeName = biome.name;
                    }
                }
            }

            if (Biome.SKY == biome) {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.25F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 0.75F;
            } else if (Biome.FOREST == biome) {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.00F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.25F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 0.5F;
            } else if (Biome.SEASONAL_FOREST == biome) {
                ModHelper.ModHelperFields.fogRedMultiplier = 0.5F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 0.5F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 1.50F;
            } else if (Biome.SHRUBLAND == biome) {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.50F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 0.5F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 1.50F;
            } else if (Biome.SAVANNA == biome) {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.25F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 0.75F;
            } else if (Biome.DESERT == biome) {
                ModHelper.ModHelperFields.fogRedMultiplier = 2.00F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 0.5F;
            } else {
                ModHelper.ModHelperFields.fogRedMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogGreenMultiplier = 1.0F;
                ModHelper.ModHelperFields.fogBlueMultiplier = 1.0F;
            }

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
