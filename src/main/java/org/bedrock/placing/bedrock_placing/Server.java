/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class Server implements ModInitializer {

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(
                Network.ID,
               Network.CODEC
        );

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!server.isDedicated()) {
                return;
            }
            ServerPlayNetworking.send(handler.player, new Network());
        });
    }
}