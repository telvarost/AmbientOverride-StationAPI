package com.github.telvarost.ambientoverride.mixin;

import com.github.telvarost.ambientoverride.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
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
            System.out.println("Day: " + currentDay + ", Time: " + currentTimeOfDay + ", Total: " + currentTime);
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
