package com.github.telvarost.ambientoverride.mixin;

import com.github.telvarost.ambientoverride.Config;
import com.github.telvarost.ambientoverride.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.FloatBuffer;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    private float viewDistance;

    @Shadow private Minecraft client;

    @Shadow private FloatBuffer fogColorBuffer;

    @Shadow private float fogRed;

    @Shadow private float fogGreen;

    @Shadow private float fogBlue;

    @Shadow private boolean renderFog;

    @Unique private float uniqueRed;

    @Unique private float uniqueGreen;

    @Unique private float uniqueBlue;

    @Inject(method = "applyFog", at = @At(value = "HEAD"))
    public void clientsideEssentials_overrideFogDensity(int f, float par2, CallbackInfo ci) {
        uniqueRed = fogRed;
        uniqueGreen = fogGreen;
        uniqueBlue = fogBlue;

        if (Config.config.enableDynamicFog) {
            this.viewDistance = (256 >> this.client.options.viewDistance) * ModHelper.ModHelperFields.fogDensityMultiplier;
        }
    }

    @Inject(
            method = "updateSkyAndFogColors",
            at = @At("HEAD"),
            cancellable = true
    )
    private void clientsideEssentials_method_1852(float f, CallbackInfo ci) {
        if (Config.config.enableColoredFog) {
            World level = this.client.world;
            LivingEntity living = this.client.camera;
            Vec3d vec3f = level.getSkyColor(this.client.camera, f);
            float red = (float) vec3f.x;
            float green = (float) vec3f.y;
            float blue = (float) vec3f.z;
            if (this.renderFog) {
                Vec3d vec3f3 = level.getCloudColor(f);
                this.fogRed = (float) vec3f3.x;
                this.fogGreen = (float) vec3f3.y;
                this.fogBlue = (float) vec3f3.z;
                red = this.fogRed;
                green = this.fogGreen;
                blue = this.fogBlue;
            } else if (living.isInFluid(Material.WATER)) {
                this.fogRed = 0.02f;
                this.fogGreen = 0.02f;
                this.fogBlue = 0.2f;
                red = this.fogRed;
                green = this.fogGreen;
                blue = this.fogBlue;
            } else if (living.isInFluid(Material.LAVA)) {
                this.fogRed = 0.6f;
                this.fogGreen = 0.1f;
                this.fogBlue = 0.0f;
                red = this.fogRed;
                green = this.fogGreen;
                blue = this.fogBlue;
            }
            if (0.0F == ModHelper.ModHelperFields.fogDensityMultiplier) {
                GL11.glClearColor(red, green, blue, 0.0f);
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "updateSkyAndFogColors",
            at = @At("RETURN"),
            cancellable = true
    )
    private void clientsideEssentials_updateSkyAndFogColors(float f, CallbackInfo ci) {
        if (Config.config.enableColoredFog) {
            GL11.glClearColor(uniqueRed * ModHelper.ModHelperFields.fogRedMultiplier
                             , uniqueGreen * ModHelper.ModHelperFields.fogGreenMultiplier
                             , uniqueBlue * ModHelper.ModHelperFields.fogBlueMultiplier
                             , 0.0f);
        }
    }

    @Inject(
            method = "updateFogColorBuffer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void clientsideEssentials_updateFogColorBuffer(float red, float green, float blue, float i, CallbackInfoReturnable<FloatBuffer> cir) {
        if (Config.config.enableColoredFog) {
            this.fogColorBuffer.clear();
            this.fogColorBuffer.put(red   * ModHelper.ModHelperFields.fogRedMultiplier  )
                               .put(green * ModHelper.ModHelperFields.fogGreenMultiplier)
                               .put(blue  * ModHelper.ModHelperFields.fogBlueMultiplier )
                               .put(i);
            this.fogColorBuffer.flip();
            cir.setReturnValue(this.fogColorBuffer);
        }
    }
}
