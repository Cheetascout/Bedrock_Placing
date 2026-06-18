/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.HitResult;
import org.bedrock.placing.bedrock_placing.Network;

public class Bedrock_PlacingClient implements ClientModInitializer {

    public static boolean serverDisabled = false;
    private int holdTicks = 0;
    private static final int WAIT_TICKS = 10;

    @Override
    public void onInitializeClient() {
        BedrockPlacingConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

        ClientPlayNetworking.registerGlobalReceiver(
                Network.ID,
                (payload, context) -> context.client().execute(() -> serverDisabled = true)
        );

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> serverDisabled = false);
    }

    private void onClientTick(MinecraftClient client) {
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
        if (serverDisabled) {
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
                client.itemUseCooldown = 0;
            }
        } else {
            holdTicks = 0;
        }
    }
}