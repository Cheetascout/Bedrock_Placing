/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.bedrock.placing.bedrock_placing.client.BedrockPlacingConfig;
import org.bedrock.placing.bedrock_placing.client.Bedrock_PlacingClient;
import net.minecraft.item.BlockItem;

@Mixin(MinecraftClient.class)
public class PlacingMixin {

    @Shadow
    public int itemUseCooldown;

    private int holdTicks = 0;
    private static final int WAIT_TICKS = 10;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient)(Object)this;

        if (client.player == null || client.world == null) {
            holdTicks = 0;
            return;
        }
        if (client.player.isUsingItem()) {
            holdTicks = 0;
            return;
        }
        if (!BedrockPlacingConfig.modEnabled) {
            holdTicks = 0;
            return;
        }
        if (BedrockPlacingConfig.creativeOnly && !client.player.isCreative()) {
            holdTicks = 0;
            return;
        }
        if (Bedrock_PlacingClient.serverDisabled) {
            holdTicks = 0;
            return;
        }

        boolean useHeld = client.options.useKey.isPressed();
        HitResult hit = client.crosshairTarget;
        boolean aimingAtBlock = hit != null && hit.getType() == HitResult.Type.BLOCK;
        boolean holdingBlock = client.player.getMainHandStack().getItem() instanceof BlockItem || client.player.getOffHandStack().getItem() instanceof BlockItem;

        if (useHeld && aimingAtBlock && holdingBlock) {
            holdTicks++;
            if (holdTicks >= WAIT_TICKS) {
                itemUseCooldown = 0;
            }
        } else {
            holdTicks = 0;
        }
    }
}