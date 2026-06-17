/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.bedrock.placing.bedrock_placing.Network;

public class Bedrock_PlacingClient implements ClientModInitializer {

    public static boolean serverDisabled = false;

    @Override
    public void onInitializeClient() {
        BedrockPlacingConfig.load();

        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
                .registerGlobalReceiver(
                        Network.ID,
                        (payload, context) -> {
                            context.client().execute(() -> serverDisabled = true);
                        }
                );

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverDisabled = false;
        });
    }
}