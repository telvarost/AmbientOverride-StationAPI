package com.github.telvarost.ambientoverride.mixin;

import com.github.telvarost.ambientoverride.Config;
import com.github.telvarost.ambientoverride.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
            method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void setWorld(World world, String message, PlayerEntity player, CallbackInfo ci) {
        Random random = new Random();
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
